package com.dngdavide.inventoryservice.service;

import com.dngdavide.inventoryservice.dto.InventoryItemRequest;
import com.dngdavide.inventoryservice.dto.InventoryItemResponse;
import com.dngdavide.inventoryservice.entity.InventoryItem;
import com.dngdavide.inventoryservice.exception.DuplicateInventoryItemException;
import com.dngdavide.inventoryservice.exception.InsufficientStockException;
import com.dngdavide.inventoryservice.exception.InventoryItemNotFoundException;
import com.dngdavide.inventoryservice.repository.InventoryItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Basic CRUD only — creating and reading stock records.
 * Reserve/release quantity logic (the Saga participant behaviour driven by
 * OrderCreated / ReleaseStock events) is intentionally NOT implemented here;
 * see docs/AI-COLLABORATION-PLAN.md, Phase 2.
 */
@Service
public class InventoryItemService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryItemService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItemResponse> findAll() {
        return inventoryItemRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public InventoryItemResponse findByProductId(Long productId) {
        return toResponse(getByProductIdOrThrow(productId));
    }

    public InventoryItemResponse create(InventoryItemRequest request) {
        if (inventoryItemRepository.findByProductId(request.productId()).isPresent()) {
            throw new DuplicateInventoryItemException(request.productId());
        }
        InventoryItem item = new InventoryItem(request.productId(), request.quantity());
        return toResponse(inventoryItemRepository.save(item));
    }

     public InventoryItemResponse reserve(Long productId, int quantity){
        //1 find item for ID
        InventoryItem item = getByProductIdOrThrow(productId);
        //2 search if enough quantity
        if (item.getQuantity() >= quantity){
        //3 if yes: itemquantity - quantity
        item.setQuantity(item.getQuantity() - quantity);
        return toResponse(inventoryItemRepository.save(item));
        }
        //4 else exeption insufficent stock 
        else {
            throw new InsufficientStockException(productId);
        }
    }

    private InventoryItem getByProductIdOrThrow(Long productId) {
        return inventoryItemRepository.findByProductId(productId)
                .orElseThrow(() -> new InventoryItemNotFoundException(productId));
    }

    private InventoryItemResponse toResponse(InventoryItem item) {
        return new InventoryItemResponse(item.getId(), item.getProductId(), item.getQuantity());
    }

}
