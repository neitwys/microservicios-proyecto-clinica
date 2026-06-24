package com.clinica.pagos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinica.pagos.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer>{
    List<Pago> findByIdCita(Integer idCita);
    boolean existsByIdCitaAndEstado(Integer idCita, String estado);
}