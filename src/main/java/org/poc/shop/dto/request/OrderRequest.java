package org.poc.shop.dto.request;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApplicationScoped
public class OrderRequest {

    private UUID cartId;
}
