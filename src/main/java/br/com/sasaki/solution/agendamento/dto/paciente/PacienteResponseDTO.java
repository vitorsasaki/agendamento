package br.com.sasaki.solution.agendamento.dto.paciente;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        Long idCliente
) {
}
