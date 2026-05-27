package com.historial.clinico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.historial.clinico.model.HistorialClinico;

public interface HistorialClinicoRepository extends JpaRepository<HistorialClinico, Long> {
}
