package br.com.sasaki.solution.agendamento.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClienteRequestDTO(
        @NotBlank(message = "Nome é obrigatório.")
        String nome,

        @NotBlank(message = "CNPJ é obrigatório.")
        String cpfCnpj,

        @NotBlank(message = "Telefone é obrigatório.")
        String telefone,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "Formato de e-mail inválido.")
        String email
) {
}
