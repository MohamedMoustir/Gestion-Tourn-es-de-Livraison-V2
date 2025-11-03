package com.livraison.demo.domain.exception;

public class DeliveryNotDeletedException extends RuntimeException {
    public DeliveryNotDeletedException(String message) {
        super(message);
    }
}
