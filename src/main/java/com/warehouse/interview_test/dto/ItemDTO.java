package com.warehouse.interview_test.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ItemDTO {
    private UUID id;
    private String brandName;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;
    private BigDecimal basePrice;
    private List<VariantDTO> variants = new ArrayList<>();
    private LocalDateTime createdAt;
}
