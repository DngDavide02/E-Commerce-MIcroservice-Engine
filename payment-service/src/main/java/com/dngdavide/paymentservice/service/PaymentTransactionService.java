package com.dngdavide.paymentservice.service;

import com.dngdavide.paymentservice.dto.PaymentRequest;
import com.dngdavide.paymentservice.dto.PaymentResponse;
import com.dngdavide.paymentservice.entity.PaymentStatus;
import com.dngdavide.paymentservice.entity.PaymentTransaction;
import com.dngdavide.paymentservice.exception.DuplicatePaymentTransactionException;
import com.dngdavide.paymentservice.exception.PaymentTransactionNotFoundException;
import com.dngdavide.paymentservice.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Basic CRUD only — creating and reading payment records, always starting
 * as PENDING. Deciding whether a payment is authorized or rejected (the
 * Saga participant behaviour triggered by StockReserved, publishing
 * PaymentCompleted / PaymentFailed) is intentionally NOT implemented here;
 * see docs/AI-COLLABORATION-PLAN.md, Phase 2 — same boundary as
 * InventoryItemService's reserve()/release().
 */
@Service
public class PaymentTransactionService {

    private final PaymentTransactionRepository paymentTransactionRepository;

    public PaymentTransactionService(PaymentTransactionRepository paymentTransactionRepository) {
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    public List<PaymentResponse> findAll() {
        return paymentTransactionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public PaymentResponse findByOrderId(Long orderId) {
        return toResponse(getByOrderIdOrThrow(orderId));
    }

    public PaymentResponse create(PaymentRequest request) {
        if (paymentTransactionRepository.findByOrderId(request.orderId()).isPresent()) {
            throw new DuplicatePaymentTransactionException(request.orderId());
        }
        PaymentTransaction transaction = new PaymentTransaction(request.orderId(), request.amount(), PaymentStatus.PENDING);
        return toResponse(paymentTransactionRepository.save(transaction));
    }

    private PaymentTransaction getByOrderIdOrThrow(Long orderId) {
        return paymentTransactionRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentTransactionNotFoundException(orderId));
    }

    private PaymentResponse toResponse(PaymentTransaction transaction) {
        return new PaymentResponse(transaction.getId(), transaction.getOrderId(),
                transaction.getAmount(), transaction.getStatus());
    }
}
