package br.com.sasaki.solution.agendamento.service.profissional;

import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalRequestDTO;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProfissionalService {

    ProfissionalResponseDTO salvar(ProfissionalRequestDTO dto);

    Page<ProfissionalResponseDTO> listarTodos(Pageable pageable);

    ProfissionalResponseDTO buscarPorId(Long id);

    Page<ProfissionalResponseDTO> buscarPorNome(String nomeProfissional, Pageable pageable);

    ProfissionalResponseDTO atualizar(Long id, ProfissionalRequestDTO dto);

    void deletar(Long id);
}
