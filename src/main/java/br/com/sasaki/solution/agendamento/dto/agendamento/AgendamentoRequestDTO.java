package br.com.sasaki.solution.agendamento.dto.agendamento;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoRequestDTO(
        @NotNull
        LocalDateTime dataHora,
        @NotNull
        Long idPaciente,
        @NotNull
        Long idProfissional,
        String observacao,
        String status

) {
}
