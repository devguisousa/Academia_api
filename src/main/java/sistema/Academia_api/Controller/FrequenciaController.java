package sistema.Academia_api.Controller;


import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sistema.Academia_api.frequencia.FrequenciaRequest;
import sistema.Academia_api.frequencia.FrequenciaResponse;
import sistema.Academia_api.service.FrequenciaService;

@RestController 
@RequestMapping ("/frequencias")
@RequiredArgsConstructor 
public class FrequenciaController {
    
    private final FrequenciaService frequenciaService;
    
    @PostMapping
    public FrequenciaResponse cadastrar(@RequestBody @Valid FrequenciaRequest request) {
        return frequenciaService.cadastrar(request);
            }


    @GetMapping
    public Page<FrequenciaResponse> listar(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
                return frequenciaService.listar(page, size);
            }
    
     @GetMapping ("/buscar/{nome}")
     public Page<FrequenciaResponse> buscar(@PathVariable String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
                return frequenciaService.buscar(nome, page, size);
            }

    
    @GetMapping("/compareceu-hoje/{alunoId}")
    public String compareceuHoje(
            @PathVariable Long alunoId) {

        return frequenciaService.compareceuHoje(alunoId);
    }

    @GetMapping("/dias-sem-comparecer/{alunoId}")
    public long diasSemComparecer(
            @PathVariable Long alunoId) {

        return frequenciaService.diasSemComparecer(alunoId);
    }
}
