package sistema.Academia_api.catraca;

import java.util.List;

final class ControlIdDtos {
    private ControlIdDtos() {}

    record LoginRequest(String login, String password) {
        @Override public String toString() { return "LoginRequest[credenciais omitidas]"; }
    }
    record LoginResponse(String session) {
        @Override public String toString() { return "LoginResponse[session omitida]"; }
    }
    record Action(String action, String parameters) {}
    record ActionsRequest(List<Action> actions) {}
    record ActionResponse(String error, Integer code) {}
}
