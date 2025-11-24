package com.example.Sheep.infraestructure.drive_adapters.jpa_repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OvejaDataJpaRepository extends JpaRepository<OvejaData, Long> {
        long countByProductorId(Long productorId);
    OvejaData findByIdentificacion(String identificacion);
    Page<OvejaData> findByProductorId(Long productorId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("update OvejaData o set o.productorId = :productorId where o.id in :ids")
    int updateProductorIdByIds(@Param("productorId") Long productorId, @Param("ids") List<Long> ids);
}
