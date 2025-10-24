package com.warehouse.interview_test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Data
public class RequestCreateItem {
    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Category ID is required")
    private String categoryId;

    @Schema(description = "Base Price", example = "100000", required = true)
    @NotNull(message = "Base price is required")
    private BigDecimal basePrice;

    @Schema(description = "Brand name", example = "Apple", required = true)
    @NotBlank(message = "Brand name is required") 
    private String brandName; 

    private List<RequestCreateVariant> variants = new ArrayList<>();
}