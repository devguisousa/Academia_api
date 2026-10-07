package sistema.Academia_api.SegurityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.ServiceSegurity.JwtAuthenticationFilter;

@Configuration
@RequiredArgsConstructor 
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                    
                    .requestMatchers("/auth/**").permitAll()

                    .requestMatchers(HttpMethod.POST, "/usuarios").hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/alunos")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.GET, "/alunos/buscar/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/alunos")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.POST, "/alunos/*/foto")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/alunos/*")
                    .hasAnyRole("ADMIN", "RECEPCAO")

                    .requestMatchers(HttpMethod.PUT, "/alunos/*/ativar")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.PUT, "/alunos/*/desativar")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.DELETE, "/alunos/*")
                    .hasRole("ADMIN")

                    .requestMatchers(HttpMethod.GET, "/alunos/perfil/**")
                    .hasAnyRole("ADMIN", "RECEPCAO")
                    
                    .requestMatchers(HttpMethod.GET, "/alunos/dashboard/*/*")
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

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean 

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}