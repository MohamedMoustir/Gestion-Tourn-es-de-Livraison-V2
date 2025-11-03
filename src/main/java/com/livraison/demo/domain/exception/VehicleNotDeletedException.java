package com.livraison.demo.domain.exception;

public class VehicleNotDeletedException extends RuntimeException {
    public VehicleNotDeletedException(String message) {
        super(message);
    }

}
