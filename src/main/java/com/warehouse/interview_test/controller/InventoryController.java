package com.warehouse.interview_test.controller;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.interview_test.dto.RequestCreateItem;
import com.warehouse.interview_test.dto.RequestCreateVariant;
import com.warehouse.interview_test.dto.RequestStockUpdate;
import com.warehouse.interview_test.dto.response.ApiResponse;
import com.warehouse.interview_test.entity.CategoryEntity;
import com.warehouse.interview_test.entity.InventoryLogEntity;
import com.warehouse.interview_test.entity.ItemEntity;
import com.warehouse.interview_test.entity.VariantEntity;
import com.warehouse.interview_test.service.InventoryService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Item endpoints
    @PostMapping("/items")
    public ResponseEntity<ItemEntity> createItem(@Valid @RequestBody RequestCreateItem request) {
        ItemEntity item = inventoryService.createItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    @GetMapping("/items")
    public ResponseEntity<ApiResponse<Page<ItemEntity>>> getAllItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name,asc") String sort
    ) {
        Page<ItemEntity> items = inventoryService.getAllItems(page, size, sort);

        ApiResponse<Page<ItemEntity>> response = ApiResponse.success(
            "Items list retrieved successfully",
            items
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemEntity> getItemById(@PathVariable UUID id) {
        ItemEntity item = inventoryService.getItemById(id);
        return ResponseEntity.ok(item);
    }

    @GetMapping("/items/{itemId}/variants")
    public ResponseEntity<List<VariantEntity>> getItemVariants(@PathVariable UUID itemId) {
        List<VariantEntity> variants = inventoryService.getVariantsByItem(itemId);
        return ResponseEntity.ok(variants);
    }

    // Variant endpoints
    @PostMapping("/items/{itemId}/variants")
    public ResponseEntity<VariantEntity> createVariant(@PathVariable UUID itemId, 
                                                @Valid @RequestBody RequestCreateVariant request) {
        VariantEntity variant = inventoryService.createVariant(itemId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(variant);
    }

    @GetMapping("/variants/{variantId}")
    public ResponseEntity<VariantEntity> getVariantById(@PathVariable UUID variantId) {
        VariantEntity variant = inventoryService.getVariantById(variantId);
        return ResponseEntity.ok(variant);
    }

    // Stock management
    @PostMapping("/variants-update/stock/{variantId}")
    public ResponseEntity<VariantEntity> updateStock(@PathVariable UUID variantId,
                                              @Valid @RequestBody RequestStockUpdate request) {
        VariantEntity variant = inventoryService.updateStock(variantId, request);
        return ResponseEntity.ok(variant);
    }

    @PostMapping("/variants/{variantId}/sell")
    public ResponseEntity<VariantEntity> sellStock(@PathVariable UUID variantId,
                                            @RequestParam Integer quantity) {
        VariantEntity variant = inventoryService.sellStock(variantId, quantity);
        return ResponseEntity.ok(variant);
    }

    // Query endpoints
    @GetMapping("/variants/low-stock")
    public ResponseEntity<List<VariantEntity>> getLowStockVariants(@RequestParam(defaultValue = "10") Integer threshold) {
        List<VariantEntity> variants = inventoryService.getLowStockVariants(threshold);
        return ResponseEntity.ok(variants);
    }

    @GetMapping("/variants-history/{variantId}")
    public ResponseEntity<ApiResponse<Page<InventoryLogEntity>>> getInventoryHistory(
        @PathVariable UUID variantId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt,desc") String sort) {

        Page<InventoryLogEntity> history = inventoryService.getVariantInventoryHistory(variantId,page, size, sort);

        ApiResponse<Page<InventoryLogEntity>> response = ApiResponse.success(
            "Inventory History list retrieved successfully",
            history
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/items/variants-filter/{itemId}")
    public ResponseEntity<List<VariantEntity>> getVariantsByAttributes(
            @PathVariable UUID itemId,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String material) {
        List<VariantEntity> variants = inventoryService.getVariantsByAttributes(itemId, size, color, material);
        return ResponseEntity.ok(variants);
    }
}
