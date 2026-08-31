package com.dngdavide.inventoryservice.service;

import com.dngdavide.inventoryservice.dto.InventoryItemRequest;
import com.dngdavide.inventoryservice.dto.InventoryItemResponse;
import com.dngdavide.inventoryservice.entity.InventoryItem;
import com.dngdavide.inventoryservice.exception.DuplicateInventoryItemException;
import com.dngdavide.inventoryservice.exception.InventoryItemNotFoundException;
import com.dngdavide.inventoryservice.repository.InventoryItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryItemServiceTest {

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    @InjectMocks
    private InventoryItemService inventoryItemService;

    @Test
    void createSavesNewInventoryItem() {
        InventoryItemRequest request = new InventoryItemRequest(10L, 100);
        when(inventoryItemRepository.findByProductId(10L)).thenReturn(Optional.empty());
        when(inventoryItemRepository.save(any(InventoryItem.class)))
                .thenReturn(new InventoryItem(10L, 100));

        InventoryItemResponse response = inventoryItemService.create(request);

        assertThat(response.productId()).isEqualTo(10L);
        assertThat(response.quantity()).isEqualTo(100);
    }

    @Test
    void createThrowsWhenProductAlreadyHasInventory() {
        InventoryItemRequest request = new InventoryItemRequest(10L, 100);
        when(inventoryItemRepository.findByProductId(10L))
                .thenReturn(Optional.of(new InventoryItem(10L, 50)));

        assertThatThrownBy(() -> inventoryItemService.create(request))
                .isInstanceOf(DuplicateInventoryItemException.class);
    }

    @Test
    void findByProductIdThrowsWhenMissing() {
        when(inventoryItemRepository.findByProductId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> inventoryItemService.findByProductId(99L))
                .isInstanceOf(InventoryItemNotFoundException.class);
    }
}
