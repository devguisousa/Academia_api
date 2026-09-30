package sistema.Academia_api.service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.alunos.Aluno;
import sistema.Academia_api.alunos.AlunoRequest;
import sistema.Academia_api.alunos.AlunoResponse;
import sistema.Academia_api.exception.AlunoException;
import sistema.Academia_api.exception.CpfJaCadastrado;
import sistema.Academia_api.exception.EmailJaCadastrado;
import sistema.Academia_api.repository.AlunoRepository;

@Service
@RequiredArgsConstructor  
public class AlunoService {

private final AlunoRepository alunoRepository; 


    

private Aluno converterAluno (AlunoRequest request) {
    Aluno aluno = new Aluno();
    aluno.setNome(request.getNome());
    aluno.setEmail(request.getEmail());
    aluno.setPinCatraca(request.getPinCatraca());
    aluno.setDataNascimento(request.getDataNascimento());
    aluno.setCpf(request.getCpf());
    aluno.setTelefone(request.getTelefone());
    aluno.setSexo(request.getSexo());
    aluno.setEndereco(request.getEndereco());
    return aluno;
}

private  AlunoResponse converterAlunoResponse (Aluno aluno) {
    AlunoResponse response = new AlunoResponse();
    response.setId(aluno.getId());
    response.setNome(aluno.getNome());
    response.setEmail(aluno.getEmail());
    response.setDataNascimento(aluno.getDataNascimento());
    response.setCpf(aluno.getCpf());
    response.setTelefone(aluno.getTelefone());
    response.setSexo(aluno.getSexo());
    response.setEndereco(aluno.getEndereco());
    response.setAtivo(aluno.getAtivo());
    response.setFotoURL(aluno.getFotoURL());
    return response;
}

private Aluno salvarAluno(Aluno aluno) {
        return alunoRepository.save(aluno);
    }


public AlunoResponse cadastrarAluno(AlunoRequest request) {

Aluno aluno = converterAluno(request);

if (alunoRepository.existsByCpf(aluno.getCpf())) {
    throw new RuntimeException("CPF já cadastrado");
} 
if(alunoRepository.existsByEmail(aluno.getEmail())) {
    throw new EmailJaCadastrado("Email já cadastrado");
}

Aluno alunoSalvo = salvarAluno(aluno);
return converterAlunoResponse(alunoSalvo);

}

    public Page<AlunoResponse> buscar(String nome, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome, pageable);

        if (alunos.isEmpty()) {
            throw new AlunoException("Aluno não encontrado");
        }

         return alunos.map(this::converterAlunoResponse);
    }

public void deletarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));

    alunoRepository.delete(aluno);
}

public AlunoResponse atualizarAluno(Long id, AlunoRequest request) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));

    if (!aluno.getCpf().equals(request.getCpf())) {
        throw new CpfJaCadastrado("CPF não pode ser alterado");
    }

    if (alunoRepository.existsByEmail(request.getEmail()) && !aluno.getEmail().equals(request.getEmail())) {
        throw new EmailJaCadastrado("Email já cadastrado");
    }

    aluno.setNome(request.getNome());
    aluno.setEmail(request.getEmail());
    aluno.setPinCatraca(request.getPinCatraca());
    aluno.setDataNascimento(request.getDataNascimento());
    aluno.setTelefone(request.getTelefone());
    aluno.setSexo(request.getSexo());
    aluno.setEndereco(request.getEndereco());
    Aluno alunoSalvo = salvarAluno(aluno);
    return converterAlunoResponse(alunoSalvo);

}     

public AlunoResponse ativarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    aluno.setAtivo(true);
    Aluno alunoSalvo = salvarAluno(aluno);
    return converterAlunoResponse(alunoSalvo);
}

public AlunoResponse desativarAluno(Long id) {
    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    aluno.setAtivo(false);
    Aluno alunoSalvo = salvarAluno(aluno);
    return converterAlunoResponse(alunoSalvo);

}

public Page<AlunoResponse> listarAlunos(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);

    Page<Aluno> alunos = alunoRepository.findAll(pageable);

    return alunos.map(this::converterAlunoResponse);

}




public AlunoResponse salvarFotos(Long id, MultipartFile foto) throws IOException {

    Aluno aluno = alunoRepository.findById(id).orElseThrow(() -> new AlunoException("Aluno não encontrado"));
    Path pasta = Paths.get("uploads/alunos");

    Files.createDirectories(pasta);

    String fotosAlunos = id + "-" + foto.getOriginalFilename();
    Path caminhoFoto = pasta.resolve(fotosAlunos);
    Files.write(caminhoFoto, foto.getBytes());
    aluno.setFotoURL("/uploads/alunos/" + fotosAlunos);
    Aluno alunoSalvo = salvarAluno(aluno);
    return converterAlunoResponse(alunoSalvo);
}
}