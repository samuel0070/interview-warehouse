package com.warehouse.interview_test.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.interview_test.entity.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {
    Page<CategoryEntity> findByParentIsNull(Pageable pageable);
    Page<CategoryEntity> findByParentId(UUID parentId,Pageable pageable);
    List<CategoryEntity> findByCategoryType(String categoryType);
    Optional<CategoryEntity> findByCategoryName(String categoryName);
    List<CategoryEntity> findByStatus(Integer status);

    Page<CategoryEntity> findAll(Pageable pageable);
}
