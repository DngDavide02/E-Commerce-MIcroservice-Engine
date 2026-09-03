package com.dngdavide.paymentservice.dto;

import com.dngdavide.paymentservice.entity.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long orderId,
        BigDecimal amount,
        PaymentStatus status
) {
}
