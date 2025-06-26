package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeRequestDTO;
import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeResponseDTO;
import br.com.sasaki.solution.agendamento.model.Especialidade;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EspecialidadeMapper extends BaseMapper<Especialidade, EspecialidadeRequestDTO, EspecialidadeResponseDTO>{

}
