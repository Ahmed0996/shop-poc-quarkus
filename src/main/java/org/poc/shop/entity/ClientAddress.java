package org.poc.shop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class ClientAddress extends Address {


    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference(value = "client-client-address-ref")
    private Client client;


}
