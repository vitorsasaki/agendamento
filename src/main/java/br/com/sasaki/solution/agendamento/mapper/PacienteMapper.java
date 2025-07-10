package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.paciente.PacienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteResponseDTO;
import br.com.sasaki.solution.agendamento.model.Paciente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface PacienteMapper extends BaseMapper<Paciente, PacienteRequestDTO, PacienteResponseDTO> {
}
