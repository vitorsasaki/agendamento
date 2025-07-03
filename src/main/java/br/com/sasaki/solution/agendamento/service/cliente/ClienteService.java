package br.com.sasaki.solution.agendamento.service.cliente;

import br.com.sasaki.solution.agendamento.dto.cliente.ClienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.cliente.ClienteResponseDTO;
import br.com.sasaki.solution.agendamento.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponseDTO create(ClienteRequestDTO dto);
    ClienteResponseDTO findById(Long id);
    Page<ClienteResponseDTO> findAll(Pageable pageable);
    ClienteResponseDTO update(Long id, ClienteRequestDTO dto);
    void delete(Long id);
    Cliente findClienteByIdOrThrow(Long id);
}
