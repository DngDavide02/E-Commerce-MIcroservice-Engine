package com.dngdavide.inventoryservice.exception;

public class InventoryItemNotFoundException extends RuntimeException {

    public InventoryItemNotFoundException(Long productId) {
        super("Inventory item not found for productId: " + productId);
    }
}
