package org.poc.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Shop extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToMany(mappedBy = "shop", orphanRemoval = false , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JsonManagedReference(value = "shop-order-ref")
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "shop_address_id")
   @JsonBackReference(value = "shop-address-ref")
    private ShopAddress address;


    @OneToMany(mappedBy = "shop", orphanRemoval = false , cascade = CascadeType.ALL , fetch = FetchType.EAGER)
    @JsonManagedReference(value="shop-cart-ref")
    private List<Cart> carts;
}
