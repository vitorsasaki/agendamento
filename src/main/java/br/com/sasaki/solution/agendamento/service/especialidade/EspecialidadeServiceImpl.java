package br.com.sasaki.solution.agendamento.service.especialidade;

import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeRequestDTO;
import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeResponseDTO;
import br.com.sasaki.solution.agendamento.exception.ResourceNotFoundException;
import br.com.sasaki.solution.agendamento.mapper.EspecialidadeMapper;
import br.com.sasaki.solution.agendamento.model.Especialidade;
import br.com.sasaki.solution.agendamento.repository.EspecialidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EspecialidadeServiceImpl implements EspecialidadeService{

    private final EspecialidadeRepository repository;
    private final EspecialidadeMapper mapper;

    @Override
    public EspecialidadeResponseDTO criar(EspecialidadeRequestDTO dto) {
        Especialidade entity = mapper.toEntity(dto);
        Especialidade salvo = repository.save(entity);
        return mapper.toDTO(salvo);
    }

    @Override
    public Page<EspecialidadeResponseDTO> listaTodas(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDTO);
    }

    @Override
    public EspecialidadeResponseDTO buscarPorId(Long id) {
        Especialidade entity = buscarEspecialidadePorId(id);
        return mapper.toDTO(entity);
    }

    @Override
    public EspecialidadeResponseDTO atualizar(Long id, EspecialidadeRequestDTO dto) {
        Especialidade entity = buscarEspecialidadePorId(id);

        mapper.updateEntityFromDTO(dto, entity);
        Especialidade atualizado = repository.save(entity);
        return mapper.toDTO(atualizado);
    }

    @Override
    public void deletar(Long id) {
        Especialidade entity = buscarEspecialidadePorId(id);
        repository.delete(entity);
    }

    private Especialidade buscarEspecialidadePorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Especialidade não encontrada com ID: " + id));
    }

}
