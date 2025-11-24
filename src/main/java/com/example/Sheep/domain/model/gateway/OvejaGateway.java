package com.example.Sheep.domain.model.gateway;

import com.example.Sheep.domain.model.Oveja;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OvejaGateway {
	long countByProductor(Long productorId);
 Oveja guardarOveja(Oveja oveja);
 Oveja buscarPorIdOveja(Long id);
 Oveja actualizarOveja(Oveja oveja);
 void eliminarPorID(Long id);
 Oveja buscarPorIdentificacion(String identificacion);
 Page<Oveja> obtenerPaginado(Pageable pageable);
 Page<Oveja> obtenerPorProductor(Long productorId, Pageable pageable);
 
 // Actualiza en lote el `productorId` para una lista de oveja ids.
 int asignarProductorAIds(Long productorId, java.util.List<Long> ids);
}
