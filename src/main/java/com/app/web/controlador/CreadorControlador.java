package com.app.web.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.app.web.entidad.Creador;
import com.app.web.servicio.CreadorServicio;
import com.app.web.servicio.ProyectoServicio;

@Controller
public class CreadorControlador {
    
    @Autowired
    private CreadorServicio servicio;
    
    @Autowired
    private ProyectoServicio proyectoServicio;
    
    @GetMapping({"/creadores", "/creadores/listar"})
    public String listarCreadores(Model modelo) {
        modelo.addAttribute("creadores", servicio.listarTodosLosCreadores());
        return "Creadores"; // nos retorna al archivo creadores.html
    }
    
    @GetMapping("/creadores/nuevo")
    public String mostrarFormularioDeRegistrarCreador(Model modelo) {
        Creador creador = new Creador();
        modelo.addAttribute("creador", creador);
        modelo.addAttribute("proyectos", proyectoServicio.listarTodosLosProyectos());
        return "crear_creador";
    }
    
    @PostMapping("/creadores")
    public String guardarCreador(@ModelAttribute("creador") Creador creador) {
        servicio.guardarCreador(creador);
        return "redirect:/creadores";
    }
    
    @GetMapping("/creadores/editar/{id}")
    public String mostrarFormularioDeEditar(@PathVariable Long id, Model modelo) {
        Creador creador = servicio.obtenerCreadorPorId(id);
        if (creador != null) {
            modelo.addAttribute("creador", creador);
            modelo.addAttribute("proyectos", proyectoServicio.listarTodosLosProyectos());
            return "editar_creador";
        } else {
            return "redirect:/creadores"; // Si no se encuentra el creador, redirige a la lista
        }
    }
    
    @PostMapping("/creadores/editar/{id}")
    public String actualizarCreador(@PathVariable Long id, @ModelAttribute("creador") Creador creador, Model modelo) {
        Creador creadorExistente = servicio.obtenerCreadorPorId(id);
        if (creadorExistente != null) {
            creadorExistente.setId(id);
            creadorExistente.setNombres(creador.getNombres());
            creadorExistente.setApellidos(creador.getApellidos());
            creadorExistente.setCorreo(creador.getCorreo());
            creadorExistente.setTelefono(creador.getTelefono());
            creadorExistente.setRol(creador.getRol());
            creadorExistente.setFechaVinculacion(creador.getFechaVinculacion());
            creadorExistente.setProyecto(creador.getProyecto()); // Añadida esta línea para actualizar el proyecto
            
            servicio.actualizarCreador(creadorExistente);
        }
        return "redirect:/creadores";
    }
    
    @GetMapping("/creadores/eliminar/{id}")
    public String eliminarCreador(@PathVariable Long id) {
        servicio.eliminarCreador(id);
        return "redirect:/creadores";
    }
}