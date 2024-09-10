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
public class OrderUpdateRequest {
    private String name;
    private String shortDescription;
    private double latitude;
    private double longitude;

}
