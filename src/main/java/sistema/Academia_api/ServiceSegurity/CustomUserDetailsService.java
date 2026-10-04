package sistema.Academia_api.ServiceSegurity;


import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.repository.UsuarioRepository;
import sistema.Academia_api.user.Usuario;

@Service 
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository userRepository;
    
    @Override 
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Usuário não encontrado"));

                         return User
                         .withUsername(usuario.getEmail())
                         .password(usuario.getSenha())
                         .roles(usuario.getRole().name())
                         .build();


    }
}
