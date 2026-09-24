package sistema.Academia_api.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sistema.Academia_api.Matricula.MatriculaRequest;
import sistema.Academia_api.Matricula.MatriculaResponse;
import sistema.Academia_api.service.MatriculaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping("/matriculas") 
public class MatriculaController {
    
private final MatriculaService service;

public MatriculaController(MatriculaService service) {
    this.service = service;
}


@PostMapping("/cadastrar")

public MatriculaResponse cadastrar(@RequestBody @Valid MatriculaRequest request) {
    return service.cadastrarMatricula(request);
}

@GetMapping("/buscar/{id}")

public MatriculaResponse buscar(@PathVariable  Long id) {
    return service.buscarMatricula(id);
}


@GetMapping

public List<MatriculaResponse> listar() {

    return service.listar();
}

@PutMapping("/alterar/{id}")

public MatriculaResponse alterar(@PathVariable Long id, @RequestBody @Valid MatriculaRequest request ) {
    return service.alterarMatricula(id, request);
}

@PutMapping("/ativar/{id}")

public MatriculaResponse ativar(@PathVariable Long id) {
    return service.ativarMatricula(id);
}

@PutMapping("/desativar/{id}")

public MatriculaResponse desativar(@PathVariable Long id) {
    return service.desativarMatricula(id);
}

}
 