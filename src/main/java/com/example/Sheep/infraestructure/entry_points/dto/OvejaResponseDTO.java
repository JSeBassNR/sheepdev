package com.example.Sheep.infraestructure.entry_points.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OvejaResponseDTO {
	private Long id;
	private String identificacion;
	private Integer edad;
	private Boolean sexo;
	private String estadoSalud;
	private LocalDateTime fechaRegistro;
	private Long productorId;
	private String resultadoML;
}
