package br.com.sasaki.solution.agendamento.mapper;

import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioRequestDTO;
import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioResponseDTO;
import br.com.sasaki.solution.agendamento.model.Usuario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ReferenceMapper.class)
public interface UsuarioMapper extends BaseMapper<Usuario, UsuarioRequestDTO, UsuarioResponseDTO> {
}
