package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    Page<Profissional> findAllByNomeProfissionalContainingIgnoreCaseAndIdCliente_Id(String nomeProfissional, Pageable pageable, Long idCliente);
    boolean existsByCrm(String crm);
    boolean existsByCrmAndIdCliente_Id(String crm, Long idCliente);
    Page<Profissional> findByIdCliente_Id(Long idCliente, Pageable pageable);

}
