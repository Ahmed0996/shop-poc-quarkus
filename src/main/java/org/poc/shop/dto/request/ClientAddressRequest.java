package org.poc.shop.dto.request;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApplicationScoped
@AllArgsConstructor
@Builder
public class ClientAddressRequest {

    private String name;
    private String shortDescription;
    private double latitude;
    private double longitude;

}
