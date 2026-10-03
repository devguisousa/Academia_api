package sistema.Academia_api.SegurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                    
                    .requestMatchers("/auth/login").permitAll()

                    .requestMatchers(HttpMethod.GET, "/alunos")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.GET, "/alunos/buscar/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/alunos/cadastrar")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/alunos/*/foto")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/alunos/alterar/*")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/alunos/*/ativar")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/alunos/*/desativar")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/alunos/deletar/*")
                    .hasRole("ADMIN")

                  

                    .requestMatchers(HttpMethod.GET, "/matriculas/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/matriculas")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/matriculas/*")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/matriculas/*/ativar")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/matriculas/*/desativar")
                    .hasRole("ADMIN")

                   

                    .requestMatchers(HttpMethod.GET, "/mensalidades/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/mensalidades")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/mensalidades/*")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/mensalidades/cancelar/*")
                    .hasRole("ADMIN")

                   

                    .requestMatchers("/pagamentos/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                   
                    .requestMatchers("/frequencias/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")


                    .requestMatchers("/lancamentos/**")
                    .hasRole("ADMIN")

                
                    .requestMatchers(HttpMethod.GET, "/planos/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/planos/**")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/planos/**")
                    .hasRole("ADMIN")

                    .anyRequest().authenticated())

                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean 

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}