package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.shop.entity.Shop;

@ApplicationScoped
public class ShopRepository implements PanacheRepository<Shop> {

}
