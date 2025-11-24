package com.example.Sheep.aplication.config;

import com.example.Sheep.domain.model.gateway.OvejaGateway;
import com.example.Sheep.domain.model.gateway.UsuarioGateway;
import com.example.Sheep.domain.usecase.OvejaUseCase;
import com.example.Sheep.infraestructure.client.GanaderoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public OvejaUseCase ovejaUseCase(OvejaGateway ovejaGateway, UsuarioGateway usuarioGateway, GanaderoClient ganaderoClient) {
        return new OvejaUseCase(ovejaGateway, usuarioGateway, ganaderoClient);
    }
}

