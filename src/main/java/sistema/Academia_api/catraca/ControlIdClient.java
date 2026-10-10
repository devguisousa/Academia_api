package sistema.Academia_api.catraca;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import sistema.Academia_api.exception.RegraNegocioException;

@Component
public class ControlIdClient {
    private final RestClient http;
    private final String usuario;
    private final String senha;
    private String session;

    @org.springframework.beans.factory.annotation.Autowired
    public ControlIdClient(@Value("${catraca.url}") String url,
                           @Value("${catraca.usuario}") String usuario,
                           @Value("${catraca.senha}") String senha,
                           @Value("${catraca.timeout:5s}") Duration timeout) {
        this.usuario = usuario;
        this.senha = senha;
        var factory = new JdkClientHttpRequestFactory(HttpClient.newBuilder()
                .connectTimeout(timeout).build());
        factory.setReadTimeout(timeout);
        this.http = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }

    ControlIdClient(RestClient http, String usuario, String senha) {
        this.http = http;
        this.usuario = usuario;
        this.senha = senha;
    }

    // Serializa login e comandos para evitar renovação concorrente da sessão.
    public synchronized void liberar(String direcao) {
        if (!List.of("clockwise", "anticlockwise", "both").contains(direcao)) {
            throw new RegraNegocioException("Direção de catraca inválida");
        }
        try {
            if (session == null) login();
            for (int tentativa = 0; tentativa < 2; tentativa++) {
                try {
                    var response = http.post()
                            .uri(builder -> builder.path("/execute_actions.fcgi")
                                    .queryParam("session", "{session}").build(session))
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(new ControlIdDtos.ActionsRequest(List.of(
                                    new ControlIdDtos.Action("catra", "allow=" + direcao))))
                            .retrieve().body(ControlIdDtos.ActionResponse.class);
                    if (response != null && response.error() != null) {
                        if (sessaoInvalida(response.error()) && tentativa == 0) {
                            session = null;
                            login();
                            continue;
                        }
                        if (sessaoInvalida(response.error())) session = null;
                        throw new RegraNegocioException("Control iD recusou o comando de liberação");
                    }
                    return;
                } catch (RestClientResponseException e) {
                    if (e.getStatusCode().value() == 401) {
                        session = null;
                        if (tentativa == 0) { login(); continue; }
                        throw new RegraNegocioException("Sessão Control iD inválida após renovação");
                    }
                    throw e;
                }
            }
        } catch (ResourceAccessException e) {
            // Não repetir comandos após timeout: a passagem pode já ter sido liberada.
            throw new RegraNegocioException("Catraca indisponível ou timeout de comunicação");
        } catch (RestClientResponseException e) {
            throw new RegraNegocioException("Erro HTTP da Control iD: " + e.getStatusCode().value());
        } catch (RestClientException e) {
            throw new RegraNegocioException("Resposta inválida ou falha de comunicação com a Control iD");
        }
    }

    private void login() {
        if (senha == null || senha.isBlank()) {
            throw new RegraNegocioException("Configure CATRACA_SENHA para acessar a Control iD");
        }
        ControlIdDtos.LoginResponse response;
        try {
            response = http.post().uri("/login.fcgi")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ControlIdDtos.LoginRequest(usuario, senha))
                    .retrieve().body(ControlIdDtos.LoginResponse.class);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                throw new RegraNegocioException("Autenticação na Control iD recusada");
            }
            throw e;
        }
        if (response == null || response.session() == null || response.session().isBlank()) {
            throw new RegraNegocioException("Autenticação na Control iD não retornou uma sessão válida");
        }
        session = response.session();
    }

    private boolean sessaoInvalida(String error) {
        String mensagem = error.toLowerCase(Locale.ROOT);
        return mensagem.contains("session") &&
                (mensagem.contains("invalid") || mensagem.contains("expired"));
    }
}
