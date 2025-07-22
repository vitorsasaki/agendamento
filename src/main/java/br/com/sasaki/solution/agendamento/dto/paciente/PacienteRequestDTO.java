package br.com.sasaki.solution.agendamento.dto.paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PacienteRequestDTO(
        @NotBlank
        String nome,
        @NotBlank @CPF
        String cpf,
        @NotBlank @Email
        String email,
        @NotBlank
        String telefone,

        Long idCliente
) {
}
