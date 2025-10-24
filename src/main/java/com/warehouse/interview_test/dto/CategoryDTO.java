package com.warehouse.interview_test.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CategoryDTO {
    private UUID id;
    private String categoryName;
    private String categoryType;
    private UUID parentId;
    private String parentName;
    private Integer status;
    private LocalDateTime createdAt;
}

