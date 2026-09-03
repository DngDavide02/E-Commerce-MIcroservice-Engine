package com.dngdavide.paymentservice.exception;

import java.time.Instant;

public record ErrorResponse(int status, String message, Instant timestamp) {
}
