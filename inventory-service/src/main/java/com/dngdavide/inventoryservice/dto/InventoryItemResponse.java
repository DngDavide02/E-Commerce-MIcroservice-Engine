package com.dngdavide.inventoryservice.dto;

public record InventoryItemResponse(
        Long id,
        Long productId,
        Integer quantity
) {
}
