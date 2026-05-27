package com.historial.clinico.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.historial.clinico.dto.HistorialClinicoDTO;
import com.historial.clinico.dto.HistorialClinicoResponseDTO;
import com.historial.clinico.model.HistorialClinico;
import com.historial.clinico.repository.HistorialClinicoRepository;

@Service
public class HistorialClinicoService {

    @Autowired
    private HistorialClinicoRepository repository;

    public List<HistorialClinicoResponseDTO> listar() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public HistorialClinicoResponseDTO crear(HistorialClinicoDTO dto) {
        HistorialClinico entity = toEntity(dto);
        return toResponseDTO(repository.save(entity));
    }

    public HistorialClinicoResponseDTO buscar(Long id) {
        return repository.findById(id)
                .map(this::toResponseDTO)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con id " + id));
    }

    public HistorialClinicoResponseDTO actualizar(Long id, HistorialClinicoDTO dto) {
        HistorialClinico existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historial no encontrado con id " + id));

        existing.setIdPaciente(dto.getIdPaciente());
        existing.setNombrePaciente(dto.getNombrePaciente());
        existing.setFechaConsulta(dto.getFechaConsulta());
        existing.setDiagnostico(dto.getDiagnostico());
        existing.setTratamiento(dto.getTratamiento());
        existing.setObservaciones(dto.getObservaciones());
        existing.setMedicoResponsable(dto.getMedicoResponsable());

        return toResponseDTO(repository.save(existing));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Historial no encontrado con id " + id);
        }
        repository.deleteById(id);
    }

    public List<HistorialClinicoResponseDTO> buscarPorPaciente(Long idPaciente) {
        return repository.findAll().stream()
                .filter(h -> h.getIdPaciente().equals(idPaciente))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialClinicoResponseDTO> buscarPorFecha(LocalDate fechaConsulta) {
        return repository.findAll().stream()
                .filter(h -> h.getFechaConsulta().equals(fechaConsulta))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<HistorialClinicoResponseDTO> buscarPorDiagnostico(String diagnostico) {
        return repository.findAll().stream()
                .filter(h -> h.getDiagnostico().toLowerCase().contains(diagnostico.toLowerCase()))
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private HistorialClinicoResponseDTO toResponseDTO(HistorialClinico historial) {
        return HistorialClinicoResponseDTO.builder()
                .id(historial.getId())
                .idPaciente(historial.getIdPaciente())
                .nombrePaciente(historial.getNombrePaciente())
                .fechaConsulta(historial.getFechaConsulta())
                .diagnostico(historial.getDiagnostico())
                .medicoResponsable(historial.getMedicoResponsable())
                .build();
    }

    private HistorialClinico toEntity(HistorialClinicoDTO dto) {
        return new HistorialClinico(null, dto.getIdPaciente(), dto.getNombrePaciente(),
                dto.getFechaConsulta(), dto.getDiagnostico(), dto.getTratamiento(),
                dto.getObservaciones(), dto.getMedicoResponsable());
    }
}
