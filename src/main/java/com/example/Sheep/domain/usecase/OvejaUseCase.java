package com.example.Sheep.domain.usecase;

import com.example.Sheep.domain.exception.DomainValidationException;
import com.example.Sheep.domain.exception.PropietarioNotFoundException;
import com.example.Sheep.domain.model.Oveja;
import com.example.Sheep.domain.model.gateway.UsuarioGateway;
import com.example.Sheep.infraestructure.client.GanaderoClient;
import com.example.Sheep.domain.model.gateway.OvejaGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Caso de uso de Oveja: orquesta reglas de dominio y persistencia.
 *
 * Reglas clave:
 * - Validaci�n de identificadores y edad en la entidad.
 * - Validaci�n remota de existencia de ganadero cuando se informa ganaderoId.
 */
@RequiredArgsConstructor
@Service
public class OvejaUseCase {
    public long countByProductor(Long productorId) {
        return ovejaGateway.countByProductor(productorId);
    }
 private final OvejaGateway ovejaGateway;
 private final UsuarioGateway usuarioGateway;
 private final GanaderoClient ganaderoClient;

 public Oveja guardarOveja(Oveja oveja){
 oveja.validate();
 // validar existencia del Productor si viene informado
 if (oveja.getProductorId() != null && !usuarioGateway.existeUsuario(oveja.getProductorId())) {
 throw new PropietarioNotFoundException(oveja.getProductorId());
 }
 return ovejaGateway.guardarOveja(oveja);
 }
 public Oveja buscarPorIdOveja(Long id){
 return ovejaGateway.buscarPorIdOveja(id);
 }
 public Oveja actualizarOveja(Oveja oveja){
 if(oveja.getId()==null){
 throw new DomainValidationException("ID requerido");
 }
 oveja.validate();
 if (oveja.getProductorId() != null && !usuarioGateway.existeUsuario(oveja.getProductorId())) {
 throw new PropietarioNotFoundException(oveja.getProductorId());
 }
 return ovejaGateway.actualizarOveja(oveja);
 }
 public void eliminar(Long id){
 ovejaGateway.eliminarPorID(id);
 }
 public Oveja buscarPorIdentificacion(String identificacion){
 return ovejaGateway.buscarPorIdentificacion(identificacion);
 }
 public Page<Oveja> obtenerPaginado(Pageable pageable){
 return ovejaGateway.obtenerPaginado(pageable);
 }
 public Page<Oveja> obtenerPorProductor(Long productorId, Pageable pageable){
 return ovejaGateway.obtenerPorProductor(productorId, pageable);
 }
    public int asignarMuchasOvejasAProductor(Long productorId, java.util.List<Long> ovejaIds){
        if (productorId == null) throw new IllegalArgumentException("productorId requerido");
        // Validación optimista: consultar ganadero-service una sola vez por petición
        if (!ganaderoClient.existeGanadero(productorId)){
            throw new PropietarioNotFoundException(productorId);
        }
        return ovejaGateway.asignarProductorAIds(productorId, ovejaIds);
    }

}
