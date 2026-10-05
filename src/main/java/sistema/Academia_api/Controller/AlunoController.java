package sistema.Academia_api.Controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import sistema.Academia_api.alunos.AlunoRequest;
import sistema.Academia_api.alunos.AlunoResponse;
import sistema.Academia_api.alunos.DashboardResponse;
import sistema.Academia_api.alunos.PerfilAlunoResponse;
import sistema.Academia_api.service.AlunoService;


@RestController
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @PostMapping
    public AlunoResponse cadastrarAluno(@RequestBody @Valid AlunoRequest request) {
        return alunoService.cadastrarAluno(request);

    }

    @DeleteMapping("/{id}")
    public void deletarAluno(@PathVariable Long id) {
        alunoService.deletarAluno(id);
    }

    @PutMapping("/{id}")
    public AlunoResponse atualizarAluno(@PathVariable Long id, @RequestBody @Valid AlunoRequest request) {
        return alunoService.atualizarAluno(id, request);
    }

    @PutMapping("/{id}/ativar")
    public AlunoResponse ativarAluno(@PathVariable Long id) {
        return alunoService.ativarAluno(id);
    }

    @PutMapping("/{id}/desativar")
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


    @GetMapping ("/perfil/{id}") 
        public PerfilAlunoResponse perfil(@PathVariable Long id) {
            return alunoService.perfil(id);
        }

    

    @GetMapping ("/dashboard/{ano}/{mes}") 
        public DashboardResponse dashboard (@PathVariable int mes, @PathVariable int ano) {
            return alunoService.dashboar(ano, mes);

        }
    
}
