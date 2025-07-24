package br.com.sasaki.solution.agendamento.serviceTest;

import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioRequestDTO;
import br.com.sasaki.solution.agendamento.dto.usuario.UsuarioResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.UsuarioMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Usuario;
import br.com.sasaki.solution.agendamento.repository.UsuarioRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.usuario.UsuarioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UsuarioServiceImplTest {

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Usuario usuario;
    private Cliente cliente;
    private UsuarioRequestDTO requestDTO;
    private UsuarioResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);

        usuario = new Usuario();
        usuario.setId(10L);
        usuario.setNome("Marcela");
        usuario.setEmail("marcela@email.com");
        usuario.setSenha("senha123");
        usuario.setIdCliente(cliente);

        requestDTO = new UsuarioRequestDTO("Marcela", "marcela@email.com", "senha123", 1L);
        responseDTO = new UsuarioResponseDTO(10L, "Marcela", "marcela@email.com", "senha123", 1L);
    }

    @Test
    void deveCadastrarUsuarioComSucesso() {
        when(usuarioRepository.existsByEmail("marcela@email.com")).thenReturn(false);
        when(usuarioMapper.toEntity(requestDTO)).thenReturn(usuario);
        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(clienteService.findClienteByIdOrThrow(1L)).thenReturn(cliente);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO result = usuarioService.cadastrar(requestDTO);

        assertNotNull(result);
        assertEquals("Marcela", result.nome());
        verify(usuarioRepository).save(usuario);
        assertEquals(cliente.getId(), usuario.getIdCliente().getId());
    }

    @Test
    void deveLancarExcecaoSeEmailJaExiste() {
        when(usuarioRepository.existsByEmail("marcela@email.com")).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> usuarioService.cadastrar(requestDTO));

        assertEquals("Já existe um usuário com este e-mail.", ex.getMessage());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveBuscarUsuarioPorId() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO result = usuarioService.buscarPorId(10L);

        assertNotNull(result);
        assertEquals("Marcela", result.nome());
    }


    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> usuarioService.buscarPorId(999L));

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveListarTodosUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(usuarioMapper.toDTO(usuario)).thenReturn(responseDTO);

        List<UsuarioResponseDTO> result = usuarioService.listarTodos();

        assertEquals(1, result.size());
        assertEquals("Marcela", result.get(0).nome());
    }

    @Test
    void deveAtualizarUsuarioComSucesso() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode("senha123")).thenReturn("senha_criptografada");
        when(clienteService.findClienteByIdOrThrow(1L)).thenReturn(cliente);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);
        when(usuarioMapper.toDTO(usuario)).thenReturn(responseDTO);

        UsuarioResponseDTO result = usuarioService.atualizar(10L, requestDTO);

        assertNotNull(result);
        assertEquals("Marcela", result.nome());
        assertEquals("senha_criptografada", usuario.getSenha());
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuario));

        usuarioService.deletar(10L);

        verify(usuarioRepository).deleteById(10L);
    }

    @Test
    void deveLancarExcecaoAoDeletarUsuarioInexistente() {
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> usuarioService.deletar(999L));

        assertEquals("Usuário não encontrado.", ex.getMessage());
        verify(usuarioRepository, never()).deleteById(any());
    }


}