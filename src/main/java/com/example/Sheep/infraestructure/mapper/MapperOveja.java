package com.example.Sheep.infraestructure.mapper;

import com.example.Sheep.domain.model.Oveja;
import com.example.Sheep.infraestructure.drive_adapters.jpa_repository.OvejaData;
import com.example.Sheep.infraestructure.entry_points.dto.OvejaRequestDTO;
import com.example.Sheep.infraestructure.entry_points.dto.OvejaResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MapperOveja {

        public Oveja toOveja(OvejaData ovejaData) {
                return new Oveja(
                                ovejaData.getId(),
                                ovejaData.getProductorId(),
                                ovejaData.getIdentificacion(),
                                ovejaData.getEdad(),
                                ovejaData.getSexo(),
                                ovejaData.getEstadoSalud(),
                                ovejaData.getFechaRegistro(),
                                ovejaData.getResultadoML(),
                                ovejaData.getImagenPath()
                );
        }

    public OvejaResponseDTO toResponse(Oveja oveja) {
        if (oveja == null) return null;
        return new OvejaResponseDTO(
                oveja.getId(),
                oveja.getIdentificacion(),
                oveja.getEdad(),
                oveja.getSexo(),
                oveja.getEstadoSalud(),
                oveja.getFechaRegistro() == null ? LocalDateTime.now() : oveja.getFechaRegistro(),
                oveja.getProductorId(),
                oveja.getResultadoML()
                // Si quieres exponer la ruta de imagen, agrégala aquí
        );
    }

    public Oveja toDomain(OvejaRequestDTO request) {
        if (request == null) return null;
        return new Oveja(
                null,
                request.getPropietarioId(),
                request.getIdentificacion(),
                request.getEdad(),
                request.getSexo(),
                request.getEstadoSalud(),
                null,
                request.getResultadoML(),
                null
        );
    }

        public OvejaData toData(Oveja oveja) {
                return new OvejaData(
                                oveja.getId(),
                                oveja.getIdentificacion(),
                                oveja.getEdad(),
                                oveja.getSexo(),
                                oveja.getEstadoSalud(),
                                oveja.getProductorId(),
                                oveja.getFechaRegistro(),
                                oveja.getResultadoML(),
                                oveja.getImagenPath()
                );
        }
}

