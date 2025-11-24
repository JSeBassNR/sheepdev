package com.example.Sheep.infraestructure.drive_adapters.jpa_repository;

import com.example.Sheep.domain.model.gateway.UsuarioGateway;
import org.springframework.stereotype.Component;

/**
 * Implementación simple (stub) de UsuarioGateway.
 *
 * Esta clase provee un bean para permitir iniciar la aplicación.
 * Actualmente considera "existente" a cualquier id distinto de null y mayor que 0.
 * Reemplazar por un adaptador real (REST/DB) cuando esté disponible.
 */
@Component
public class UsuarioGatewayImpl implements UsuarioGateway {

    @Override
    public boolean existeUsuario(Long id) {
        return id != null && id > 0;
    }
}
