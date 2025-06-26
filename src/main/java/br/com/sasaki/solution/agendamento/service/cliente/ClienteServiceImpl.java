package br.com.sasaki.solution.agendamento.service.cliente;

import br.com.sasaki.solution.agendamento.dto.cliente.ClienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.cliente.ClienteResponseDTO;
import br.com.sasaki.solution.agendamento.exception.ResourceNotFoundException;
import br.com.sasaki.solution.agendamento.mapper.ClienteMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository repository;

    @Override
    public ClienteResponseDTO create(ClienteRequestDTO dto) {
        validarCnpjEEmail(dto);
        Cliente cliente = ClienteMapper.toEntity(dto);
        Cliente saved = repository.save(cliente);
        return ClienteMapper.toResponseDTO(saved);
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = findClienteByIdOrThrow(id);
        return ClienteMapper.toResponseDTO(cliente);
    }

    @Override
    public Page<ClienteResponseDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ClienteMapper::toResponseDTO);
    }

    @Override
    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto) {
        Cliente cliente = findClienteByIdOrThrow(id);
        ClienteMapper.updateEntity(cliente, dto);
        Cliente updated = repository.save(cliente);
        return ClienteMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        Cliente cliente = findClienteByIdOrThrow(id);
        repository.delete(cliente);
    }

    // --- Métodos privados de apoio ---

    private Cliente findClienteByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    private void validarCnpjEEmail(ClienteRequestDTO dto) {
        if (repository.existsByCpfCnpj(dto.cpfCnpj())) {
            throw new IllegalArgumentException("Já existe um cliente com este CNPJ.");
        }
        if (repository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Já existe um cliente com este e-mail.");
        }
    }
}
