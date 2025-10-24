package com.warehouse.interview_test.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.interview_test.entity.VariantEntity;

@Repository
public interface VariantRepository extends JpaRepository<VariantEntity, UUID>{
    
    Optional<VariantEntity> findBySku(String sku);
    List<VariantEntity> findByItemId(UUID itemId);
    List<VariantEntity> findByStockQuantityGreaterThan(Integer quantity);
    List<VariantEntity> findByStockQuantity(Integer stockQuantity);
    
    @Query("SELECT v FROM VariantEntity  v WHERE v.item.id = :itemId " +
           "AND (:size IS NULL OR v.size = :size) " +
           "AND (:color IS NULL OR v.color = :color) " +
           "AND (:material IS NULL OR v.material = :material)")
    List<VariantEntity> findByItemIdAndAttributes(@Param("itemId") UUID itemId,
                                           @Param("size") String size,
                                           @Param("color") String color,
                                           @Param("material") String material);
    
    @Query("SELECT v FROM VariantEntity  v WHERE v.stockQuantity <= :threshold AND v.stockQuantity > 0")
    List<VariantEntity> findLowStockVariants(@Param("threshold") Integer threshold);
    
    List<VariantEntity> findByStockQuantityLessThanEqual(Integer threshold);

    
}
