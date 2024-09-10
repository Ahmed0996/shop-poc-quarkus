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
public class CartRequest {

    private long clientId;
    private long shopId;
    private List<ItemRequest> itemList;
}
