package com.warehouse.interview_test.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.interview_test.dto.response.ApiResponse;
import com.warehouse.interview_test.entity.CategoryEntity;
import com.warehouse.interview_test.service.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryEntity> createCategory(
            @RequestParam String categoryName,
            @RequestParam String categoryType,
            @RequestParam(required = false) UUID parentId,
            @RequestParam(defaultValue = "1") Integer status) {
        CategoryEntity category = categoryService.createCategory(categoryName, categoryType, parentId, status);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CategoryEntity>>> getAllCategories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "categoryName,asc") String sort) {

        Page<CategoryEntity> categories = categoryService.getAllCategories(page, size, sort);

        ApiResponse<Page<CategoryEntity>> response = ApiResponse.success(
            "Category list retrieved successfully",
            categories
        );

        return ResponseEntity.ok(response);
   
    }

    @GetMapping("/c1")
    public ResponseEntity<ApiResponse<Page<CategoryEntity>>> getRootCategories(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "categoryName,asc") String sort
    ) {
      
        Page<CategoryEntity> categories = categoryService.getRootCategories(page, size, sort);

        ApiResponse<Page<CategoryEntity>> response = ApiResponse.success(
            "Category list retrieved successfully",
            categories
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/c2/{id}")
    public ResponseEntity<ApiResponse<Page<CategoryEntity>>> getChildCategories(@PathVariable UUID id,
    @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "categoryName,asc") String sort) {

        Page<CategoryEntity> categories = categoryService.getChildCategories(id, page, size, sort);

        ApiResponse<Page<CategoryEntity>> response = ApiResponse.success(
            "Category list retrieved successfully",
            categories
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryEntity> getCategoryById(@PathVariable UUID id) {
        CategoryEntity category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryEntity> updateCategory(
            @PathVariable UUID id,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) String categoryType,
            @RequestParam(required = false) Integer status) {
        CategoryEntity category = categoryService.updateCategory(id, categoryName, categoryType, status);
        return ResponseEntity.ok(category);
    }

   
}