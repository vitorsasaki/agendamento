package br.com.sasaki.solution.agendamento.service.paciente;

import br.com.sasaki.solution.agendamento.config.TenantContext;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.PacienteMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Paciente;
import br.com.sasaki.solution.agendamento.repository.ClienteRepository;
import br.com.sasaki.solution.agendamento.repository.PacienteRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final ClienteRepository clienteRepository;
    private final PacienteMapper pacienteMapper;
    private final ClienteService clienteService;

    @Override
    public PacienteResponseDTO criar(PacienteRequestDTO dto, Long idCliente) {
        validarCpfEEmail(dto.cpf(), dto.email(), idCliente);

        Paciente paciente = pacienteMapper.toEntity(dto);
        paciente.setIdCliente(clienteService.findClienteByIdOrThrow(idCliente));
        return pacienteMapper.toDTO(pacienteRepository.save(paciente));
    }

    @Override
    public Page<PacienteResponseDTO> listarTodos(Pageable pageable, Long idCliente) {
        return pacienteRepository.findByIdCliente_Id(idCliente, pageable).map(pacienteMapper::toDTO);
    }

    @Override
    public PacienteResponseDTO buscarPorId(Long id) {
        return pacienteMapper.toDTO(buscarPacienteOuFalhar(id));
    }

    @Override
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente existente = buscarPacienteOuFalhar(id);

        Cliente cliente = clienteService.findClienteByIdOrThrow(TenantContext.getCurrentTenant());
        validarCpfEEmail(dto.cpf(), dto.email(), id);

        pacienteMapper.updateEntityFromDTO(dto, existente);
        existente.setIdCliente(cliente);

        return pacienteMapper.toDTO(pacienteRepository.save(existente));
    }

    @Override
    public void deletar(Long id) {
        Paciente paciente = buscarPacienteOuFalhar(id);
        pacienteRepository.delete(paciente);
    }

    @Override
    public Page<PacienteResponseDTO> buscarPorNome(String nome, Long idCliente, Pageable pageable) {
        Cliente cliente = clienteService.findClienteByIdOrThrow(idCliente);
        return pacienteRepository.findByNomeContainingIgnoreCaseAndIdCliente_Id(nome, idCliente, pageable)
                .map(pacienteMapper::toDTO);
    }

    private void validarCpfEEmail(String cpf, String email, Long idCliente) {
        if (pacienteRepository.existsByCpfAndIdCliente_Id(cpf, idCliente)) {
            throw new BusinessException("Já existe um paciente com este CPF para este cliente.");
        }

        if (pacienteRepository.existsByEmailAndIdCliente_Id(email, idCliente)) {
            throw new BusinessException("Já existe um paciente com este e-mail para este cliente.");
        }
    }


    public Paciente buscarPacienteOuFalhar(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Paciente não encontrado com ID: " + id));
    }


}
