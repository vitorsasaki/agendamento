package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.cliente.ClienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.cliente.ClienteResponseDTO;
import br.com.sasaki.solution.agendamento.model.Cliente;

public class ClienteMapper {

    public static Cliente toEntity(ClienteRequestDTO dto) {
        return Cliente.builder()
                .nome(dto.nome())
                .cpfCnpj(dto.cpfCnpj())
                .telefone(dto.telefone())
                .email(dto.email())
                .build();
    }

    public static ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpfCnpj(),
                cliente.getTelefone(),
                cliente.getEmail()
        );
    }

    public static void updateEntity(Cliente cliente, ClienteRequestDTO dto) {
        cliente.setNome(dto.nome());
        cliente.setCpfCnpj(dto.cpfCnpj());
        cliente.setTelefone(dto.telefone());
        cliente.setEmail(dto.email());
    }
}
