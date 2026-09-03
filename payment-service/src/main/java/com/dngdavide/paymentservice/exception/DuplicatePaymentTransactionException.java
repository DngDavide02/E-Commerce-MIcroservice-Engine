package com.dngdavide.paymentservice.exception;

public class DuplicatePaymentTransactionException extends RuntimeException {

    public DuplicatePaymentTransactionException(Long orderId) {
        super("Payment transaction already exists for orderId: " + orderId);
    }
}
