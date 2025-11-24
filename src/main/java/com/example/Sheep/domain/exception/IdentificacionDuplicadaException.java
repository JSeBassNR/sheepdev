package com.example.Sheep.domain.exception;

public class IdentificacionDuplicadaException extends RuntimeException {
 public IdentificacionDuplicadaException(String identificacion) {
 super("La identificacion ya existe: " + identificacion);
 }
}
