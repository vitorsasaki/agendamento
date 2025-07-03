package br.com.sasaki.solution.agendamento.service.profissional;

import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalRequestDTO;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.ProfissionalMapper;
import br.com.sasaki.solution.agendamento.model.Profissional;
import br.com.sasaki.solution.agendamento.repository.ClienteRepository;
import br.com.sasaki.solution.agendamento.repository.EspecialidadeRepository;
import br.com.sasaki.solution.agendamento.repository.ProfissionalRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.especialidade.EspecialidadeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfissionalServiceImpl implements ProfissionalService {

    private final ProfissionalRepository repository;
    private final ClienteRepository clienteRepository;
    private final EspecialidadeRepository especialidadeRepository;
    private final ProfissionalMapper mapper;
    private final ClienteService clienteService;
    private final EspecialidadeService especialidadeService;

    @Override
    public ProfissionalResponseDTO salvar(ProfissionalRequestDTO dto) {
        verificaSeExisteCrmCadastrado(dto.crm());
        Profissional profissional = mapper.toEntity(dto);
        profissional.setIdCliente(clienteService.findClienteByIdOrThrow(dto.idCliente()));
        profissional.setIdEspecialidade(especialidadeService.buscarEspecialidadePorId(dto.idEspecialidade()));

        return mapper.toDTO(repository.save(profissional));
    }

    @Override
    public Page<ProfissionalResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public ProfissionalResponseDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
    }

    @Override
    public Page<ProfissionalResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return repository.findAllByNomeProfissionalContainingIgnoreCase(nome, pageable)
                .map(mapper::toDTO);
    }

    @Override
    public ProfissionalResponseDTO atualizar(Long id, ProfissionalRequestDTO dto) {
        var profissional = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional não encontrado"));
        mapper.updateEntityFromDTO(dto, profissional);
        profissional.setIdCliente(clienteService.findClienteByIdOrThrow(dto.idCliente()));
        profissional.setIdEspecialidade(especialidadeService.buscarEspecialidadePorId(dto.idEspecialidade()));
        return mapper.toDTO(repository.save(profissional));
    }

    @Override
    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Profissional não encontrado");
        }
        repository.deleteById(id);
    }

    private void verificaSeExisteCrmCadastrado(String crm){
        if(repository.existsByCrm(crm)){
            throw new BusinessException("Já existe um médico cadastrado com esse CRM");
        }
    }

}
