# Testes unitários

JUnit Jupiter e Mockito, com `MockitoExtension`, `@Mock` e `@InjectMocks`.
As dependências já são fornecidas pelo `pom.xml` existente.
Os testes usam repositórios e dependências simulados, sem iniciar Spring ou acessar PostgreSQL.

Execute apenas esta suíte na raiz do projeto:

```bash
./mvnw -Dtest='sistema.Academia_api.unit.*Test' test
```

São 51 casos em nove classes: AlunoService, PlanoService, MatriculaService,
MensalidadeService, PagamentoService, FrequenciaService, LancamentoService,
AuthService e UserService. Incluem fluxos de sucesso, rejeições por regras de
negócio, conversão de respostas e verificação de operações nos mocks.
Não representam cobertura completa de todos os métodos da aplicação.

O teste original `AcademiaApiApplicationTests` foi preservado. Ele inicia o
contexto Spring e pode precisar das configurações e do banco da aplicação;
o comando acima seleciona somente os novos testes unitários.

## Validação

Os 51 casos passaram em uma cópia temporária do projeto, com zero falhas e erros.
A execução no projeto original foi bloqueada por um erro preexistente na primeira
linha de `src/main/java/sistema/Academia_api/exception/CredenciaisInvalidasException.java`:
`ppackage` em vez de `package`. Essa correção foi feita somente na cópia temporária.
Nenhum arquivo de produção ou configuração foi alterado.
