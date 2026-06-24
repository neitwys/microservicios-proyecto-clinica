package com.mvclinicas.Citas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mvclinicas.Citas.dto.PagoCrearDTO;
import com.mvclinicas.Citas.dto.PagoMostrarDTO;
import com.mvclinicas.Citas.model.Citas;
import com.mvclinicas.Citas.repository.CitaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class CitasService {

    @Autowired
    private CitaRepository repository;

    
    // CREAR CITA
public Citas crearCita(Citas cita) {

    boolean existe = repository.existsByIdMedicoAndFechaAndHora(
            cita.getIdMedico(),
            cita.getFecha(),
            cita.getHora()
    );

    if (existe) {
        throw new RuntimeException("El médico ya tiene una cita en ese horario");
    }

    if (cita.getFecha().isEqual(LocalDate.now()) &&
            cita.getHora().isBefore(LocalTime.now())) {
        throw new RuntimeException("No puedes agendar en una hora pasada");
    }

    cita.setEstado("PENDIENTE");
    cita.setIdPago(null); 

    return repository.save(cita);
}


    // OBTENER POR ID
    public Citas obtenerPorId(Integer id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cita no encontrada"));
    }


    // LISTAR TODAS
    public List<Citas> listar() {

        return repository.findAll();
    }


    // LISTAR POR PACIENTE
    public List<Citas> listarPorPaciente(Integer idPaciente) {

        return repository.findByIdPaciente(idPaciente);
    }


    // LISTAR POR MÉDICO
    public List<Citas> listarPorMedico(Integer idMedico) {

        return repository.findByIdMedico(idMedico);
    }


    // ACTUALIZAR
    public Citas actualizar(Integer id, Citas citaActualizada) {

        Citas cita = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cita no encontrada"));

        // Validar horario ocupado
        boolean existe = repository.existsByIdMedicoAndFechaAndHora(
                citaActualizada.getIdMedico(),
                citaActualizada.getFecha(),
                citaActualizada.getHora()
        );

        // Evita conflicto consigo misma
        if (existe &&
                !cita.getIdCita().equals(id)) {

            throw new RuntimeException(
                    "El médico ya tiene una cita en ese horario"
            );
        }

        cita.setIdPaciente(citaActualizada.getIdPaciente());
        cita.setIdMedico(citaActualizada.getIdMedico());
        cita.setFecha(citaActualizada.getFecha());
        cita.setHora(citaActualizada.getHora());
        cita.setMotivo(citaActualizada.getMotivo());
        cita.setEstado(citaActualizada.getEstado());

        return repository.save(cita);
    }


    // ELIMINAR
    public void eliminar(Integer id) {

        Citas cita = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Cita no encontrada"));

        repository.delete(cita);
    }


    // CONFIRMAR CITA
    public Citas confirmarCita(Integer id, Integer idPago) { 
    Citas cita = obtenerPorId(id);

    if ("CONFIRMADA".equals(cita.getEstado())) {
        throw new IllegalStateException("La cita con ID " + id + " ya se encuentra CONFIRMADA.");
    }
    
    if ("CANCELADA".equals(cita.getEstado())) {
        throw new IllegalStateException("No se puede confirmar una cita que ya fue CANCELADA.");
    }

    cita.setEstado("CONFIRMADA");
    cita.setIdPago(idPago); 

    return repository.save(cita);
    }

    // CANCELAR CITA
    public Citas cancelarCita(Integer id) {

        Citas cita = obtenerPorId(id);

        cita.setEstado("CANCELADA");

        return repository.save(cita);
    }
}