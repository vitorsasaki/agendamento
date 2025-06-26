package br.com.sasaki.solution.agendamento.dto.cliente;

public record ClienteResponseDTO(
        Long id,
        String nome,
        String cpfCnpj,
        String telefone,
        String email
) {
}
