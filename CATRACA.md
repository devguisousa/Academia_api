# Integração Control iD

Configure CATRACA_URL (padrão http://192.168.1.235), CATRACA_USUARIO (admin),
CATRACA_SENHA (obrigatória para comandos) e CATRACA_TIMEOUT (5s).
O login ocorre sob demanda; iniciar a aplicação não envia comandos ao equipamento.
Nunca coloque credenciais reais no repositório.

Endpoints com JWT existente e roles ADMIN ou RECEPCAO:
- POST /api/catraca/manual/entrada
- POST /api/catraca/manual/saida
- POST /api/catraca/manual/ambos

Não precisam de corpo nem identificação de aluno. Retornam 204 quando o equipamento
aceita o comando. Logs SLF4J registram usuário, horário e direção; isso não confirma giro.
Todas as liberações usam CatracaService -> ControlIdClient.

POST /api/catraca/identificacao é uma simulação administrativa protegida, não o callback
real do equipamento. Corpo: {"identificador":"123456","tipo":"PIN"}.
Utiliza Aluno.pinCatraca existente. PIN duplicado é rejeitado, pois a coluna não é única.
Verifica aluno ativo, matrícula ativa e vigente, plano ativo. Não existe política atual
de bloqueio de acesso por mensalidade/pagamento; nenhuma política financeira foi criada.
Erros reutilizam as exceptions e o GlobalExceptionHandler existentes (404/409).

TODOs:
- Mapear identificadores reais de cartão, biometria e senha. Hoje esses tipos são negados.
- Configurar e autenticar callbacks reais do leitor conforme modelo/firmware.
- Correlacionar e deduplicar eventos de giro antes de chamar
  LeitorService.registrarPassagemConfirmada, que reutiliza FrequenciaService.cadastrar.
  Não existe endpoint público para registro de eventos; abertura não registra presença.
- Validar fisicamente login, expiração da sessão, direções e liberação de uma passagem.
  Sessão é reutilizada e renovada uma vez após 401 ou erro explícito de sessão inválida.
  Timeout/offline não repetem comando, pois o equipamento pode já ter executado a ação.

Sem mudanças no banco, entidades, JWT ou dependências. A única regra de segurança
adicionada é ADMIN/RECEPCAO para /api/catraca/**. Nenhuma configuração permanente
ou bloqueio após abertura é enviado ao equipamento.

Testes sem equipamento: ./mvnw -Dtest='ControlIdClientTest,LeitorServiceTest,CatracaSecurityTest' test
Referências:
- https://www.controlid.com.br/docs/access-api-pt/gerenciamento-secao/fazer-login/
- https://www.controlid.com.br/docs/access-api-pt/acoes/abertura-remota-porta-e-catraca/
