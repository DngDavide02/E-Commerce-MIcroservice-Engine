package com.dngdavide.paymentservice.service;

import com.dngdavide.paymentservice.dto.PaymentRequest;
import com.dngdavide.paymentservice.dto.PaymentResponse;
import com.dngdavide.paymentservice.entity.PaymentStatus;
import com.dngdavide.paymentservice.entity.PaymentTransaction;
import com.dngdavide.paymentservice.exception.DuplicatePaymentTransactionException;
import com.dngdavide.paymentservice.exception.PaymentTransactionNotFoundException;
import com.dngdavide.paymentservice.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentTransactionServiceTest {

    @Mock
    private PaymentTransactionRepository paymentTransactionRepository;

    @InjectMocks
    private PaymentTransactionService paymentTransactionService;

    @Test
    void createSavesNewPendingTransaction() {
        PaymentRequest request = new PaymentRequest(1L, BigDecimal.valueOf(99.90));
        when(paymentTransactionRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(paymentTransactionRepository.save(any(PaymentTransaction.class)))
                .thenReturn(new PaymentTransaction(1L, BigDecimal.valueOf(99.90), PaymentStatus.PENDING));

        PaymentResponse response = paymentTransactionService.create(request);

        assertThat(response.orderId()).isEqualTo(1L);
        assertThat(response.status()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void createThrowsWhenOrderAlreadyHasTransaction() {
        PaymentRequest request = new PaymentRequest(1L, BigDecimal.valueOf(99.90));
        when(paymentTransactionRepository.findByOrderId(1L))
                .thenReturn(Optional.of(new PaymentTransaction(1L, BigDecimal.valueOf(99.90), PaymentStatus.PENDING)));

        assertThatThrownBy(() -> paymentTransactionService.create(request))
                .isInstanceOf(DuplicatePaymentTransactionException.class);
    }

    @Test
    void findByOrderIdThrowsWhenMissing() {
        when(paymentTransactionRepository.findByOrderId(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentTransactionService.findByOrderId(99L))
                .isInstanceOf(PaymentTransactionNotFoundException.class);
    }
}
