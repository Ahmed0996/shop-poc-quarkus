package org.poc.shop.dto.response;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class ClientResponse {
    private Long id ;
    private String name ;
    private String email ;
    private String phone ;
}
