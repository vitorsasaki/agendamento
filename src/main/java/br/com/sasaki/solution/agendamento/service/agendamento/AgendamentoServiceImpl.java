package br.com.sasaki.solution.agendamento.service.agendamento;

import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.AgendamentoMapper;
import br.com.sasaki.solution.agendamento.model.Agendamento;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Paciente;
import br.com.sasaki.solution.agendamento.model.Profissional;
import br.com.sasaki.solution.agendamento.repository.AgendamentoRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.paciente.PacienteServiceImpl;
import br.com.sasaki.solution.agendamento.service.profissional.ProfissionalServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgendamentoServiceImpl implements AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final PacienteServiceImpl pacienteService;
    private final ProfissionalServiceImpl profissionalService;
    private final ClienteService clienteService;
    private final AgendamentoMapper agendamentoMapper;


    @Override
    @Transactional
    public AgendamentoResponseDTO criar(AgendamentoRequestDTO agendamentoRequestDTO, Long idCliente) {
        Agendamento agendamento = populaAgendamento(agendamentoRequestDTO, idCliente, null, new Agendamento());
        return agendamentoMapper.toDTO(agendamentoRepository.save(agendamento));
    }

    @Override
    public Page<AgendamentoResponseDTO> listarTodos(Pageable pageable, Long idCliente) {
        return agendamentoRepository.findByCliente_Id(idCliente, pageable)
                .map(agendamentoMapper::toDTO);
    }

    @Override
    public List<AgendamentoResponseDTO> listarTodos(Long idCliente) {
        return agendamentoRepository.findByCliente_Id(idCliente)
                .stream()
                .map(agendamentoMapper:: toDTO)
                .toList();
    }

    @Override
    public void deletar(Long id) {
        Agendamento agendamento = buscarAgendamento(id);
        agendamentoRepository.delete(agendamento);

    }

    @Override
    @Transactional
    public AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto, Long idCliente) {
        Agendamento agendamento = buscarAgendamento(id);
        agendamento = populaAgendamento(dto, idCliente, agendamento.getId(), agendamento);
        return agendamentoMapper.toDTO(agendamentoRepository.save(agendamento));

    }

    @Override
    public Page<AgendamentoResponseDTO> buscarPorPaciente(String nome, Long idCliente, Pageable pageable) {
        return agendamentoRepository.findByPacienteNomeContainingIgnoreCaseAndCliente_Id(nome, idCliente, pageable)
                .map(agendamentoMapper::toDTO);
    }

    public Agendamento buscarAgendamento(Long id){
        return agendamentoRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Agendamento não encontrado"));
    }

    public Agendamento populaAgendamento(AgendamentoRequestDTO agendamentoRequestDTO, Long idCliente, Long idAgendamento, Agendamento agendamento){
        Paciente paciente = pacienteService.buscarPacienteOuFalhar(agendamentoRequestDTO.idPaciente());
        Profissional profissional = profissionalService.buscarProfissionalOuFalhar(agendamentoRequestDTO.idProfissional());
        Cliente cliente = clienteService.findClienteByIdOrThrow(idCliente);
        if(idAgendamento == null)
            agendamento = agendamentoMapper.toEntity(agendamentoRequestDTO);
        else
            agendamentoMapper.updateEntityFromDTO(agendamentoRequestDTO, agendamento);

        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setCliente(cliente);

        return agendamento;
    }


}
