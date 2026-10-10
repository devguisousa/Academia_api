package sistema.Academia_api.catraca;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import sistema.Academia_api.Controller.CatracaController;
import sistema.Academia_api.SegurityConfig.SecurityConfig;
import sistema.Academia_api.ServiceSegurity.JwtAuthenticationFilter;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(CatracaSecurityTest.Config.class)
class CatracaSecurityTest {
    @Configuration @EnableWebMvc @org.springframework.security.config.annotation.web.configuration.EnableWebSecurity @Import({SecurityConfig.class, CatracaController.class})
    static class Config {
        @Bean CatracaService catracaService() { return mock(CatracaService.class); }
        @Bean LeitorService leitorService() { return mock(LeitorService.class); }
        @Bean JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
            JwtAuthenticationFilter filter = mock(JwtAuthenticationFilter.class);
            doAnswer(inv -> {
                ((FilterChain) inv.getArgument(2)).doFilter(
                        (ServletRequest) inv.getArgument(0), (ServletResponse) inv.getArgument(1));
                return null;
            }).when(filter).doFilter(any(), any(), any());
            return filter;
        }
    }
    @Autowired WebApplicationContext context;
    @Autowired CatracaService catraca;
    @Autowired LeitorService leitor;
    MockMvc mvc;
    @BeforeEach void setup() {
        reset(catraca, leitor);
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }
    @Test void permiteAdminERecepcaoEmTodasDirecoes() throws Exception {
        for (String role : new String[]{"ADMIN", "RECEPCAO"}) {
            for (String direcao : new String[]{"entrada", "saida", "ambos"}) {
                mvc.perform(post("/api/catraca/manual/" + direcao).with(user("operador").roles(role)))
                        .andExpect(status().isNoContent());
            }
        }
        verify(catraca, times(2)).liberarEntrada();
        verify(catraca, times(2)).liberarSaida();
        verify(catraca, times(2)).liberarAmbos();
        verifyNoInteractions(leitor);
    }
    @Test void bloqueiaAnonimoEPerfilSemPermissao() throws Exception {
        for (String direcao : new String[]{"entrada", "saida", "ambos"}) {
            mvc.perform(post("/api/catraca/manual/" + direcao)).andExpect(status().is4xxClientError());
            mvc.perform(post("/api/catraca/manual/" + direcao).with(user("aluno").roles("ALUNO")))
                    .andExpect(status().isForbidden());
        }
        verifyNoInteractions(catraca, leitor);
    }
}
