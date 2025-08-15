package br.com.sasaki.solution.agendamento.dto.profissional;

public record ProfissionalResponseDTO(
        Long id,
        String nomeProfissional,
        String crm,
        Long idCliente,
        String especialidade
) {
}
