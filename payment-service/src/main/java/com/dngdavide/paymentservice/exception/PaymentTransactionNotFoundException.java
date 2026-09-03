package com.dngdavide.paymentservice.exception;

public class PaymentTransactionNotFoundException extends RuntimeException {

    public PaymentTransactionNotFoundException(Long orderId) {
        super("Payment transaction not found for orderId: " + orderId);
    }
}
