package com.warehouse.interview_test.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
public class VariantDTO {
    private UUID id;
    private UUID itemId;
    private String itemName;
    private String size;
    private String color;
    private String material;
    private String variantName;
    private String sku;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean inStock;
    private Map<String, String> attributes;
    private LocalDateTime createdAt;
}
