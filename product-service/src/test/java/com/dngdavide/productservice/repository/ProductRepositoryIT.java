package com.dngdavide.productservice.repository;

import com.dngdavide.productservice.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Not picked up by the default surefire "test" phase (only *Test classes are) —
 * requires a running Docker daemon. Run explicitly with:
 * ./mvnw test -pl product-service -Dtest=ProductRepositoryIT
 */
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private ProductRepository productRepository;

    @Test
    void savesAndFindsProduct() {
        Product product = new Product("Monitor", "27-inch 4K monitor", BigDecimal.valueOf(399.00), "Electronics");

        Product saved = productRepository.save(product);

        assertThat(productRepository.findById(saved.getId())).isPresent();
    }
}
