package pe.edu.cibertec.EF_FRONTEND.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pe.edu.cibertec.EF_FRONTEND.config.FeignConfig;
import pe.edu.cibertec.EF_FRONTEND.dto.ConsultaRequestDTO;
import pe.edu.cibertec.EF_FRONTEND.dto.ConsultaResponseDTO;

@FeignClient(name = "buscar", url = "http://localhost:8090/alumno", configuration = FeignConfig.class)
public interface ConsultarClient {

    @PostMapping("/buscar")
    ResponseEntity<ConsultaResponseDTO> consulta(@RequestBody ConsultaRequestDTO consultaRequestDTO);
}
