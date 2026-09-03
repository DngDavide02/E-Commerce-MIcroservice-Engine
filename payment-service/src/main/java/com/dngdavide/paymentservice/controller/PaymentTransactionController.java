package com.dngdavide.paymentservice.controller;

import com.dngdavide.paymentservice.dto.PaymentRequest;
import com.dngdavide.paymentservice.dto.PaymentResponse;
import com.dngdavide.paymentservice.service.PaymentTransactionService;
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
 * Basic CRUD only. No authorize/reject endpoints here on purpose —
 * see PaymentTransactionService's class-level note.
 */
@RestController
@RequestMapping("/api/payments")
public class PaymentTransactionController {

    private final PaymentTransactionService paymentTransactionService;

    public PaymentTransactionController(PaymentTransactionService paymentTransactionService) {
        this.paymentTransactionService = paymentTransactionService;
    }

    @GetMapping
    public List<PaymentResponse> findAll() {
        return paymentTransactionService.findAll();
    }

    @GetMapping("/{orderId}")
    public PaymentResponse findByOrderId(@PathVariable Long orderId) {
        return paymentTransactionService.findByOrderId(orderId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody PaymentRequest request) {
        return paymentTransactionService.create(request);
    }
}
