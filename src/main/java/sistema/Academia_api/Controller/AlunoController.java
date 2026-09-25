package sistema.Academia_api.Controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import sistema.Academia_api.alunos.AlunoRequest;
import sistema.Academia_api.alunos.AlunoResponse;
import sistema.Academia_api.service.AlunoService;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping("/cadastrar")
    public AlunoResponse cadastrarAluno(@RequestBody @Valid AlunoRequest request) {
        return alunoService.cadastrarAluno(request);

    }

    @DeleteMapping("/deletar/{id}")
    public void deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
    }

    @PutMapping("/atualizar/{id}")
    public AlunoResponse atualizarAluno(@PathVariable Long id, @RequestBody @Valid AlunoRequest request) {
        return alunoService.atualizarAluno(id, request);
    }

    @PutMapping("/ativar/{id}")
    public AlunoResponse ativarAluno(@PathVariable Long id) {
        return alunoService.ativarAluno(id);
    }

    @PutMapping("/desativar/{id}")
    public AlunoResponse desativarAluno(@PathVariable Long id) {
        return alunoService.desativarAluno(id);
    }

   @GetMapping("/buscar/{nome}")
public Page<AlunoResponse> buscarAlunoPorNome(
        @PathVariable String nome,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {

    return alunoService.buscar(nome, page, size);
}

    @GetMapping
    public Page<AlunoResponse> listarAlunos(@RequestParam(defaultValue = "0")int page,
                                            @RequestParam(defaultValue = "20") int size) {
        return alunoService.listarAlunos(page,size);

    }

    @PostMapping("/{id}/foto")
    public AlunoResponse salvarFoto(@PathVariable Long id, @RequestParam("foto") MultipartFile foto
    ) throws IOException {

        return alunoService.salvarFotos(id, foto);
    }


}