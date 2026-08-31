package com.dngdavide.inventoryservice.exception;

public class DuplicateInventoryItemException extends RuntimeException {

    public DuplicateInventoryItemException(Long productId) {
        super("Inventory item already exists for productId: " + productId);
    }
}
