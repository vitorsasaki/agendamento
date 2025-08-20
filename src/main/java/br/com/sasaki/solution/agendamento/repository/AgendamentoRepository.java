package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Agendamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    Page<Agendamento> findByCliente_Id(Long idCliente, Pageable pageable);
    List<Agendamento> findByCliente_Id(Long idCliente);
    Page<Agendamento> findByPacienteNomeContainingIgnoreCaseAndCliente_Id(String nome, Long idCliente, Pageable pageable);

}
