package org.poc.shop.dto.request;


import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class ClientRequest {

    private String name ;
    private String email ;
    private String phone ;
    List<ClientAddressRequest> addresses;


}
