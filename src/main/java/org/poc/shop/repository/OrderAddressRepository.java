package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.poc.shop.entity.OrderAddress;

import java.util.UUID;

@ApplicationScoped
public class OrderAddressRepository implements PanacheRepository<OrderAddress> {

    @Transactional
    public OrderAddress createOrderAddress(OrderAddress orderAddress) {
        persist(orderAddress);
        return orderAddress;
    }

    public OrderAddress getOrderAddressById(Long id) {
        return findById(id);
    }

    @Transactional
    public OrderAddress updateOrderAddress(Long id, OrderAddress updatedOrderAddress) {
        OrderAddress orderAddress = findById(id);
        if (orderAddress != null) {
            orderAddress.setLatitude(updatedOrderAddress.getLatitude());
            orderAddress.setLongitude(updatedOrderAddress.getLongitude());
            orderAddress.setName(updatedOrderAddress.getName());
            orderAddress.setShortDescription(updatedOrderAddress.getShortDescription());
            persist(orderAddress);
        }
        return orderAddress;
    }

    @Transactional
    public void deleteOrderAddress(Long id) {
        OrderAddress orderAddress = findById(id);
        if (orderAddress != null) {
            delete(orderAddress);
        }
    }

    public OrderAddress findByOrderId(UUID orderId) {
        return find("SELECT o.address FROM Order o WHERE o.id = ?1", orderId).firstResult();
    }
}