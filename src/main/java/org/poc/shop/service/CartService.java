package org.poc.shop.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.poc.shop.dto.request.CartRequest;
import org.poc.shop.dto.request.ItemRequest;
import org.poc.shop.entity.*;
import org.poc.shop.enums.CartStatus;
import org.poc.shop.repository.CartRepository;
import org.poc.shop.repository.ClientRepository;
import org.poc.shop.repository.ItemRepository;
import org.poc.shop.repository.ShopRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartService {

    @Inject
    private CartRepository cartRepository;
    @Inject
    private ClientRepository clientRepository;
    @Inject
    private ShopRepository shopRepository;

    @Inject
    private ItemRepository itemRepository;
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);


    public List<Cart> getAllCarts() {

        return cartRepository.findAll().list();
    }

    public Cart getCart(UUID id) {

        return cartRepository.findById(id);
    }


    public Cart createCart(CartRequest cartRequest) {

        Client client = validateClient(cartRequest.getClientId());
        Shop shop = validateShop(cartRequest.getShopId());

        validateClientAddress(client);

        Cart existingCart = findExistingCart(client, shop);

        if (existingCart != null) {
            logger.info("Cart already exists for client: {} ", client);
            updateCartItems(existingCart, cartRequest);
            cartRepository.persist(existingCart);
            return existingCart;
        } else {
            logger.info("Creating new cart for client: {} ", client);
            Cart newCart = new Cart(client, shop, retrieveItems(cartRequest, new ArrayList<>()));
            cartRepository.persist(newCart);
            return newCart;
        }


    }

    private Client validateClient(Long clientId) {
        Client client = clientRepository.findById(clientId);
        if (client == null) {
            throw new EntityNotFoundException("Client not found");
        }
        return client;
    }

    private Shop validateShop(Long shopId) {
        Shop shop = shopRepository.findById(shopId);
        if (shop == null) {
            throw new EntityNotFoundException("Shop not found, please provide a valid shop id");
        }
        return shop;
    }

    private void validateClientAddress(Client client) {
        if (client.getAddresses() == null || client.getAddresses().isEmpty()) {
            ClientAddress clientAddress = new ClientAddress();
            List<ClientAddress> clientAddresses = clientAddress.findAll().list();
            client.getAddresses().add(clientAddresses.getFirst());
        }
    }

    private Cart findExistingCart(Client client, Shop shop) {
        for (Cart cart : client.getCarts()) {
            if (cart.getShop().getId().equals(shop.getId()) && cart.getStatus() == CartStatus.DRAFT) {
                return cart;
            }
        }
        return null;
    }

    private void updateCartItems(Cart cart, CartRequest cartRequest) {
        List<Item> cartItems = cart.getItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Add new items from the request to the cart
        List<Item> retrievedItems = retrieveItems(cartRequest, cartItems);
        for (Item item : retrievedItems) {
            boolean exist = false;
            for (Item item2 : cartItems) {
                if (item.getId().equals(item2.getId())) {
                    exist = true;
                }
            }
            if (!exist) {
                cartItems.add(item);
            }
        }

        for (Item item : cartItems) {
            for (ItemRequest itemRequest : cartRequest.getItemList()) {
                if (item.getId().equals(itemRequest.getItemId())) {
                    logger.info("Item already exists in cart, updating quantity");
                    item.setQuantity(item.getQuantity().add(itemRequest.getQuantity()));
                    logger.info("Updated item quantity: {}", item.getQuantity());
                }
            }
        }

        cart.setItems(cartItems);
    }


    private List<Item> retrieveItems(CartRequest cartRequest, List<Item> cartItems) {
        for (ItemRequest item : cartRequest.getItemList()) {
            Item selectedItem = itemRepository.findById(item.getItemId());
            if (selectedItem == null) {
                throw new EntityNotFoundException("Item not found, please provide a valid item id");
            } else {
                selectedItem.setQuantity(item.getQuantity());
            }
            cartItems.add(selectedItem);
        }
        return cartItems;
    }


}

