package com.clinica.pagos.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.clinica.pagos.dto.PagoCrearDTO;
import com.clinica.pagos.dto.PagoMostrarDTO;
import com.clinica.pagos.model.Pago;
import com.clinica.pagos.model.Usuario;
import com.clinica.pagos.repository.PagoRepository;

@Service
public class PagoService {
    private final PagoRepository repository;

    public PagoService(PagoRepository repository) {
        this.repository = repository;
    }

    // Listar todos los PagoMostrarDTO
    public List<PagoMostrarDTO> listarTodos() {
        List<Pago> pagos = repository.findAll();
        List<PagoMostrarDTO> dtos = new ArrayList<>();
        
        for (Pago pago : pagos) {
            dtos.add(convertirADTO(pago));
        }
        
        return dtos;
    }

    // Buscar por el ID del PagoMostrarDTO
    public PagoMostrarDTO buscarPorId(Integer id) {
        Pago pago = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado con id " + id + " no existe"));
        return convertirADTO(pago);
    }
    
public PagoMostrarDTO registrarPago(PagoCrearDTO dto) {
    Pago pago = new Pago();
    pago.setIdCita(dto.getIdCita());
    pago.setIdPaciente(dto.getIdPaciente());
    pago.setMonto(dto.getMonto());
    pago.setMetodoPago(dto.getMetodoPago());
    pago.setEstado("COMPLETADO"); // Si el pago es exitoso

    Pago guardado = repository.save(pago);

    try {
        RestTemplate restTemplate = new RestTemplate();

        java.util.Map<String, String> notiRequest = new java.util.HashMap<>();
        notiRequest.put("destinatario", "Paciente ID: " + guardado.getIdPaciente());
        notiRequest.put("mensaje", "Tu pago de $" + guardado.getMonto() + " ha sido registrado exitosamente.");
        notiRequest.put("tipo", "EMAIL");

        restTemplate.postForObject("http://localhost:8087/notificaciones/crear", notiRequest, String.class);
    } catch (RestClientException e) {
        System.out.println("Error al enviar notificación: " + e.getMessage());
    }
 
    return convertirADTO(guardado);
    }

    public void eliminarPorId(Integer id) {
        buscarPorId(id);
        repository.deleteById(id);
    }

    public PagoMostrarDTO actualizarPago(Integer id, PagoCrearDTO dto) {
        Pago pagoExistente = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("No se puede actualizar, pago no encontrado con id: " + id + " no existe"));

        pagoExistente.setMonto(dto.getMonto());
        pagoExistente.setMetodoPago(dto.getMetodoPago());

        Pago actualizado = repository.save(pagoExistente);
        return convertirADTO(actualizado);
    }

    // Método para convertir de Pago a PagoMostrarDTO
    private PagoMostrarDTO convertirADTO(Pago pago) {
        PagoMostrarDTO dto = new PagoMostrarDTO();
        dto.setId(pago.getId());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setFechaCreacion(pago.getFechaCreacion());
        
        dto.setIdCita(pago.getIdCita());
        dto.setIdPaciente(pago.getIdPaciente());
        dto.setNombrePaciente(obtenerNombrePaciente(pago.getIdPaciente())); // <--- ¡Épico!
        
        return dto;
    }   
    private String obtenerNombrePaciente(Integer id) {
        if (id == null) return "No asignado";
        RestTemplate restTemplate = new RestTemplate();
        try {
            // Asumiendo que Autenticación sigue en el 8081
            String url = "http://localhost:8081/autenticacion/buscar/" + id;
            Usuario user = restTemplate.getForObject(url, Usuario.class);
            return (user != null) ? user.getNombre() : "Desconocido";
        } catch (RestClientException e) {
            return "Error de conexión con Autenticación";
        }
    }
}