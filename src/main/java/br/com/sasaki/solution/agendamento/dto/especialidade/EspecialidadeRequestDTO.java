package br.com.sasaki.solution.agendamento.dto.especialidade;

import jakarta.validation.constraints.NotBlank;

public record EspecialidadeRequestDTO(
        @NotBlank(message = "O nome do especialista é obrigatório")
        String nomeEspecialidade
) {
}
