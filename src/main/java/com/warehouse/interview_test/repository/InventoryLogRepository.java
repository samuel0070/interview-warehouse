package com.warehouse.interview_test.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.interview_test.entity.InventoryLogEntity;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLogEntity,UUID>{

        List<InventoryLogEntity> findByVariantIdOrderByCreatedAtDesc(UUID variantId);
        Page<InventoryLogEntity> findByVariantId(UUID variantId,Pageable pageable);
    
}
