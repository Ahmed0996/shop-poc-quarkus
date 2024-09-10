package org.poc.shop.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.poc.shop.entity.Item;

@ApplicationScoped
public class ItemRepository implements PanacheRepository<Item> {

}
