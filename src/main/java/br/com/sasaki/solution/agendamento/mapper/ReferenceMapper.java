package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Especialidade;
import org.springframework.stereotype.Component;

@Component
public class ReferenceMapper {

    public Cliente toCliente(Long id) {
        if (id == null) return null;
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }

    public Especialidade toEspecialidade(Long id) {
        if (id == null) return null;
        Especialidade especialidade = new Especialidade();
        especialidade.setId(id);
        return especialidade;
    }

    public Long fromCliente(Cliente cliente) {
        return cliente != null ? cliente.getId() : null;
    }

    public Long fromEspecialidade(Especialidade especialidade) {
        return especialidade != null ? especialidade.getId() : null;
    }
}
