package br.com.sasaki.solution.agendamento.service.usuario;

import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioRequestDTO;
import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioResponseDTO;
import br.com.sasaki.solution.agendamento.model.Usuario;

import java.util.List;

public interface UsuarioService {
    UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto);
    UsuarioResponseDTO buscarPorId(Long id);
    List<UsuarioResponseDTO> listarTodos();
    void deletar(Long id);
    UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto);
}
