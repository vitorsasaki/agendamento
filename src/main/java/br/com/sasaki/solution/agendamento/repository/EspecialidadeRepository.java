package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Especialidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {
}
