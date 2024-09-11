package org.poc.shop.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.poc.shop.enums.CartStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Cart extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private CartStatus status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateTimestamp;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonManagedReference
    @JoinTable(
            name = "cart_item",
            joinColumns = @JoinColumn(name = "cart_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id")
    )
    private List<Item> items;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference(value = "client-cart-ref")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonBackReference(value = "shop-cart-ref")
    private Shop shop;


    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonBackReference(value = "cart-order-ref")
    private Order order;


    public Cart() {
        this.status = CartStatus.DRAFT;
    }

    public Cart(Client client, Shop shop) {
        this.status = CartStatus.DRAFT;
        this.client = client;
        this.shop = shop;

    }

    public Cart(Client client, Shop shop, List<Item> items) {
        this.items = new ArrayList<>();
        this.status = CartStatus.DRAFT;
        this.client = client;
        this.shop = shop;
        this.items.addAll(items);
    }
}
