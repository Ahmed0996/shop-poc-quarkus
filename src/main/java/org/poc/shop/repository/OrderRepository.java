package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.shop.entity.Order;
import org.poc.shop.enums.OrderStatus;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class OrderRepository  implements PanacheRepositoryBase<Order, UUID> {
    public List<Order> findAllByStatusAndCreatedDate(OrderStatus status, Date createdDate) {
        return find("status = ?1 and DATE(createdAt) = DATE(?2)", status, createdDate).list();
    }

    public List<Order>getAllShopOrdersByStatusAndDate(OrderStatus status, Date createdDate , Long shopId) {
        return find("status = ?1 and DATE(createdAt) = DATE(?2) and shop.id = ?3", status, createdDate , shopId).list();
    }

    public List<Order>findAllByStatus(OrderStatus status) {
        return find("status", status).list();
    }

    public List<Order> findAllByDate(Date createdDate) {
        return find("DATE(createdAt) = DATE(?1)", createdDate).list();
    }

}
