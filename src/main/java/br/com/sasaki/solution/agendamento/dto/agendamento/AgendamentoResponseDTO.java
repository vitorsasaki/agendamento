package br.com.sasaki.solution.agendamento.dto.agendamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoResponseDTO(
        Long id,
        LocalDateTime dataHora,
        String nomePaciente,
        String nomeProfissional,
        String observacao,
        String status

) {
}
