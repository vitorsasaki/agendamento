package br.com.sasaki.solution.agendamento.service.agendamento;

import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AgendamentoService {

    AgendamentoResponseDTO criar(AgendamentoRequestDTO agendamentoRequestDTO, Long idCliente);
    Page<AgendamentoResponseDTO> listarTodos(Pageable pageable, Long idCliente);
    void deletar(Long id );
    AgendamentoResponseDTO atualizar(Long id, AgendamentoRequestDTO dto, Long idCliente);
    Page<AgendamentoResponseDTO> buscarPorPaciente(String nome, Long idCliente, Pageable pageable);
}
