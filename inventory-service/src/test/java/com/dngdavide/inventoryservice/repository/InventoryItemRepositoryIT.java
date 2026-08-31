package com.dngdavide.inventoryservice.repository;

import com.dngdavide.inventoryservice.entity.InventoryItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Not picked up by the default surefire "test" phase (only *Test classes are) —
 * requires a running Docker daemon. Run explicitly with:
 * ./mvnw test -pl inventory-service -Dtest=InventoryItemRepositoryIT
 */
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InventoryItemRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Test
    void savesAndFindsByProductId() {
        InventoryItem item = new InventoryItem(7L, 25);

        inventoryItemRepository.save(item);

        assertThat(inventoryItemRepository.findByProductId(7L)).isPresent();
    }
}
