package pe.edu.cibertec.EF_FRONTEND.controller;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.EF_FRONTEND.client.ConsultarClient;
import pe.edu.cibertec.EF_FRONTEND.dto.ConsultaRequestDTO;
import pe.edu.cibertec.EF_FRONTEND.dto.ConsultaResponseDTO;
import pe.edu.cibertec.EF_FRONTEND.viewmodel.BuscarModel;

import java.util.Set;

@Controller
@RequestMapping("/busqueda")
public class BuscarControllerAsync {

    @Autowired
    ConsultarClient consultarClient;

    @GetMapping("/")
    public String inicio(Model model) {
        BuscarModel buscarModel = new BuscarModel("","");
        model.addAttribute("buscarModel", buscarModel);
        return "inicio";
    }

    @PostMapping("/autenticar")
    public String autenticar(@ModelAttribute("buscarModel") BuscarModel buscarModel, Model model) {
        try {
            ConsultaRequestDTO requestDTO = new ConsultaRequestDTO(buscarModel.codigo());
            // Llamar al servicio usando FeignClient
            ResponseEntity<ConsultaResponseDTO> response = consultarClient.consulta(requestDTO);
            // Comprobar si la respuesta fue exitosa
            if (response.getStatusCode().is2xxSuccessful()) {
                ConsultaResponseDTO alumno = response.getBody();
                // Verificar si el cuerpo de la respuesta es nulo o no contiene datos
                if (alumno == null || alumno.codigoAlumno() == null || alumno.nombres() == null) {
                    model.addAttribute("mensaje", "No se encontraron resultados");
                    return "inicio";
                }

                Set<String> codigosValidos = Set.of("12000024548", "202211234", "202316542", "202319677", "202410783");

                if (!codigosValidos.contains(alumno.codigoAlumno())) {
                    model.addAttribute("mensaje", "Alumno no existente");
                    return "inicio";
                }
                // Añadir datos del alumno al modelo para pasarlos a la vista
                model.addAttribute("codigoAlumno", alumno.codigoAlumno());
                model.addAttribute("nombres", alumno.nombres());
                model.addAttribute("apellidos", alumno.apellidos());
                model.addAttribute("carrera", alumno.carrera());
                model.addAttribute("ciclo", alumno.ciclo());

                // Redirigir a la página de resultados del alumno
                return "alumno";
            } else {
                model.addAttribute("mensaje", "Error al consumir servicio");
                return "inicio";
            }
        } catch (FeignException.GatewayTimeout e) {
            // Mensaje específico para timeout
            model.addAttribute("mensaje", "Error: Se ha agotado el tiempo de espera al consumir el servicio (timeout de conexión).");
            return "inicio";
        } catch (FeignException e) {
            // Manejar cualquier otra excepción lanzada por Feign
            model.addAttribute("mensaje", "Error: Se ha agotado el tiempo de espera al consumir el servicio (timeout de lectura).");
            return "inicio";
        } catch (Exception e) {
            // Manejar cualquier otro error general
            model.addAttribute("mensaje", "Error inesperado: " + e.getMessage());
            return "inicio";
        }
    }
}
