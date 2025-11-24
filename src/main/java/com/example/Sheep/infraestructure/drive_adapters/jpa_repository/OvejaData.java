package com.example.Sheep.infraestructure.drive_adapters.jpa_repository;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ovejas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OvejaData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String identificacion;
    private Integer edad;
    private Boolean sexo; // TRUE hembra, FALSE macho
    private String estadoSalud;
    private Long productorId;
    private LocalDateTime fechaRegistro;

    // Resultado del modelo entrenado (por ejemplo: 'Sano', 'Enfermo', etc.)
    private String resultadoML;

    // Ruta de la imagen asociada a la oveja (en el sistema de archivos)
    private String imagenPath;

    @PrePersist
    private void onCreate() {
        // Establecer fecha de registro automática si no viene informada
        if (this.fechaRegistro == null) {
            this.fechaRegistro = LocalDateTime.now();
        }
    }
}
