package br.com.sasaki.solution.agendamento.security;

import br.com.sasaki.solution.agendamento.config.TenantContext;
import br.com.sasaki.solution.agendamento.model.Usuario;
import br.com.sasaki.solution.agendamento.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = getTokenFromRequest(request);

            if (token != null && tokenProvider.validateToken(token)) {
                String email = tokenProvider.getUsernameFromToken(token);
                Long idCliente = tokenProvider.getIdClienteFromToken(token); // Novo método

                Usuario usuario = usuarioRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());

                SecurityContextHolder.getContext().setAuthentication(authentication);
                TenantContext.setCurrentTenant(idCliente);
            }

            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear(); // Importante para não vazar contexto entre requisições
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return (bearerToken != null && bearerToken.startsWith("Bearer "))
                ? bearerToken.substring(7)
                : null;
    }
}
