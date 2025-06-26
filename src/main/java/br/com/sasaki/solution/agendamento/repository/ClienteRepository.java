package br.com.sasaki.solution.agendamento.repository;

import br.com.sasaki.solution.agendamento.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpfCnpj(String cpfCnpj);
    boolean existsByEmail(String email);
}
