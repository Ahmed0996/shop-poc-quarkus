package org.poc.shop.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.poc.shop.entity.Shop;
import org.poc.shop.repository.ShopRepository;

import java.util.List;


@ApplicationScoped
public class ShopService {

    @Inject
    private ShopRepository shopRepository;


    @Transactional
    public Shop createShop(Shop shop) {

            shopRepository.persist(shop);
            return shop;

    }

    public Shop getShopById(Long id) {

            return shopRepository.findById(id);

    }


    public Shop updateShop(Long id, Shop updatedShop) {

            Shop shop = shopRepository.findById(id);
            if (shop == null) {
                throw new EntityNotFoundException("Shop not found");
            }
            shop.setName(updatedShop.getName());
            shop.setAddress(updatedShop.getAddress());
            shopRepository.persist(shop);
            return shop;

    }


    public void deleteShop(Long id) {
            Shop shop = shopRepository.findById(id);
            if (shop == null) {
                throw new EntityNotFoundException("Shop not found");
            }
            shopRepository.delete(shop);
    }

    public List<Shop> getAllShops() {
        return shopRepository.findAll().list();
    }


}