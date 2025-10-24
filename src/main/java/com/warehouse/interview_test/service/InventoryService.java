package com.warehouse.interview_test.service;

import com.warehouse.interview_test.dto.RequestCreateItem;
import com.warehouse.interview_test.dto.RequestCreateVariant;
import com.warehouse.interview_test.dto.RequestStockUpdate;
import com.warehouse.interview_test.entity.*;
import com.warehouse.interview_test.exception.InsufficientStockException;
import com.warehouse.interview_test.repository.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import javax.naming.InsufficientResourcesException;

@Service
@Transactional
public class InventoryService {
    
    private final ItemRepository itemRepository;
    private final VariantRepository variantRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryLogRepository inventoryLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public InventoryService(ItemRepository itemRepository, 
                          VariantRepository variantRepository,
                          CategoryRepository categoryRepository,
                          InventoryLogRepository inventoryLogRepository) {
        this.itemRepository = itemRepository;
        this.variantRepository = variantRepository;
        this.categoryRepository = categoryRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }

    public ItemEntity createItem(RequestCreateItem request) {
    // Check if item already exists
    List<ItemEntity> existingItems = itemRepository.findByNameAndBrandName(request.getName(),request.getBrandName());
    
    ItemEntity item;
    if (!existingItems.isEmpty()) {
        item = existingItems.get(0);
        logger.info("Item '{}' already exists, adding variants to existing item", request.getName());
    } else {
        CategoryEntity category = categoryRepository.findById(UUID.fromString(request.getCategoryId()))
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + request.getCategoryId()));

        item = new ItemEntity();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(category);
        item.setBasePrice(request.getBasePrice());
        item.setBrandName(request.getBrandName());
        
        item = itemRepository.save(item);
        logger.info("Created new item '{}'", request.getName());
    }

    // Create variants if provided
    if (request.getVariants() != null && !request.getVariants().isEmpty()) {
        int createdCount = 0;
        int skippedCount = 0;
        
        for (RequestCreateVariant variantRequest : request.getVariants()) {
            // Check if similar variant already exists for this item
            boolean variantExists = checkIfVariantExists(item.getId(), variantRequest);
            
            if (variantExists) {
                logger.info("Variant already exists for item '{}' with size: {}, color: {}, material: {}", 
                           request.getName(), variantRequest.getSize(), variantRequest.getColor(), variantRequest.getMaterial());
                skippedCount++;
                continue;
            }
            
            try {
                createVariant(item.getId(), variantRequest);
                createdCount++;
            } catch (RuntimeException e) {
                logger.warn("Failed to create variant: {}", e.getMessage());
            }
        }
        
        logger.info("Created {} variants, skipped {} existing variants for item '{}'", 
                   createdCount, skippedCount, request.getName());
    }

    return item;
}

private boolean checkIfVariantExists(UUID itemId, RequestCreateVariant variantRequest) {
    List<VariantEntity> existingVariants = variantRepository.findByItemIdAndAttributes(
        itemId, 
        variantRequest.getSize(), 
        variantRequest.getColor(), 
        variantRequest.getMaterial()
    );
    return !existingVariants.isEmpty();
}

    public VariantEntity createVariant(UUID itemId, RequestCreateVariant request) {
        ItemEntity item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + itemId));

        // Check for duplicate SKU
        if (variantRepository.findBySku(request.getSku()).isPresent()) {
            throw new RuntimeException("Variant with SKU '" + request.getSku() + "' already exists");
        }

        VariantEntity variant = new VariantEntity();
        variant.setItem(item);
        variant.setSize(request.getSize());
        variant.setColor(request.getColor());
        variant.setMaterial(request.getMaterial());
        variant.setSku(request.getSku());
        variant.setPrice(request.getPrice());
        variant.setStockQuantity(request.getStockQuantity());
        
        if (request.getAttributes() != null) {
            variant.setAttributes(request.getAttributes());
        }

        variant.generateVariantName();
         //To Handle SKU - generate if null or empty
        if (request.getSku() == null || request.getSku().trim().isEmpty()) {
            variant.generateSku();
            
            // Check if generated SKU already exists and regenerate if needed
            int attempt = 0;
            while (variantRepository.findBySku(variant.getSku()).isPresent() && attempt < 5) {
                variant.generateSku();
                attempt++;
            }
            
            if (attempt >= 5) {
                throw new RuntimeException("Unable to generate unique SKU after multiple attempts");
            }
        } else {
            variant.setSku(request.getSku());
        }

        return variantRepository.save(variant);
    }

    public VariantEntity updateStock(UUID variantId, RequestStockUpdate request) {
        VariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + variantId));

        int previousQuantity = variant.getStockQuantity();
        int newQuantity = previousQuantity + request.getQuantityChange();
        
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock. Available: " + previousQuantity + 
                                     ", Requested reduction: " + Math.abs(request.getQuantityChange()));
        }

        variant.setStockQuantity(newQuantity);
        VariantEntity updatedVariant = variantRepository.save(variant);

        // Log inventory change
        logInventoryChange(variant, "ADJUSTMENT", request.getQuantityChange(), 
                          previousQuantity, newQuantity, request.getReason());

        return updatedVariant;
    }

    public VariantEntity sellStock(UUID variantId, Integer quantity) {
        VariantEntity variant = variantRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + variantId));

        if (!variant.hasSufficientStock(quantity)) {
            throw new InsufficientStockException("Insufficient stock. Available: " + variant.getStockQuantity() + 
                                     ", Requested: " + quantity);
        }

        int previousQuantity = variant.getStockQuantity();
        int newQuantity = previousQuantity - quantity;
        variant.setStockQuantity(newQuantity);
        VariantEntity updatedVariant = variantRepository.save(variant);

        // Log the sale
        logInventoryChange(variant, "STOCK_OUT", -quantity, previousQuantity, 
                          newQuantity, "Sale of " + quantity + " units");

        return updatedVariant;
    }

    private void logInventoryChange(VariantEntity variant, String changeType, 
                                  int quantityChange, int previousQuantity, 
                                  int newQuantity, String reason) {
        InventoryLogEntity log = new InventoryLogEntity();
        log.setVariant(variant);
        log.setChangeType(changeType);
        log.setQuantityChange(quantityChange);
        log.setPreviousQuantity(previousQuantity);
        log.setNewQuantity(newQuantity);
        log.setReason(reason);
        
        inventoryLogRepository.save(log);
    }

    // Query methods
    public Page<ItemEntity> getAllItems(int page, int size, String sort) {
    String[] sortParams = sort.split(",");
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
    );
    return itemRepository.findAll(pageable);
    }

    public ItemEntity getItemById(UUID id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found with id: " + id));
    }

    public VariantEntity getVariantById(UUID id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + id));
    }

    public List<VariantEntity> getVariantsByItem(UUID itemId) {
        return variantRepository.findByItemId(itemId);
    }

    public List<VariantEntity> getLowStockVariants(Integer threshold) {
        return variantRepository.findLowStockVariants(threshold);
    }

    public Page<InventoryLogEntity> getVariantInventoryHistory(UUID variantId,int page, int size, String sort) {
    String[] sortParams = sort.split(",");
    Pageable pageable = PageRequest.of(
        page,
        size,
        Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0])
    );
    return inventoryLogRepository.findByVariantId(variantId,pageable);
    }

    public List<VariantEntity> getVariantsByAttributes(UUID itemId, String size, String color, String material) {
        return variantRepository.findByItemIdAndAttributes(itemId, size, color, material);
    }
}
