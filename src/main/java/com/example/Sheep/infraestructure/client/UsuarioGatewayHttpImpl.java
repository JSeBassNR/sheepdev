package com.example.Sheep.infraestructure.client;

import com.example.Sheep.domain.model.gateway.UsuarioGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class UsuarioGatewayHttpImpl implements UsuarioGateway {

    private final GanaderoClient ganaderoClient;

    @Override
    public boolean existeUsuario(Long id) {
        if (id == null) return false;
        try {
            return ganaderoClient.existeGanadero(id);
        } catch (Exception e) {
            throw new RuntimeException("Error consultando ganadero-service: " + e.getMessage(), e);
        }
    }
}
