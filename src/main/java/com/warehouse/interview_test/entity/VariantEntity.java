package com.warehouse.interview_test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Entity
@Table(name = "variants")
@Getter
@Setter
@AllArgsConstructor
public class VariantEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnore 
    private ItemEntity item;

    // Variant attributes
    private String size;
    private String color;
    private String material;

    @Column(name = "variant_name", nullable = false)
    private String variantName;

    @Column(unique = true, nullable = false)
    private String sku;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, String> attributes = new HashMap<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public VariantEntity() {}

    public VariantEntity(ItemEntity item, String size, String color, String material, String sku, BigDecimal price, Integer stockQuantity) {
        this.item = item;
        this.size = size;
        this.color = color;
        this.material = material;
        this.sku = sku;
        this.price = price;
        this.stockQuantity = stockQuantity;
        generateVariantName();
    }


    // Business logic methods
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public boolean hasSufficientStock(Integer quantity) {
        return stockQuantity >= quantity;
    }


    public void generateVariantName() {
        List<String> parts = new ArrayList<>();
        if (color != null) parts.add(color);
        if (material != null) parts.add(material);
        if (item != null) parts.add(item.getName());
        if (size != null) parts.add(size);
        
        this.variantName = String.join(" ", parts);
    }

    public void generateSku() {
        List<String> parts = new ArrayList<>();
        if (item != null) {
            String itemCode = item.getBrandName().replaceAll("\\s+", "").toUpperCase();
            parts.add(itemCode.substring(0, Math.min(itemCode.length(), 5)));
        }
        if (color != null) parts.add(color.toUpperCase().substring(0, Math.min(color.length(), 3)));
        if (material != null) parts.add(material.toUpperCase().substring(0, Math.min(material.length(), 3)));
        if (size != null) parts.add(size.toUpperCase());
        
        // Add year and month (YYMM format)
        String yearMonth = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMDD"));
        
        // Add sequential number (you might want to get this from database)
        String sequential = String.format("%03d", new Random().nextInt(1000));
        
        this.sku = String.join("-", parts) + "-" + yearMonth;
    }
}

