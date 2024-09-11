package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.shop.entity.Cart;

import java.util.UUID;

@ApplicationScoped
public class CartRepository implements PanacheRepositoryBase<Cart, UUID> {

}
