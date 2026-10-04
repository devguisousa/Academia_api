package sistema.Academia_api.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sistema.Academia_api.ServiceSegurity.UserService;
import sistema.Academia_api.user.UsuarioRequest;
import sistema.Academia_api.user.UsuarioResponse;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;

    @PostMapping
    public UsuarioResponse cadastrar(
            @RequestBody @Valid UsuarioRequest request) {

        return userService.cadastrar(request);
    }
}