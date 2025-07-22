package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByCpfAndIdCliente_Id(String cpf, Long idCliente);

    boolean existsByEmailAndIdCliente_Id(String email, Long idCliente);

    Optional<Paciente> findByIdAndIdCliente_Id(Long id, Long idCliente);

    Page<Paciente> findByNomeContainingIgnoreCaseAndIdCliente_Id(String nome, Long idCliente, Pageable pageable);

    Page<Paciente> findByIdCliente_Id(Long idCliente, Pageable pageable);
}
