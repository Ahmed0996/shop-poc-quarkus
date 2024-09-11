package org.poc.shop.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.poc.shop.dto.request.ClientAddressRequest;
import org.poc.shop.dto.request.OrderRequest;
import org.poc.shop.dto.request.OrderUpdateRequest;
import org.poc.shop.dto.response.OrderResponse;
import org.poc.shop.entity.*;
import org.poc.shop.enums.CartStatus;
import org.poc.shop.enums.OrderStatus;
import org.poc.shop.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@RequiredArgsConstructor
@Transactional
public class OrderService {

    final CartRepository cartRepository;
    final ClientRepository clientRepository;
    final ShopRepository shopRepository;
    final OrderRepository orderRepository;
    final OrderAddressRepository orderAddressRepository;

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll().list();
        return orders.stream().map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getCreatedAt(), o.getDistance())).collect(Collectors.toList());
    }

    public OrderResponse createOrder(OrderRequest orderRequest) {


        Cart cart = cartRepository.findById(orderRequest.getCartId());
        Order order = new Order();
        if (cart.getStatus() == CartStatus.DRAFT) {
            ClientAddress clientAddress = getClientDefaultAddress(cart.getClient());
            order.setStatus(OrderStatus.DRAFT);
            order.setCart(cart);
            order.setAddress(convertClientToOrder(clientAddress));
            order.setDistance(distanceCalculator(order.getAddress(), cart.getShop()));
            order.setShop(cart.getShop());
            orderRepository.persist(order);
        }
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setCreatedAt(order.getCreatedAt());
        orderResponse.setDistance(order.getDistance());

        return orderResponse;
    }

    public Order updateOrderAddress(OrderUpdateRequest orderUpdateRequest, UUID orderId) throws IllegalAccessException {
        Order order = orderRepository.findByIdOptional(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (order.getCart().getStatus() != CartStatus.DRAFT) {
            throw new IllegalAccessException("Can not update order address because cart status is not DRAFT");
        }
        OrderAddress orderAddress = order.getAddress();
        orderAddress.setName(orderUpdateRequest.getName());
        orderAddress.setShortDescription(orderUpdateRequest.getShortDescription());
        orderAddress.setLatitude(orderUpdateRequest.getLatitude());
        orderAddress.setLongitude(orderUpdateRequest.getLongitude());
        order.setAddress(orderAddress);
        orderRepository.persist(order);

        return order;
    }


    private BigDecimal distanceCalculator(OrderAddress orderAddress, Shop shop) {
        ShopAddress shopAddress = shop.getAddress();
        double distance = calculateDistance(orderAddress.getLatitude(), orderAddress.getLongitude(), shopAddress.getLatitude(), shopAddress.getLongitude());

        return new BigDecimal(distance);

    }

    private ClientAddress getClientDefaultAddress(Client client) {
        List<ClientAddress> clientAddresses = client.getAddresses();
        ClientAddress clientAddress = new ClientAddress();

        for (ClientAddress clientAddress1 : clientAddresses) {
            if (clientAddress1.isDefault()) {
                clientAddress = clientAddress1;
            }

        }
        return clientAddress;
    }

    private OrderAddress convertClientToOrder(ClientAddress clientAddress) {

        OrderAddress orderAddress = new OrderAddress();
        orderAddress.setName(clientAddress.getName());
        orderAddress.setShortDescription(clientAddress.getShortDescription());
        orderAddress.setLongitude(clientAddress.getLongitude());
        orderAddress.setLatitude(clientAddress.getLatitude());
        return orderAddress;

    }


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371;
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }


    public List<Map.Entry<UUID, double[]>> findShortestPath(double startX, double startY, List<Map<UUID, double[]>> points) {
        List<Map.Entry<UUID, double[]>> path = new ArrayList<>();

        double[] startPoint = {startX, startY};
        path.add(new AbstractMap.SimpleEntry<>(null, startPoint));

        List<Map<UUID, double[]>> remainingPoints = new ArrayList<>(points);
        double[] currentPoint = startPoint;

        while (!remainingPoints.isEmpty()) {
            final double[] current = currentPoint;

            Map.Entry<UUID, double[]> nearestPoint = remainingPoints.stream()
                    .map(map -> map.entrySet().iterator().next())
                    .min(Comparator.comparingDouble(entry -> calculateDistance(current[0], current[1], entry.getValue()[0], entry.getValue()[1])))
                    .orElseThrow();


            path.add(nearestPoint);
            remainingPoints.removeIf(map -> map.containsKey(nearestPoint.getKey()));


            currentPoint = nearestPoint.getValue();
        }

        return path;
    }

    public List<OrderResponse> getAllOrdersByStatusAndDate(OrderStatus status, Date createdDate2) {
        List<Order> order = orderRepository.findAllByStatusAndCreatedDate(status, createdDate2);
        return order.stream().map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getCreatedAt(), o.getDistance())).collect(Collectors.toList());
    }


    public OrderResponse payOrder(UUID id) throws IllegalAccessException {
        Order order = orderRepository.findByIdOptional(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (order.getCart().getStatus() == CartStatus.DRAFT && order.getStatus() == OrderStatus.DRAFT) {
            order.getCart().setStatus(CartStatus.ON_DELIVERY);
            order.setStatus(OrderStatus.PAID);
            orderRepository.persist(order);
            return new OrderResponse(order.getId(), order.getStatus(), order.getCreatedAt(), order.getDistance());
        } else {
            throw new IllegalAccessException("Cart or order status isn't draft");
        }
    }

    public OrderAddress getOrderAddressBy(UUID orderId) {
        return orderAddressRepository.findByOrderId(orderId);
    }

    public List<OrderResponse> ordersDeliveryArrangement(Date deliveryDate, Long shopId) {

        List<Order> orders = orderRepository.getAllShopOrdersByStatusAndDate(OrderStatus.PAID, deliveryDate, shopId);
        ShopAddress shopAddress = shopRepository.findById(shopId).getAddress();
        List<Map<UUID, double[]>> deliveryOrders = new ArrayList<>();
        for (Order order : orders) {
            OrderAddress orderAddress = getOrderAddressBy((order.getId()));
            double[] points = {orderAddress.getLatitude(), orderAddress.getLongitude()};
            Map<UUID, double[]> orderData = new HashMap<>();
            orderData.put(order.getId(), points);
            deliveryOrders.add(orderData);
        }


        List<Map.Entry<UUID, double[]>> path = findShortestPath(shopAddress.getLatitude(), shopAddress.getLongitude(), deliveryOrders);

        List<Order> finalOrders = new ArrayList<>();
        boolean startPoint = false;
        for (Map.Entry<UUID, double[]> entry : path) {
            if (startPoint) {
                Order order = orderRepository.findById(entry.getKey());
                finalOrders.add(order);
            }
            startPoint = true;
        }
        return finalOrders.stream().map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getCreatedAt(), o.getDistance())).collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findAllByStatus(status);
        return orders.stream().map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getCreatedAt(), o.getDistance())).collect(Collectors.toList());
    }

    public List<OrderResponse> getAllOrdersByDate(Date deliveryDate) {
        List<Order> orders = orderRepository.findAllByDate(deliveryDate);
        return orders.stream().map(o -> new OrderResponse(o.getId(), o.getStatus(), o.getCreatedAt(), o.getDistance())).collect(Collectors.toList());
    }

}
