package com.clinica.examenes.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.clinica.examenes.dto.ExamenDetalleDTO;
import com.clinica.examenes.model.Examenes;
import com.clinica.examenes.model.Usuario;
import com.clinica.examenes.repository.ExamenesRepository;

@Service
public class ExamenesService {

    private final ExamenesRepository repository;

    public ExamenesService(ExamenesRepository repository) {
        this.repository = repository;
    }


    // POST
    public Examenes guardarExamenes(Examenes examenes) {

        return repository.save(examenes);
    }


    // GET ALL
    public List<Examenes> listar() {

        return repository.findAll();
    }


    // GET POR PACIENTE
    public List<Examenes> buscarPorPaciente(Integer idPaciente) {

        return repository.findByIdPaciente(idPaciente);
    }


    // GET DTO POR PACIENTE
    public List<ExamenDetalleDTO> obtenerExamenDTO(Integer idPaciente) {

        List<Examenes> examenes =
                repository.findByIdPaciente(idPaciente);

        List<ExamenDetalleDTO> examenesDTO =
                new ArrayList<>();

        for (Examenes e : examenes) {

            ExamenDetalleDTO dto =
                    new ExamenDetalleDTO();

            dto.setId(e.getId());
            dto.setNombreExamen(e.getNombreExamen());
            dto.setFechaExamen(e.getFechaExamen());
            dto.setResultado(e.getResultado());
            dto.setEstado(e.getEstado());

            dto.setNombrePaciente(
                    obtenerNombreDesdeAutenticacion(
                            e.getIdPaciente()
                    )
            );

            dto.setNombreMedico(
                    obtenerNombreDesdeAutenticacion(
                            e.getIdMedico()
                    )
            );

            examenesDTO.add(dto);
        }

        return examenesDTO;
    }


    // GET DTO POR ID
    public ExamenDetalleDTO obtenerExamenPorId(Integer id) {

        Examenes e = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Examen no encontrado"
                        ));

        ExamenDetalleDTO dto =
                new ExamenDetalleDTO();

        dto.setId(e.getId());
        dto.setNombreExamen(e.getNombreExamen());
        dto.setFechaExamen(e.getFechaExamen());
        dto.setResultado(e.getResultado());
        dto.setEstado(e.getEstado());

        dto.setNombrePaciente(
                obtenerNombreDesdeAutenticacion(
                        e.getIdPaciente()
                )
        );

        dto.setNombreMedico(
                obtenerNombreDesdeAutenticacion(
                        e.getIdMedico()
                )
        );

        return dto;
    }


    // PUT
    public Examenes actualizar(
            Integer id,
            Examenes examenActualizado) {

        Examenes examen = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Examen no encontrado"
                        ));

        examen.setNombreExamen(
                examenActualizado.getNombreExamen()
        );

        examen.setFechaExamen(
                examenActualizado.getFechaExamen()
        );

        examen.setResultado(
                examenActualizado.getResultado()
        );

        examen.setEstado(
                examenActualizado.getEstado()
        );

        examen.setIdPaciente(
                examenActualizado.getIdPaciente()
        );

        examen.setIdMedico(
                examenActualizado.getIdMedico()
        );

        return repository.save(examen);
    }


    // DELETE
    public void eliminar(Integer id) {

        Examenes examen = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Examen no encontrado"
                        ));

        repository.delete(examen);
    }


    // MÉTODO AUXILIAR
    private String obtenerNombreDesdeAutenticacion(
            Integer id) {

        if (id == null) {
            return "No asignado";
        }

        RestTemplate restTemplate =
                new RestTemplate();

        String url =
                "http://localhost:8081/autenticacion/buscar/"
                        + id;

        try {

            Usuario user =
                    restTemplate.getForObject(
                            url,
                            Usuario.class
                    );

            return (user != null)
                    ? user.getNombre()
                    : "No encontrado";

        } catch (RestClientException e) {

            return "Error de conexión";
        }
    }
}