package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Profissional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    Page<Profissional> findAllByNomeProfissionalContainingIgnoreCase(String nomeProfissional, Pageable pageable);
    boolean existsByCrm(String crm);

}
