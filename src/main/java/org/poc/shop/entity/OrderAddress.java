package org.poc.shop.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class OrderAddress extends Address {


    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference(value = "order-address-ref")
    private List<Order> order;

    public OrderAddress(Long id, Double latitude, Double longitude) {
        setId(id);
        setLatitude(latitude);
        setLongitude(longitude);
    }


    public OrderAddress(ClientAddress clientAddress) {

        setLatitude(clientAddress.getLatitude());
        setLongitude(clientAddress.getLongitude());
        setName(clientAddress.getName());
        setShortDescription(clientAddress.getShortDescription());


    }
}