package sistema.Academia_api.catraca;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import sistema.Academia_api.exception.RegraNegocioException;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ControlIdClientTest {
    MockRestServiceServer server;
    ControlIdClient client;

    @BeforeEach void setup() {
        var builder = RestClient.builder().baseUrl("http://catraca.test");
        server = MockRestServiceServer.bindTo(builder).build();
        client = new ControlIdClient(builder.build(), "admin", "teste");
    }
    void login(String session) {
        server.expect(requestTo("http://catraca.test/login.fcgi"))
                .andExpect(method(org.springframework.http.HttpMethod.POST))
                .andExpect(content().json("{\"login\":\"admin\",\"password\":\"teste\"}"))
                .andRespond(withSuccess("{\"session\":\"" + session + "\"}", MediaType.APPLICATION_JSON));
    }
    @Test void reutilizaSessaoEEnviaDirecoes() {
        login("abc");
        for (String direcao : new String[]{"clockwise", "anticlockwise", "both"}) {
            server.expect(requestTo("http://catraca.test/execute_actions.fcgi?session=abc"))
                    .andExpect(content().json("{\"actions\":[{\"action\":\"catra\",\"parameters\":\"allow=" + direcao + "\"}]}"))
                    .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
        }
        client.liberar("clockwise"); client.liberar("anticlockwise"); client.liberar("both");
        server.verify();
    }
    @Test void renovaSessaoUmaVez() {
        login("antiga");
        server.expect(requestTo("http://catraca.test/execute_actions.fcgi?session=antiga"))
                .andRespond(withStatus(HttpStatus.UNAUTHORIZED));
        login("nova");
        server.expect(requestTo("http://catraca.test/execute_actions.fcgi?session=nova"))
                .andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
        client.liberar("clockwise"); server.verify();
    }
    @Test void erroNoCorpoNaoIndicaSucesso() {
        login("abc");
        server.expect(requestTo("http://catraca.test/execute_actions.fcgi?session=abc"))
                .andRespond(withSuccess("{\"error\":\"Invalid action\",\"code\":1}", MediaType.APPLICATION_JSON));
        assertThrows(RegraNegocioException.class, () -> client.liberar("clockwise")); server.verify();
    }
    @Test void timeoutNaoRepeteAbertura() {
        login("abc");
        server.expect(requestTo("http://catraca.test/execute_actions.fcgi?session=abc"))
                .andRespond(withException(new java.net.SocketTimeoutException()));
        assertThrows(RegraNegocioException.class, () -> client.liberar("clockwise")); server.verify();
    }
    @Test void loginRecusadoNaoEnviaAcao() {
        server.expect(requestTo("http://catraca.test/login.fcgi"))
                .andRespond(withStatus(HttpStatus.FORBIDDEN));
        assertThrows(RegraNegocioException.class, () -> client.liberar("both")); server.verify();
    }
}
