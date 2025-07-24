package br.com.sasaki.solution.agendamento.serviceTest;

import br.com.sasaki.solution.agendamento.config.TenantContext;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalRequestDTO;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.ProfissionalMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Especialidade;
import br.com.sasaki.solution.agendamento.model.Profissional;
import br.com.sasaki.solution.agendamento.repository.ProfissionalRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.especialidade.EspecialidadeService;
import br.com.sasaki.solution.agendamento.service.profissional.ProfissionalServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class ProfissionalServiceImplTest {

    @InjectMocks
    private ProfissionalServiceImpl profissionalService;

    @Mock
    private ProfissionalRepository repository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private EspecialidadeService especialidadeService;

    @Mock
    private ProfissionalMapper mapper;

    @Captor
    private ArgumentCaptor<Profissional> profissionalCaptor;

    private Cliente cliente;
    private Especialidade especialidade;
    private Profissional profissional;
    private ProfissionalRequestDTO requestDTO;
    private ProfissionalResponseDTO responseDTO;

    @BeforeEach
    void setup() {

        TenantContext.setCurrentTenant(1L);

        cliente = new Cliente();
        cliente.setId(1L);


        especialidade = new Especialidade();
        especialidade.setId(2L);
        especialidade.setNomeEspecialidade("Cardiologista");

        profissional = new Profissional();
        profissional.setId(10L);
        profissional.setNomeProfissional("Dra. Ana");
        profissional.setCrm("CRM123");
        profissional.setIdCliente(cliente);
        profissional.setIdEspecialidade(especialidade);

        requestDTO = new ProfissionalRequestDTO("Dra. Ana", "CRM123", 1L, 2L);
        responseDTO = new ProfissionalResponseDTO(10L, "Dra. Ana", "CRM123", 1L, 2L);
    }

    @Test
    void deveSalvarProfissionalComSucesso() {
        // Arrange
        when(repository.existsByCrmAndIdCliente_Id("CRM123", 1L)).thenReturn(false);
        when(mapper.toEntity(requestDTO)).thenReturn(profissional);
        when(clienteService.findClienteByIdOrThrow(1L)).thenReturn(cliente);
        when(especialidadeService.buscarEspecialidadePorId(2L)).thenReturn(especialidade);
        when(repository.save(any(Profissional.class))).thenReturn(profissional);
        when(mapper.toDTO(profissional)).thenReturn(responseDTO);

        // Act
        ProfissionalResponseDTO result = profissionalService.salvar(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Dra. Ana", result.nomeProfissional());
        assertEquals(2L, result.idEspecialidade());

        verify(repository).save(profissionalCaptor.capture());
        Profissional salvo = profissionalCaptor.getValue();
        assertEquals(cliente.getId(), salvo.getIdCliente().getId());
        assertEquals(especialidade.getId(), salvo.getIdEspecialidade().getId());
    }

    @Test
    void deveLancarExcecaoQuandoCrmDuplicado() {
        when(repository.existsByCrmAndIdCliente_Id("CRM123", 1L)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> profissionalService.salvar(requestDTO));

        assertEquals("Já existe um médico cadastrado com esse CRM", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void deveAtualizarProfissionalComSucesso() {

        // Arrange
        when(repository.findById(10L)).thenReturn(Optional.of(profissional)); // buscarProfissionalOuFalhar
        doNothing().when(mapper).updateEntityFromDTO(requestDTO, profissional); // update no mesmo objeto
        when(clienteService.findClienteByIdOrThrow(1L)).thenReturn(cliente);
        when(especialidadeService.buscarEspecialidadePorId(2L)).thenReturn(especialidade);
        when(repository.save(profissional)).thenReturn(profissional);
        when(mapper.toDTO(profissional)).thenReturn(responseDTO);

        // Act
        ProfissionalResponseDTO result = profissionalService.atualizar(10L, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Dra. Ana", result.nomeProfissional());
        assertEquals(2L, result.idEspecialidade());

        verify(mapper).updateEntityFromDTO(requestDTO, profissional);
        verify(repository).save(profissional);
        assertEquals(cliente.getId(), profissional.getIdCliente().getId());
        assertEquals(especialidade.getId(), profissional.getIdEspecialidade().getId());
    }

    @Test
    void deveLancarExcecaoAoAtualizarIdInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> profissionalService.atualizar(999L, requestDTO));

        assertEquals("Profissional não encontrado", ex.getMessage());
        verify(mapper, never()).updateEntityFromDTO(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void deveBuscarProfissionalPorIdComSucesso() {
        when(repository.findById(10L)).thenReturn(Optional.of(profissional));
        when(mapper.toDTO(profissional)).thenReturn(responseDTO);

        ProfissionalResponseDTO result = profissionalService.buscarPorId(10L);

        assertNotNull(result);
        assertEquals("Dra. Ana", result.nomeProfissional());
        verify(repository).findById(10L);
        verify(mapper).toDTO(profissional);
    }

    @Test
    void deveLancarExcecaoAoBuscarProfissionalPorIdInexistente() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> profissionalService.buscarPorId(999L));

        assertEquals("Profissional não encontrado", ex.getMessage());
    }

    @Test
    void deveListarTodosProfissionaisPaginado() {
        Page<Profissional> page = new PageImpl<>(Collections.singletonList(profissional));
        when(repository.findByIdCliente_Id(1L, Pageable.unpaged())).thenReturn(page);
        when(mapper.toDTO(any(Profissional.class))).thenReturn(responseDTO);

        Page<ProfissionalResponseDTO> result = profissionalService.listarTodos(Pageable.unpaged(), 1L);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Dra. Ana", result.getContent().get(0).nomeProfissional());
    }

    @Test
    void deveBuscarProfissionaisPorNome() {
        Page<Profissional> page = new PageImpl<>(Collections.singletonList(profissional));
        when(repository.findAllByNomeProfissionalContainingIgnoreCaseAndIdCliente_Id(eq("ana"), any(Pageable.class), eq(1L)))
                .thenReturn(page);
        when(mapper.toDTO(any(Profissional.class))).thenReturn(responseDTO);

        Page<ProfissionalResponseDTO> result = profissionalService.buscarPorNome("ana", Pageable.unpaged(), 1L);

        assertEquals(1, result.getTotalElements());
        assertEquals("Dra. Ana", result.getContent().get(0).nomeProfissional());
    }

    @Test
    void deveDeletarProfissionalComSucesso() {
        when(repository.existsById(10L)).thenReturn(true);

        profissionalService.deletar(10L);

        verify(repository).deleteById(10L);
    }

    @Test
    void deveLancarExcecaoAoDeletarProfissionalInexistente() {
        when(repository.existsById(999L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> profissionalService.deletar(999L));

        assertEquals("Profissional não encontrado", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }









}