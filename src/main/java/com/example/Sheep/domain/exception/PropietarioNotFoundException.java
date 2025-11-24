package com.example.Sheep.domain.exception;

public class PropietarioNotFoundException extends RuntimeException {
 public PropietarioNotFoundException(Long propietarioId) {
 super("Propietario no encontrado: " + propietarioId);
 }
}
