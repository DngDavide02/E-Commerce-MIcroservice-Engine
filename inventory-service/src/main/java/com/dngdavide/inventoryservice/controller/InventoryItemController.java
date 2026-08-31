package com.dngdavide.inventoryservice.controller;

import com.dngdavide.inventoryservice.dto.InventoryItemRequest;
import com.dngdavide.inventoryservice.dto.InventoryItemResponse;
import com.dngdavide.inventoryservice.service.InventoryItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Basic CRUD only. No reserve/release endpoints here on purpose —
 * see InventoryItemService's class-level note.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryItemController {

    private final InventoryItemService inventoryItemService;

    public InventoryItemController(InventoryItemService inventoryItemService) {
        this.inventoryItemService = inventoryItemService;
    }

    @GetMapping
    public List<InventoryItemResponse> findAll() {
        return inventoryItemService.findAll();
    }

    @GetMapping("/{productId}")
    public InventoryItemResponse findByProductId(@PathVariable Long productId) {
        return inventoryItemService.findByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse create(@Valid @RequestBody InventoryItemRequest request) {
        return inventoryItemService.create(request);
    }
}
