package com.warehouse.interview_test.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.interview_test.entity.ItemEntity;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, UUID>{

    List<ItemEntity> findByName(String name);
    List<ItemEntity> findByCategoryId(UUID categoryId);
    List<ItemEntity> findByCategoryName(String categoryName);
    List<ItemEntity> findByNameContainingIgnoreCase(String name);

    List<ItemEntity> findByNameAndBrandName(String name, String brandName);

    Page<ItemEntity> findAll(Pageable pageable);
    
}
