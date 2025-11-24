package com.example.Sheep.infraestructure.entry_points;

import com.example.Sheep.domain.model.Oveja;
import com.example.Sheep.domain.usecase.OvejaUseCase;
import com.example.Sheep.infraestructure.entry_points.dto.OvejaRequestDTO;
import com.example.Sheep.infraestructure.entry_points.dto.OvejaResponseDTO;
import com.example.Sheep.infraestructure.mapper.MapperOveja;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


import java.util.List;


@RestController
@RequestMapping("/api/MachIA/ovejas")
@RequiredArgsConstructor
public class OvejaController {
    @GetMapping("/productor/{productorId}/count")
    public ResponseEntity<Long> countByProductor(@PathVariable Long productorId) {
        long count = ovejaUseCase.countByProductor(productorId);
        return ResponseEntity.ok(count);
    }

    private final OvejaUseCase ovejaUseCase;
    private final MapperOveja mapperOveja;

    @PostMapping("/save")
    public ResponseEntity<OvejaResponseDTO> save(@Valid @RequestBody OvejaRequestDTO request){
        Oveja domain = mapperOveja.toDomain(request);
        Oveja saved = ovejaUseCase.guardarOveja(domain);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperOveja.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OvejaResponseDTO> findById(@PathVariable Long id){
        Oveja o = ovejaUseCase.buscarPorIdOveja(id);
        return ResponseEntity.ok(mapperOveja.toResponse(o));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OvejaResponseDTO> update(@PathVariable Long id, @Valid @RequestBody OvejaRequestDTO request){
        Oveja domain = mapperOveja.toDomain(request);
        domain.setId(id);
        Oveja updated = ovejaUseCase.actualizarOveja(domain);
        return ResponseEntity.ok(mapperOveja.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        ovejaUseCase.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/identificacion/{identificacion}")
    public ResponseEntity<OvejaResponseDTO> findByIdentificacion(@PathVariable String identificacion){
        Oveja o = ovejaUseCase.buscarPorIdentificacion(identificacion);
        return o != null ? ResponseEntity.ok(mapperOveja.toResponse(o)) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<Page<OvejaResponseDTO>> findAllPaged(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size){
        Page<Oveja> ovejas = ovejaUseCase.obtenerPaginado(PageRequest.of(page, size));
        if (!ovejas.hasContent()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        List<OvejaResponseDTO> content = ovejas.getContent().stream().map(mapperOveja::toResponse).toList();
        Page<OvejaResponseDTO> dtoPage = new PageImpl<>(content, ovejas.getPageable(), ovejas.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/productor/{productorId}")
    public ResponseEntity<Page<OvejaResponseDTO>> findByProductor(@PathVariable Long productorId,
                                                                 @RequestParam(defaultValue = "0") int page,
                                                                 @RequestParam(defaultValue = "10") int size){
        Page<Oveja> ovejas = ovejaUseCase.obtenerPorProductor(productorId, PageRequest.of(page, size));
        if (!ovejas.hasContent()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        List<OvejaResponseDTO> content = ovejas.getContent().stream().map(mapperOveja::toResponse).toList();
        Page<OvejaResponseDTO> dtoPage = new PageImpl<>(content, ovejas.getPageable(), ovejas.getTotalElements());
        return ResponseEntity.ok(dtoPage);
    }
        // Carpeta base donde se guardarán las imágenes
    private static final String IMAGES_BASE_PATH = System.getProperty("user.dir") + "/uploads/ovejas/";

    @PostMapping(value = "/{id}/imagen", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // Crear carpeta si no existe
            File dir = new File(IMAGES_BASE_PATH);
            if (!dir.exists()) dir.mkdirs();
            // Nombre único: oveja_{id}_timestamp.ext
            String ext = file.getOriginalFilename() != null && file.getOriginalFilename().contains(".") ? file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')) : ".jpg";
            String filename = "oveja_" + id + "_" + System.currentTimeMillis() + ext;
            File dest = new File(dir, filename);
            file.transferTo(dest);
            // Actualizar ruta en la oveja
            Oveja oveja = ovejaUseCase.buscarPorIdOveja(id);
            if (oveja == null) return ResponseEntity.notFound().build();
            oveja.setImagenPath(dest.getAbsolutePath());
            ovejaUseCase.actualizarOveja(oveja);

            // Llamar a Modelo_Entrenado_Ovinos para procesar la oveja
            try {
                URL url = new URL("http://localhost:8000/procesar-oveja");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);
                String jsonInputString = "{\"oveja_id\":" + id + "}";
                try (java.io.OutputStream os = con.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }
                int code = con.getResponseCode();
                // Opcional: leer respuesta
            } catch (Exception ex) {
                // Loguear pero no interrumpir el flujo principal
                System.err.println("Error llamando a Modelo_Entrenado_Ovinos: " + ex.getMessage());
            }

            return ResponseEntity.ok("Imagen subida correctamente");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la imagen: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}/imagen", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<FileSystemResource> getImage(@PathVariable Long id) {
        Oveja oveja = ovejaUseCase.buscarPorIdOveja(id);
        if (oveja == null || oveja.getImagenPath() == null) return ResponseEntity.notFound().build();
        File imgFile = new File(oveja.getImagenPath());
        if (!imgFile.exists()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new FileSystemResource(imgFile));
    }

}

