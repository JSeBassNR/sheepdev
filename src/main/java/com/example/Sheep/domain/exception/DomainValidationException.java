package com.example.Sheep.domain.exception;

public class DomainValidationException extends RuntimeException {
 public DomainValidationException(String message) {
 super(message);
 }
}
