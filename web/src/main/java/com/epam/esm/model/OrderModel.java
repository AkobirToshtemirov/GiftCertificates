package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
@JsonRootName(value = "order")
@Relation(collectionRelation = "orders")
public class OrderModel extends RepresentationModel<OrderModel> {
    private Long id;
    private BigDecimal price;
    private LocalDateTime orderedTime;
    private Long giftCertificateId;
    private Long userId;
}
