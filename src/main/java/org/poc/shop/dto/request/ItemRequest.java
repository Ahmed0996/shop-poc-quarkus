package org.poc.shop.dto.request;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

import java.math.BigInteger;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class ItemRequest {

    private Long itemId ;
    private BigInteger quantity ;

}
