package org.poc.shop.dto.response;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class ItemResponse {

    private Long id;
    private String name;
    private String description;
    private BigInteger quantity;
    private BigDecimal price;
}
