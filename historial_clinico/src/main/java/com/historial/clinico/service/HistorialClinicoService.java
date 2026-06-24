package com.historial.clinico.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.historial.clinico.dto.CitaEnHistorialDTO;
import com.historial.clinico.dto.HistorialClinicoDTO;
import com.historial.clinico.dto.HistorialClinicoResponseDTO;
import com.historial.clinico.dto.PagoEnHistorialDTO;
import com.historial.clinico.dto.RecetaEnHistorialDTO;
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
        List<CitaEnHistorialDTO> citas = obtenerCitas(historial.getIdPaciente());
        List<PagoEnHistorialDTO> pagos = obtenerPagos(historial.getIdPaciente());
        List<RecetaEnHistorialDTO> recetas = obtenerRecetas(historial.getIdPaciente());

        return HistorialClinicoResponseDTO.builder()
                .id(historial.getId())
                .idPaciente(historial.getIdPaciente())
                .nombrePaciente(historial.getNombrePaciente())
                .fechaConsulta(historial.getFechaConsulta())
                .diagnostico(historial.getDiagnostico())
                .medicoResponsable(historial.getMedicoResponsable())
                .citas(citas)
                .pagos(pagos)
                .recetas(recetas)
                .build();
    }

    private List<CitaEnHistorialDTO> obtenerCitas(Long idPaciente) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            CitaEnHistorialDTO[] citas = restTemplate.getForObject(
                    "http://localhost:8082/citas/paciente/" + idPaciente,
                    CitaEnHistorialDTO[].class
            );
            return citas != null ? List.of(citas) : Collections.emptyList();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }

    private List<PagoEnHistorialDTO> obtenerPagos(Long idPaciente) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            PagoEnHistorialDTO[] pagos = restTemplate.getForObject(
                    "http://localhost:8084/pagos/listar",
                    PagoEnHistorialDTO[].class
            );

            if (pagos == null) return Collections.emptyList();

            return List.of(pagos).stream()
                    .filter(p -> p.getIdPaciente() != null && p.getIdPaciente().equals(idPaciente.intValue()))
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }

    private List<RecetaEnHistorialDTO> obtenerRecetas(Long idPaciente) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            RecetaEnHistorialDTO[] recetas = restTemplate.getForObject(
                    "http://localhost:8086/recetas/paciente/" + idPaciente,
                    RecetaEnHistorialDTO[].class
            );
            return recetas != null ? List.of(recetas) : Collections.emptyList();
        } catch (RestClientException e) {
            return Collections.emptyList();
        }
    }

    private HistorialClinico toEntity(HistorialClinicoDTO dto) {
        return new HistorialClinico(null, dto.getIdPaciente(), dto.getNombrePaciente(),
                dto.getFechaConsulta(), dto.getDiagnostico(), dto.getTratamiento(),
                dto.getObservaciones(), dto.getMedicoResponsable());
    }
}
