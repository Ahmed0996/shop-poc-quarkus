package org.poc.shop.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.poc.shop.dto.request.ClientAddressRequest;
import org.poc.shop.dto.request.OrderUpdateRequest;
import org.poc.shop.dto.response.OrderResponse;
import org.poc.shop.entity.*;
import org.poc.shop.enums.CartStatus;
import org.poc.shop.enums.OrderStatus;
import org.poc.shop.repository.OrderAddressRepository;
import org.poc.shop.repository.OrderRepository;
import org.poc.shop.repository.ShopRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class OrderServiceTests {


    @Inject
    OrderService orderService;

    @InjectMock
    OrderRepository orderRepository;

    @InjectMock
    OrderAddressRepository orderAddressRepository;

    @InjectMock
    ShopRepository shopRepository;



    private Order order;
    UUID orderId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        order = new Order();
        Cart cart = new Cart();
        cart.setStatus(CartStatus.DRAFT);
        order.setCart(cart);

        OrderAddress address = new OrderAddress();
        address.setName("Old Name");
        address.setShortDescription("Old Description");
        order.setAddress(address);
        order.setStatus(OrderStatus.DRAFT);
    }

    @Test
    void testPayOrder_CartAndOrderStatusDraft_Success() throws IllegalAccessException {
        when(orderRepository.findByIdOptional(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.findById(orderId)).thenReturn(order);

        OrderResponse result = orderService.payOrder(orderId);

        assertNotNull(result);
        assertAll(
                () -> assertEquals(OrderStatus.PAID, result.getStatus())
                //() -> assertEquals(CartStatus.ON_DELIVERY, result.getCart().getStatus())
        );
    }

    @Test
    void testPayOrder_NonExistingOrderId_ThrowsEntityNotFoundException() {
        when(orderRepository.findByIdOptional(orderId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> orderService.payOrder(orderId));

        assertEquals("Order not found", thrown.getMessage());
        verify(orderRepository, never()).persist(any(Order.class));
    }

    @Test
    void testPayOrder_CartAndOrderStatusNonDraft_ThrowsIllegalAccessException() {
        order.setStatus(OrderStatus.PAID);
        order.getCart().setStatus(CartStatus.ON_DELIVERY);
        when(orderRepository.findByIdOptional(orderId)).thenReturn(Optional.of(order));

        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, () -> orderService.payOrder(orderId));

        assertEquals("Cart or order status isn't draft", thrown.getMessage());
        verify(orderRepository, never()).persist(any(Order.class));
    }

    @Test
    void testUpdateOrderAddress_CartStatusDraft_Success() throws IllegalAccessException {
        ClientAddressRequest clientAddressRequest = ClientAddressRequest.builder()
                .name("New Name")
                .shortDescription("New Description")
                .longitude(8.6821)
                .latitude(50.1109)
                .build();
        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(clientAddressRequest.getName() , clientAddressRequest.getShortDescription() ,clientAddressRequest.getLatitude() , clientAddressRequest.getLongitude());
        when(orderRepository.findByIdOptional(orderId)).thenReturn(Optional.of(order));

        Order updatedOrder = orderService.updateOrderAddress(orderUpdateRequest , orderId);

        verify(orderRepository, times(1)).persist(order);
        assertEquals("New Name", updatedOrder.getAddress().getName());
        assertEquals("New Description", updatedOrder.getAddress().getShortDescription());
        assertEquals(50.1109, updatedOrder.getAddress().getLatitude());
        assertEquals(8.6821, updatedOrder.getAddress().getLongitude());
    }

    @Test
    void testUpdateOrderAddress_CartStatusOnDelivery_ThrowsIllegalAccessException() {
        order.getCart().setStatus(CartStatus.ON_DELIVERY);
        when(orderRepository.findByIdOptional(any())).thenReturn(Optional.of(order));

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, () -> orderService.updateOrderAddress(orderUpdateRequest , orderId));

        assertEquals("Can not update order address because cart status is not DRAFT", thrown.getMessage());
        verify(orderRepository, never()).persist(order);
    }

    @Test
    void testUpdateOrderAddress_OrderNotFound_ThrowsEntityNotFoundException() {
        when(orderRepository.findByIdOptional(any())).thenReturn(Optional.empty());

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest();
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderAddress(orderUpdateRequest ,orderId));

        assertEquals("Order not found", thrown.getMessage());
    }

    @Test
    void testFindShortestPath() {
        // Test data
        List<Map<UUID, double[]>> points = new ArrayList<>();

        UUID point1Id = UUID.randomUUID();
        UUID point2Id = UUID.randomUUID();
        UUID point3Id = UUID.randomUUID();

        Map<UUID, double[]> point1 = Map.of(point1Id, new double[]{2.0, 3.0});
        Map<UUID, double[]> point2 = Map.of(point2Id, new double[]{5.0, 1.0});
        Map<UUID, double[]> point3 = Map.of(point3Id, new double[]{1.0, 4.0});

        points.add(point1);
        points.add(point2);
        points.add(point3);


        List<Map.Entry<UUID, double[]>> result = orderService.findShortestPath(0, 0, points);


        assertNotNull(result);
        assertEquals(4, result.size());


        assertNull(result.get(0).getKey());
        assertArrayEquals(new double[]{0.0, 0.0}, result.get(0).getValue());


        assertEquals(point1Id, result.get(1).getKey());
        assertEquals(point3Id, result.get(2).getKey());
        assertEquals(point2Id, result.get(3).getKey());
    }

}
