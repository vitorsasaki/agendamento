package br.com.sasaki.solution.agendamento.service.auth;

import br.com.sasaki.solution.agendamento.dto.auth.AuthRequestDTO;
import br.com.sasaki.solution.agendamento.dto.auth.AuthResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.model.Usuario;
import br.com.sasaki.solution.agendamento.repository.UsuarioRepository;
import br.com.sasaki.solution.agendamento.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponseDTO autenticar(AuthRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new BusinessException("Email ou senha inválidos");
        }

        String token = jwtTokenProvider.generateToken(usuario);
        return new AuthResponseDTO(token, "Bearer", usuario.getNome());
    }
}
