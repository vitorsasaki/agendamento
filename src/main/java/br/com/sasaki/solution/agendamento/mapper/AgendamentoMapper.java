package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import br.com.sasaki.solution.agendamento.model.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface AgendamentoMapper extends BaseMapper<Agendamento, AgendamentoRequestDTO, AgendamentoResponseDTO> {

    @Override
    @Mapping(target = "nomePaciente", source = "paciente.nome")
    @Mapping(target = "nomeProfissional", source = "profissional.nomeProfissional")
    AgendamentoResponseDTO toDTO(Agendamento entity);
}
