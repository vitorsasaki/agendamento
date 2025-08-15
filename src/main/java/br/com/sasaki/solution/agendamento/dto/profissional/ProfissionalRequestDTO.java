package br.com.sasaki.solution.agendamento.dto.profissional;

import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

public record ProfissionalRequestDTO(
        String nomeProfissional,
        String crm,
        Long idCliente,
        Long idEspecialidade
) {
}
