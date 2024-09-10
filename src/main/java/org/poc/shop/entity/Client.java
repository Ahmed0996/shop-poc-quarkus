package org.poc.shop.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;


@Setter
@Getter
@Entity
@AllArgsConstructor
@Builder
@ToString
public class Client extends PanacheEntityBase {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Email
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone number")
    private String phone;

    private boolean isActive;

    private boolean isDeleted;


    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "client-client-address-ref")
    private List<ClientAddress> addresses;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonManagedReference(value = "client-cart-ref")
    private List<Cart> carts;


    public Client() {
        this.isActive = true;
        this.isDeleted = false;
    }


}
