package sistema.Academia_api.ServiceSegurity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sistema.Academia_api.repository.UserRepository;
import sistema.Academia_api.user.User;
import sistema.Academia_api.user.UserRequest;
import sistema.Academia_api.user.UserResponse;

@Service 
@RequiredArgsConstructor 
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private User converterUser (UserRequest request) {
        User user = new User();

        user.setEmail(request.getEmail());
        user.setNome(request.getNome());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setRole(request.getRole());

        return user;
    }

    private UserResponse converterUserResponse(User user) {
        UserResponse userResponse = new UserResponse();

        userResponse.setId(user.getId());
        userResponse.setNome(user.getNome());
        userResponse.setEmail(user.getEmail());
        userResponse.setRole(user.getRole());

        return userResponse;
    }

    private User salvaUser (User user) {
        return userRepository.save(user);
    }

    public  UserResponse cadastrar (UserRequest request) {
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("email ja cadastrado");
            
        }
        
        User user = converterUser(request);
        User userSalvo = salvaUser(user);
        return converterUserResponse(userSalvo);
    }
    
}
