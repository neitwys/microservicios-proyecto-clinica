package com.clinica.recetas.controller;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.clinica.recetas.DTO.RecetaCrearDTO;
import com.clinica.recetas.DTO.RecetaDetalleDTO;
import com.clinica.recetas.DTO.RecetaMostrarDTO;
import com.clinica.recetas.service.RecetasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/recetas")
public class RecetaController { 

    private final RecetasService service;


    public RecetaController(RecetasService service) {
        this.service = service;
    }

    @GetMapping("/detalle/paciente/{idPaciente}")
    public List<RecetaDetalleDTO> detallePaciente(
        @PathVariable Integer idPaciente) {

    return service.listarDetallePaciente(idPaciente);
    }
  
    @PostMapping("/crear")
    public com.clinica.recetas.DTO.RecetaMostrarDTO crear(
            @Valid @RequestBody com.clinica.recetas.DTO.RecetaCrearDTO dto) {

        return service.crearReceta(dto);
    }

  
    @GetMapping("/listar")
    public List<RecetaMostrarDTO> listar() {
        return  service.listarTodas();
    }

  
    @GetMapping("/buscar/{id}")
    public RecetaMostrarDTO buscarPorId(
            @PathVariable Integer id) {

        return service.buscarPorId(id);
    }

   
    @GetMapping("/paciente/{idPaciente}")
    public List<RecetaMostrarDTO> listarPorPaciente(
            @PathVariable Integer idPaciente) {

        return service.listarPorPaciente(idPaciente);
    }

   
    @GetMapping("/medico/{idMedico}")
    public List<RecetaMostrarDTO> listarPorMedico(
            @PathVariable Integer idMedico) {

        return service.listarPorMedico(idMedico);
    }

   
    @DeleteMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {

        RecetaMostrarDTO receta = service.buscarPorId(id);

        if (receta != null) {
            service.eliminarPorId(id);
            return "Receta eliminada";
        }

        return "Receta no encontrada";
    }

    @PutMapping("/actualizar/{id}")
    public String actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody RecetaCrearDTO dto) {

        RecetaMostrarDTO receta = service.actualizarReceta(id, dto);

        if (receta != null) {
            return "Receta actualizada";
        }

        return "Receta no encontrada";
    }
}