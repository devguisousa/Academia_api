package sistema.Academia_api.catraca;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IdentificacaoLeitorRequest(@NotBlank String identificador,
                                        @NotNull TipoIdentificacao tipo) {}
