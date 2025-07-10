package br.com.sasaki.solution.agendamento.service.usuario;

import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioRequestDTO;
import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.UsuarioMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Usuario;
import br.com.sasaki.solution.agendamento.repository.ClienteRepository;
import br.com.sasaki.solution.agendamento.repository.UsuarioRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;
    private final ClienteService clienteService;


    @Override
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        verificarEmail(dto.email());
        Usuario usuario = usuarioMapper.toEntity(dto);
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setIdCliente(clienteService.findClienteByIdOrThrow(dto.idCliente()));
        return usuarioMapper.toDTO(usuarioRepository.save(usuario));

    }

    @Override
    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = buscarUsuario(id);
        return usuarioMapper.toDTO(usuario);
    }

    @Override
    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletar(Long id) {
        Usuario usuario = buscarUsuario(id);
        usuarioRepository.deleteById(usuario.getId());
    }

    @Override
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        Usuario usuario = buscarUsuario(id);

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setIdCliente(clienteService.findClienteByIdOrThrow(dto.idCliente()));

        return usuarioMapper.toDTO(usuarioRepository.save(usuario));
    }


    private void verificarEmail(String email){
        if (usuarioRepository.existsByEmail(email)) {
            throw new BusinessException("Já existe um usuário com este e-mail.");
        }

    }

    public Usuario buscarUsuario(Long id){
       Usuario usuario = usuarioRepository.findById(id)
               .orElseThrow(() -> new BusinessException("Usuário não encontrado."));
       return usuario;

    }


}
