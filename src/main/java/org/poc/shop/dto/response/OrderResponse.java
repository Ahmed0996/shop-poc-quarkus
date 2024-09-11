package org.poc.shop.dto.response;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.poc.shop.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApplicationScoped
public class OrderResponse {

    private UUID id;
    private OrderStatus status;
    private Date createdAt;
    private BigDecimal distance;

}
