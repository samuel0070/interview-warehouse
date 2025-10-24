package com.warehouse.interview_test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class RequestCreateVariant {
    private String size;
    private String color;
    private String material;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    private Map<String, String> attributes;
}
