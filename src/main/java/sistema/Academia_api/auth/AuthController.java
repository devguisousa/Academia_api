package sistema.Academia_api.auth;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import sistema.Academia_api.user.login.LoginRequest;
import sistema.Academia_api.user.login.LoginResponse;
import sistema.Academia_api.user.login.RefreshRequest;

@RestController 
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping ("/refresh")
    public LoginResponse refresh(@RequestBody @Valid RefreshRequest request) {
        return authService.refresh(request);
    }
  
}
