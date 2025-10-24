package com.warehouse.interview_test.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestStockUpdate {
    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;
    
    private String reason;
}