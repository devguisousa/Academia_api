# Academia API

API REST para gerenciar uma academia: alunos, planos, matrículas, mensalidades, pagamentos, frequência e o financeiro (receitas e despesas). Feita com Java 21 e Spring Boot, com autenticação JWT e dois perfis de usuário.

Este repositório tem só o back end. Não existe interface, então tudo é consumido direto pela API.

## O que ela faz

- **Alunos**: cadastro, busca por nome, ativar/desativar, foto e um perfil que junta plano, matrícula e última presença.
- **Planos**: nome, valor e duração em dias.
- **Matrículas**: ligam um aluno a um plano. O número da matrícula é gerado automaticamente (8 dígitos, sem repetir).
- **Mensalidades**: nascem como `PENDENTE` e podem virar `PAGO`, `ATRASADO` ou `CANCELADO`.
- **Pagamentos**: PIX, dinheiro, cartão de crédito ou débito.
- **Frequência**: registra a entrada do aluno, diz se ele veio hoje e há quantos dias não aparece.
- **Lançamentos**: receitas e despesas avulsas, com resumo por período, mês ou ano.
- **Dashboard** (só admin): alunos ativos, mensalidades por status, receitas, despesas e saldo do mês, e quantos alunos vieram hoje.

## Tecnologias

- Java 21
- Spring Boot 4.1.1 (Web MVC, Data JPA, Security, Validation)
- PostgreSQL 17
- JWT com `java-jwt` e senhas com BCrypt
- Lombok
- Docker e Docker Compose
- JUnit 5 e Mockito

## Como rodar

### Com Docker (mais fácil)

Precisa de Docker e Docker Compose.

```bash
git clone https://github.com/devguisousa/Academia_api.git
cd Academia_api
cp .env.exemple .env
```

Abra o `.env` e preencha os valores (veja a tabela abaixo). Depois:

```bash
docker compose up --build
```

A API sobe em `http://localhost:8080`. O Postgres fica exposto na porta `5433` do seu computador, caso queira conectar por fora.

Na primeira subida, se ainda não existir nenhum usuário `ADMIN`, a API cria um com os dados de `ADMIN_NOME`, `ADMIN_EMAIL` e `ADMIN_PASSWORD`. Se essas variáveis estiverem vazias, ela só avisa no log e segue sem criar.

### Sem Docker

Precisa de Java 21 e de um PostgreSQL rodando. Além das variáveis do `.env`, adicione a URL do banco, porque o `application.properties` lê `DB_URL`:

```
DB_URL=jdbc:postgresql://localhost:5432/academia_db
```

Se quiser só o banco no Docker e a API na sua máquina, suba apenas o serviço do Postgres (`docker compose up postgres -d`) e use a porta `5433` na URL.

```bash
./mvnw spring-boot:run
```

Rode o comando na raiz do projeto, porque o `.env` é lido a partir dali.

### Variáveis de ambiente

| Variável | Para que serve |
|---|---|
| `DB_USER` | Usuário do PostgreSQL |
| `DB_PASSWORD` | Senha do PostgreSQL |
| `JWT_SECRET` | Chave usada para assinar os tokens. Use algo longo e aleatório |
| `ADMIN_NOME` | Nome do admin criado na primeira subida |
| `ADMIN_EMAIL` | Email (login) desse admin |
| `ADMIN_PASSWORD` | Senha desse admin |
| `DB_URL` | URL JDBC do banco. Só precisa definir fora do Docker, o Compose já passa esse valor |

O `.env` está no `.gitignore`. Não suba esse arquivo.

## Autenticação e permissões

O login devolve dois tokens:

- `acessToken`, válido por 30 minutos, usado nas requisições
- `refreshToken`, válido por 20 dias, usado só para pedir um novo access token

```bash
# login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "admin@exemplo.com", "senha": "sua-senha"}'

# usando o token
curl http://localhost:8080/alunos \
  -H "Authorization: Bearer SEU_ACESS_TOKEN"

# renovando o access token
curl -X POST http://localhost:8080/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken": "SEU_REFRESH_TOKEN"}'
```

Existem dois perfis:

| Perfil | O que pode fazer |
|---|---|
| `ADMIN` | Tudo |
| `RECEPCAO` | Cadastrar e editar alunos (inclusive foto), ver perfil, criar matrículas e mensalidades, registrar pagamentos e frequência, e consultar planos, matrículas e mensalidades |

Ficam só com o admin: criar usuários, ativar/desativar/excluir aluno, dashboard, alterar, ativar ou desativar matrícula, alterar ou cancelar mensalidade, criar e alterar planos, e todo o módulo de lançamentos.

Para criar um usuário novo (só admin), envie `POST /usuarios` com `nome`, `email`, `senha` (mínimo 5 caracteres) e `role` (`ADMIN` ou `RECEPCAO`).

## Endpoints

As listagens são paginadas com `page` e `size`.

| Recurso | Rotas |
|---|---|
| Auth | `POST /auth/login`, `POST /auth/refresh` |
| Usuários | `POST /usuarios` |
| Alunos | `POST /alunos`, `GET /alunos`, `GET /alunos/buscar/{nome}`, `GET /alunos/perfil/{id}`, `PUT /alunos/{id}`, `PUT /alunos/{id}/ativar`, `PUT /alunos/{id}/desativar`, `DELETE /alunos/{id}`, `POST /alunos/{id}/foto` (multipart, campo `foto`), `GET /alunos/dashboard/{ano}/{mes}` |
| Planos | `POST /planos`, `GET /planos`, `GET /planos/buscar/{id}`, `PUT /planos/{id}`, `PUT /planos/{id}/ativar`, `PUT /planos/{id}/desativar` |
| Matrículas | `POST /matriculas`, `GET /matriculas`, `GET /matriculas/buscar/{id}`, `PUT /matriculas/{id}` (troca o plano), `PUT /matriculas/{id}/ativar`, `PUT /matriculas/{id}/desativar` |
| Mensalidades | `POST /mensalidades`, `GET /mensalidades`, `GET /mensalidades/buscar/{id}`, `PUT /mensalidades/{id}`, `PUT /mensalidades/cancelar/{id}` |
| Pagamentos | `POST /pagamentos/cadastrar`, `GET /pagamentos/listar`, `GET /pagamentos/buscar/{id}` |
| Frequências | `POST /frequencias`, `GET /frequencias`, `GET /frequencias/buscar/{nome}`, `GET /frequencias/compareceu-hoje/{alunoId}`, `GET /frequencias/dias-sem-comparecer/{alunoId}` |
| Lançamentos | `POST /lancamentos`, `GET /lancamentos`, `GET /lancamentos/buscar/{descricao}`, `PUT /lancamentos/{id}`, `DELETE /lancamentos/{id}`, `GET /lancamentos/periodo?inicio=&fim=`, `GET /lancamentos/resumo/periodo?inicio=&fim=`, `GET /lancamentos/resumo/mensal?ano=&mes=`, `GET /lancamentos/resumo/anual?ano=` |

Exemplo de pagamento de uma mensalidade:

```bash
curl -X POST http://localhost:8080/pagamentos/cadastrar \
  -H "Authorization: Bearer SEU_ACESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"mensalidadeId": 1, "valor": 120.00, "formaDePagamento": "PIX"}'
```

Formas de pagamento aceitas: `PIX`, `DINHEIRO`, `CARTAO_CREDITO`, `CARTAO_DEBITO`.

## Regras de negócio

- Um aluno só pode ter uma matrícula ativa por vez.
- Não dá para matricular aluno desativado nem usar plano desativado (vale também ao reativar uma matrícula).
- O fim da matrícula é a data de início mais a duração do plano, em dias.
- CPF e email do aluno não podem se repetir.
- Só é possível criar mensalidade para uma matrícula ativa.
- O valor do pagamento tem que ser igual ao da mensalidade. Pagamento parcial não existe.
- Mensalidade já paga ou cancelada não aceita novo pagamento. Mensalidade paga não pode ser alterada nem cancelada.
- Todo dia à meia-noite, uma tarefa agendada marca como `ATRASADO` as mensalidades `PENDENTE` com vencimento anterior a hoje.

Os erros voltam em JSON com `status`, `erro` e `data`: `409` para regra de negócio, `404` para recurso não encontrado, `401` para login ou token inválido e `400` para dados inválidos (com a lista dos campos que falharam).

## Testes

Os testes unitários usam JUnit 5 e Mockito, com um arquivo por service (aluno, plano, matrícula, mensalidade, pagamento, frequência, lançamento, auth e usuário). Eles não sobem o Spring nem precisam de banco.

```bash
./mvnw -Dtest='sistema.Academia_api.unit.*Test' test
```

O `./mvnw test` sem filtro também roda o teste de contexto do Spring, que precisa das variáveis de ambiente e de um banco disponível.

## Estrutura

```
src/main/java/sistema/Academia_api
├── Controller        # rotas HTTP
├── service           # regras de negócio
├── repository        # acesso ao banco (Spring Data JPA)
├── SegurityConfig    # regras de acesso e criação do admin inicial
├── ServiceSegurity   # geração e validação dos tokens, filtro JWT
├── auth              # login e refresh
├── exception         # exceções e tratamento global de erros
└── alunos, Planos, Matricula, mensalidade, pagamentos,
    frequencia, lancamentos, user    # entidades e DTOs de cada módulo
```

## Limitações e próximos passos

- O aluno tem o campo `pinCatraca`, mas ainda não existe integração com catraca.
- Registrar um pagamento não cria um lançamento de receita sozinho. Hoje receitas e despesas do dashboard vêm só dos lançamentos cadastrados.
- O refresh token não é rotacionado nem pode ser revogado: o mesmo token vale até expirar.
- Alguns erros ainda caem no `500` genérico (por exemplo CPF repetido e mensalidade inexistente) porque lançam `RuntimeException` em vez de uma exceção própria.
- O banco é criado pelo Hibernate (`ddl-auto=update`), sem migrations.
- Faltam testes de integração e documentação interativa (Swagger/OpenAPI).
