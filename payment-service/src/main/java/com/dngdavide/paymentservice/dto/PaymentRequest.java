package com.dngdavide.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @NotNull Long orderId,
        @NotNull @Positive BigDecimal amount
) {
}
