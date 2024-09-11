package org.poc.shop.dto.response;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.poc.shop.entity.Client;
import org.poc.shop.entity.Item;
import org.poc.shop.entity.Order;
import org.poc.shop.entity.Shop;
import org.poc.shop.enums.CartStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class CartResponse {

    private UUID id;
    private BigDecimal totalAmount;
    private CartStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime updateTimestamp;
    private List<Item> items;
    private Client client;
    private Shop shop;
    private Order order;
}
