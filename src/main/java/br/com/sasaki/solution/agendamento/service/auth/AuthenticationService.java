package br.com.sasaki.solution.agendamento.service.auth;

import br.com.sasaki.solution.agendamento.dto.auth.AuthRequestDTO;
import br.com.sasaki.solution.agendamento.dto.auth.AuthResponseDTO;

public interface AuthenticationService {
    AuthResponseDTO autenticar(AuthRequestDTO request);
}
