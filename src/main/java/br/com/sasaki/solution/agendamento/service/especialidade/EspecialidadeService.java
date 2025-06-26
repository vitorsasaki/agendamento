package br.com.sasaki.solution.agendamento.service.especialidade;

import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeRequestDTO;
import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EspecialidadeService {

    EspecialidadeResponseDTO criar(EspecialidadeRequestDTO dto);

    Page<EspecialidadeResponseDTO> listaTodas(Pageable pageable);

    EspecialidadeResponseDTO buscarPorId(Long id);

    EspecialidadeResponseDTO atualizar(Long id, EspecialidadeRequestDTO dto);

    void deletar(Long id);
}
