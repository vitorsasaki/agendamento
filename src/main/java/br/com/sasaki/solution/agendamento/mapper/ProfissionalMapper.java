package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalRequestDTO;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalResponseDTO;
import br.com.sasaki.solution.agendamento.model.Profissional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface ProfissionalMapper extends BaseMapper<Profissional, ProfissionalRequestDTO, ProfissionalResponseDTO>{

    @Override
    @Mapping(source = "idEspecialidade.nomeEspecialidade", target = "especialidade")
    ProfissionalResponseDTO toDTO(Profissional entity);
}
