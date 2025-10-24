package com.warehouse.interview_test.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.interview_test.dto.response.ApiResponse;
import com.warehouse.interview_test.dto.response.PageResponse;
import com.warehouse.interview_test.entity.CategoryEntity;
import com.warehouse.interview_test.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryEntity createCategory(String categoryName, String categoryType, UUID parentId, Integer status) {
        CategoryEntity parent = null;
        if (parentId != null) {
            parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
        }

        CategoryEntity category = new CategoryEntity();
        category.setCategoryName(categoryName);
        category.setCategoryType(categoryType);
        category.setParent(parent);
        category.setStatus(status != null ? status : 1);

        return categoryRepository.save(category);
    }

  public Page<CategoryEntity> getAllCategories(int page, int size, String sort) {
    String[] sortParams = sort.split(",");
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
    );
    return categoryRepository.findAll(pageable);
}

    public Page<CategoryEntity> getRootCategories(int page, int size, String sort) {
    String[] sortParams = sort.split(",");
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
    );
        return categoryRepository.findByParentIsNull(pageable);
    }

    public Page<CategoryEntity> getChildCategories(UUID parentId,int page, int size, String sort) {
        
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
    );
        return categoryRepository.findByParentId(parentId, pageable);
    }

    public CategoryEntity getCategoryById(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    public CategoryEntity updateCategory(UUID id, String categoryName, String categoryType, Integer status) {
        CategoryEntity category = getCategoryById(id);
        if (categoryName != null) category.setCategoryName(categoryName);
        if (categoryType != null) category.setCategoryType(categoryType);
        if (status != null) category.setStatus(status);
        return categoryRepository.save(category);
    }

    public void deleteCategory(UUID id) {
        CategoryEntity category = getCategoryById(id);
        // Check if category has items
        if (!category.getItems().isEmpty()) {
            throw new RuntimeException("Cannot delete category with existing items");
        }
        categoryRepository.delete(category);
    }
}
