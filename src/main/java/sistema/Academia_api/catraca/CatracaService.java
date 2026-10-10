package sistema.Academia_api.catraca;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatracaService {
    private final ControlIdClient controlIdClient;

    public void liberarEntrada() { controlIdClient.liberar("clockwise"); }
    public void liberarSaida() { controlIdClient.liberar("anticlockwise"); }
    public void liberarAmbos() { controlIdClient.liberar("both"); }
}
