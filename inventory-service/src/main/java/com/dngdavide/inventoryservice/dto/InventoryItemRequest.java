package com.dngdavide.inventoryservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryItemRequest(
        @NotNull Long productId,
        @NotNull @PositiveOrZero Integer quantity
) {
}
