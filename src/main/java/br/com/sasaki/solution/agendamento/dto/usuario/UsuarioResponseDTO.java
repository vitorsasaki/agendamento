package br.com.sasaki.solution.agendamento.dto.usuario;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        String senha,
        Long idCliente
) {
}
