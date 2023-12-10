package com.epam.esm.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class GiftCertificate {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Double duration;
    private LocalDateTime createdDate;
    private LocalDateTime lustUpdatedDate;
    private List<Tag> tags;
}