package org.poc.shop.integraton.service;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.poc.shop.dto.request.ClientAddressRequest;
import org.poc.shop.dto.request.OrderUpdateRequest;
import org.poc.shop.dto.response.OrderResponse;
import org.poc.shop.entity.Cart;
import org.poc.shop.entity.Order;
import org.poc.shop.entity.OrderAddress;
import org.poc.shop.enums.CartStatus;
import org.poc.shop.enums.OrderStatus;
import org.poc.shop.repository.OrderRepository;
import org.poc.shop.service.OrderService;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Transactional
class OrderServiceIntegrationTests {

    @Inject
    OrderService orderService;

    @Inject
    OrderRepository orderRepository;

    @Test
    void testPayOrder_CartAndOrderStatusDraft_Success() throws IllegalAccessException {
        Order order = createDraftOrderWithDraftCart();
        orderRepository.persist(order);

        OrderResponse result = orderService.payOrder(order.getId());

        assertNotNull(result);
        assertAll(
                () -> assertEquals(OrderStatus.PAID, result.getStatus())
        );
    }

    @Test
    void testPayOrder_NonExistingOrderId_ThrowsEntityNotFoundException() {

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> orderService.payOrder(UUID.randomUUID()));
        assertEquals("Order not found", thrown.getMessage());
    }

    @Test
    void testPayOrder_CartAndOrderStatusNonDraft_ThrowsIllegalAccessException() {
        Order order = createPaidOrderWithOnDeliveryCart();
        orderRepository.persist(order);

        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, () -> orderService.payOrder(order.getId()));
        assertEquals("Cart or order status isn't draft", thrown.getMessage());
    }

    @Test
    public void testUpdateOrderAddress_CartStatusDraft_Success() throws IllegalAccessException {
        Order order = createDraftOrderWithDraftCart();
        orderRepository.persist(order);

        OrderUpdateRequest updateRequest = new OrderUpdateRequest(createClientAddressRequest().getName() , createClientAddressRequest().getShortDescription() ,createClientAddressRequest().getLatitude() , createClientAddressRequest().getLongitude());
        Order updatedOrder = orderService.updateOrderAddress( updateRequest ,order.getId());

        assertNotNull(updatedOrder);
        assertAll(
                () -> assertEquals("New Address", updatedOrder.getAddress().getName()),
                () -> assertEquals("New short description", updatedOrder.getAddress().getShortDescription()),
                () -> assertEquals(10.0, updatedOrder.getAddress().getLatitude()),
                () -> assertEquals(20.0, updatedOrder.getAddress().getLongitude())
        );
    }

    @Test
    void testUpdateOrderAddress_CartStatusOnDelivery_ThrowsIllegalAccessException() {
        Order order = createPaidOrderWithOnDeliveryCart();
        orderRepository.persist(order);

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(createClientAddressRequest().getName() , createClientAddressRequest().getShortDescription() ,createClientAddressRequest().getLatitude() , createClientAddressRequest().getLongitude());
        IllegalAccessException thrown = assertThrows(IllegalAccessException.class, () -> orderService.updateOrderAddress(orderUpdateRequest , order.getId()));

        assertEquals("Can not update order address because cart status is not DRAFT", thrown.getMessage());

    }

    @Test
    void testUpdateOrderAddress_OrderNotFound_ThrowsEntityNotFoundException() {

        OrderUpdateRequest orderUpdateRequest = new OrderUpdateRequest(createClientAddressRequest().getName() , createClientAddressRequest().getShortDescription() ,createClientAddressRequest().getLatitude() , createClientAddressRequest().getLongitude());
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> orderService.updateOrderAddress(orderUpdateRequest , UUID.randomUUID()));

        assertEquals("Order not found", thrown.getMessage());
    }

    private Order createDraftOrderWithDraftCart() {
        Order order = new Order();
        order.setCart(new Cart());
        order.getCart().setStatus(CartStatus.DRAFT);
        order.setAddress(new OrderAddress());
        order.setStatus(OrderStatus.DRAFT);
        return order;
    }

    private Order createPaidOrderWithOnDeliveryCart() {
        Order order = new Order();
        order.setCart(new Cart());
        order.getCart().setStatus(CartStatus.ON_DELIVERY);
        order.setAddress(new OrderAddress());
        order.setStatus(OrderStatus.PAID);
        return order;
    }

    private ClientAddressRequest createClientAddressRequest() {
        ClientAddressRequest addressRequest = new ClientAddressRequest();
        addressRequest.setName("New Address");
        addressRequest.setShortDescription("New short description");
        addressRequest.setLatitude(10.0);
        addressRequest.setLongitude(20.0);
        return addressRequest;
    }
}
