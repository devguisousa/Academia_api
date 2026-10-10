package sistema.Academia_api.Controller;

import java.security.Principal;
import java.time.LocalDateTime;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sistema.Academia_api.catraca.*;

@RestController
@RequestMapping("/api/catraca")
@RequiredArgsConstructor
@Slf4j
public class CatracaController {
    private final CatracaService catracaService;
    private final LeitorService leitorService;

    // A autorização ADMIN/RECEPCAO é aplicada pelo SecurityFilterChain existente.
    @PostMapping("/manual/entrada")
    public ResponseEntity<Void> entrada(Principal usuario) {
        catracaService.liberarEntrada();
        registrar(usuario, "ENTRADA");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/manual/saida")
    public ResponseEntity<Void> saida(Principal usuario) {
        catracaService.liberarSaida();
        registrar(usuario, "SAIDA");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/manual/ambos")
    public ResponseEntity<Void> ambos(Principal usuario) {
        catracaService.liberarAmbos();
        registrar(usuario, "AMBOS");
        return ResponseEntity.noContent().build();
    }

    // Simulação administrativa: não é o callback definitivo do leitor Control iD.
    @PostMapping("/identificacao")
    public Long identificar(@Valid @RequestBody IdentificacaoLeitorRequest request) {
        return leitorService.identificar(request);
    }

    private void registrar(Principal usuario, String direcao) {
        log.info("Liberação manual enviada usuario={} data={} direcao={}",
                usuario.getName(), LocalDateTime.now(), direcao);
    }
}
