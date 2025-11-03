package com.livraison.demo.domain.exception;

public class WarehouseNotDeletedException extends RuntimeException {
    public WarehouseNotDeletedException(String message) {
        super(message);
    }

}