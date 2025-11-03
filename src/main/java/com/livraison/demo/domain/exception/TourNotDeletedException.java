package com.livraison.demo.domain.exception;

public class TourNotDeletedException extends RuntimeException {
    public TourNotDeletedException(String message) {
        super(message);
    }
}
