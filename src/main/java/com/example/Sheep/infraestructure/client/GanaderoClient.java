package com.example.Sheep.infraestructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GanaderoClient {

    private final RestTemplate restTemplate;

    @Value("${ganadero.service.base-url:http://localhost:9192}")
    private String baseUrl;

    public GanaderoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean existeGanadero(Long id){
        try{
            String url = String.format("%s/api/MachIA/usuario/%d", baseUrl, id);
            restTemplate.getForEntity(url, Object.class);
            return true;
        } catch (HttpClientErrorException.NotFound e){
            return false;
        } catch (Exception e){
            throw new RuntimeException("Error consultando ganadero-service: " + e.getMessage(), e);
        }
    }
}
