package br.com.sasaki.solution.agendamento.service.paciente;

import br.com.sasaki.solution.agendamento.dto.paciente.PacienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PacienteService {
    PacienteResponseDTO criar(PacienteRequestDTO dto);
    Page<PacienteResponseDTO> listarTodos(Pageable pageable);
    PacienteResponseDTO buscarPorId(Long id);
    PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto);
    void deletar(Long id);
    Page<PacienteResponseDTO> buscarPorNome(String nome, Long idCliente, Pageable pageable);
}
