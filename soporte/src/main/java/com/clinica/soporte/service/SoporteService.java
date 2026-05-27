package com.clinica.soporte.service;

import com.clinica.soporte.model.Cita;
import com.clinica.soporte.model.Soporte;
import com.clinica.soporte.repository.SoporteRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SoporteService {

    private final SoporteRepository repository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String clinicaBaseUrl;

    public SoporteService(SoporteRepository repository, @Value("${clinica.proyecto.base-url:http://localhost:8081}") String clinicaBaseUrl) {
        this.repository = repository;
        this.clinicaBaseUrl = clinicaBaseUrl.replaceAll("/+$", "");
    }

    public List<Soporte> listar() {
        return repository.findAll();
    }

    public Soporte guardar(Soporte request) {
        if (request.getFechaCreacion() == null) {
            request.setFechaCreacion(LocalDateTime.now());
        }
        if (request.getEstado() == null || request.getEstado().isBlank()) {
            request.setEstado("Abierto");
        }
        request.setPrioridad(calcularPrioridad(request));
        return repository.save(request);
    }

    public Soporte buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Soporte no encontrado"));
    }

    public Soporte actualizar(Long id, Soporte request) {
        Soporte existente = buscarPorId(id);
        existente.setUsuario(request.getUsuario());
        existente.setModulo(request.getModulo());
        existente.setDescripcion(request.getDescripcion());
        if (request.getCorreo() != null && !request.getCorreo().isBlank()) {
            existente.setCorreo(request.getCorreo());
        }
        existente.setCitaId(request.getCitaId());
        existente.setEstado(request.getEstado() == null || request.getEstado().isBlank() ? "Actualizado" : request.getEstado());
        existente.setMensaje(request.getMensaje());
        existente.setResultados(request.getResultados());
        existente.setPrioridad(calcularPrioridad(existente));
        return repository.save(existente);
    }

    public void eliminarPorId(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Soporte no encontrado", ex);
        }
    }

    public Cita obtenerCita(Long id) {
        try {
            return consultarMicroservicioClinica(String.format("%s/cita/%d", clinicaBaseUrl, id));
        } catch (ResponseStatusException ex) {
            crearTicketSistema("Sistema", "CITAS", "Error al obtener la cita con ID: " + id);
            throw ex;
        }
    }

    public Cita verificarCita(Long id) {
        try {
            return consultarMicroservicioClinica(String.format("%s/verificar-cita/%d", clinicaBaseUrl, id));
        } catch (ResponseStatusException ex) {
            crearTicketSistema("Sistema", "CITAS", "Error al verificar la cita con ID: " + id);
            throw ex;
        }
    }

    private Cita consultarMicroservicioClinica(String url) {
        try {
            Cita cita = restTemplate.getForObject(url, Cita.class);
            if (cita == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró la cita en el microservicio de clínica");
            }
            return cita;
        } catch (RestClientException ex) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "No se pudo conectar al microservicio de clínica", ex);
        }
    }

    private void crearTicketSistema(String usuario, String modulo, String descripcion) {
        Soporte soporte = new Soporte();
        soporte.setUsuario(usuario);
        soporte.setModulo(modulo);
        soporte.setDescripcion(descripcion);
        soporte.setEstado("Abierto");
        soporte.setPrioridad(calcularPrioridad(soporte));
        soporte.setFechaCreacion(LocalDateTime.now());
        repository.save(soporte);
    }

    public Soporte enviarTicketCorreo(Soporte request) {
        Soporte soporte = buscarPorId(request.getId());
        soporte.setCorreo(request.getCorreo());
        soporte.setMensaje(request.getMensaje());
        soporte.setEstado("CORREO_ENVIADO");
        return repository.save(soporte);
    }

    public Soporte agregarResultados(Soporte request) {
        Soporte soporte = buscarPorId(request.getId());
        soporte.setResultados(request.getResultados());
        soporte.setEstado("RESULTADOS_AGREGADOS");
        soporte.setPrioridad(calcularPrioridad(soporte));
        return repository.save(soporte);
    }

    private String calcularPrioridad(Soporte soporte) {
        String modulo = soporte.getModulo() == null ? "" : soporte.getModulo().trim().toUpperCase();
        String descripcion = soporte.getDescripcion() == null ? "" : soporte.getDescripcion().trim().toLowerCase();

        if (modulo.contains("CITAS") || modulo.contains("RESULTADOS") || modulo.contains("CORREO") || descripcion.contains("error") || descripcion.contains("no puede") || descripcion.contains("fallo") || descripcion.contains("no funciona")) {
            return "GRAVE";
        }
        if (descripcion.length() > 120 || descripcion.contains("lento") || descripcion.contains("problema")) {
            return "MEDIA";
        }
        return "BAJA";
    }
}