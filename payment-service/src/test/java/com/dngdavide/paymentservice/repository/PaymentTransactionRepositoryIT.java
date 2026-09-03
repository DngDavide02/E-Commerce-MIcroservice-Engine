package com.dngdavide.paymentservice.repository;

import com.dngdavide.paymentservice.entity.PaymentStatus;
import com.dngdavide.paymentservice.entity.PaymentTransaction;
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
 * ./mvnw test -pl payment-service -Dtest=PaymentTransactionRepositoryIT
 */
@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentTransactionRepositoryIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private PaymentTransactionRepository paymentTransactionRepository;

    @Test
    void savesAndFindsByOrderId() {
        PaymentTransaction transaction = new PaymentTransaction(42L, BigDecimal.valueOf(150.00), PaymentStatus.PENDING);

        paymentTransactionRepository.save(transaction);

        assertThat(paymentTransactionRepository.findByOrderId(42L)).isPresent();
    }
}
