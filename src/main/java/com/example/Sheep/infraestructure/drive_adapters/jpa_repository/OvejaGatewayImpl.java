package com.example.Sheep.infraestructure.drive_adapters.jpa_repository;

import com.example.Sheep.domain.model.Oveja;
import com.example.Sheep.domain.model.gateway.OvejaGateway;
import com.example.Sheep.infraestructure.mapper.MapperOveja;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * Adaptador de salida (driver adapter) para la persistencia JPA de Oveja.
 * Implementa el puerto de dominio {@link OvejaGateway}.
 *
 * Reglas implementadas aqu�:
 * - Protecci�n contra duplicidad de identificaci�n (por DB y chequeo previo).
 */
@Repository
@RequiredArgsConstructor
public class OvejaGatewayImpl implements OvejaGateway {
    @Override
    public long countByProductor(Long productorId) {
        return repository.countByProductorId(productorId);
    }

    private final OvejaDataJpaRepository repository;
    private final MapperOveja mapperOveja;

    @Override
    public Oveja guardarOveja(Oveja oveja) {
        OvejaData ovejaData = mapperOveja.toData(oveja);
        return mapperOveja.toOveja(repository.save(ovejaData));
    }

    @Override
    public Oveja buscarPorIdOveja(Long id) {
        return repository.findById(id)
                .map(ovejaData -> mapperOveja.toOveja(ovejaData))
                .orElseThrow(() -> new RuntimeException("Oveja no encontrada"));
    }

    @Override
    public Oveja actualizarOveja(Oveja oveja) {
        if (!repository.existsById(oveja.getId())) {
            throw new RuntimeException("Usuario con id " + oveja.getId() + " no existe");
        }
        OvejaData existing = repository.findById(oveja.getId())
                .orElseThrow(() -> new RuntimeException("Oveja con id " + oveja.getId() + " no existe"));
        return mapperOveja.toOveja(repository.save(existing));
    }

    @Override
    public void eliminarPorID(Long id) {
        try {
            repository.deleteById(id);
        } catch (Exception error) {
            throw new RuntimeException(error.getMessage());
        }
    }

    @Override
    public Oveja buscarPorIdentificacion(String identificacion) {
        var data = repository.findByIdentificacion(identificacion);
        return data == null ? null : mapperOveja.toOveja(data);
    }

    @Override
    public Page<Oveja> obtenerPaginado(Pageable pageable) {
        return repository.findAll(pageable).map(mapperOveja::toOveja);
    }

    @Override
    public Page<Oveja> obtenerPorProductor(Long productorId, Pageable pageable) {
        return repository.findByProductorId(productorId, pageable).map(mapperOveja::toOveja);
    }

    @Override
    public int asignarProductorAIds(Long productorId, List<Long> ids) {
        if (ids == null || ids.isEmpty()) return 0;
        final int BATCH = 500;
        int updated = 0;
        for (int i = 0; i < ids.size(); i += BATCH) {
            int end = Math.min(i + BATCH, ids.size());
            List<Long> chunk = ids.subList(i, end);
            updated += repository.updateProductorIdByIds(productorId, chunk);
        }
        return updated;
    }
}
