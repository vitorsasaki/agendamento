package br.com.sasaki.solution.agendamento.serviceTest;

import br.com.sasaki.solution.agendamento.dto.paciente.PacienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.PacienteMapper;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Paciente;
import br.com.sasaki.solution.agendamento.repository.PacienteRepository;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.paciente.PacienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class PacienteServiceImplTest {

    @InjectMocks
    private PacienteServiceImpl pacienteService;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PacienteMapper pacienteMapper;

    @Captor
    private ArgumentCaptor<Paciente> pacienteCaptor;

    private Cliente cliente;
    private Paciente paciente;
    private PacienteRequestDTO requestDTO;
    private PacienteResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        cliente = new Cliente();
        cliente.setId(1L);

        paciente = new Paciente();
        paciente.setId(10L);
        paciente.setCpf("12345678900");
        paciente.setEmail("teste@teste.com");

        requestDTO = new PacienteRequestDTO("Nome Teste", "12345678900", "teste@teste.com", "99999-9999", 2L);
        responseDTO = new PacienteResponseDTO(10L, "Nome Teste", "12345678900", "teste@teste.com", "99999-9999", 2L);
    }

    @Test
    void deveCriarPacienteComSucesso() {
        when(pacienteRepository.existsByCpfAndIdCliente_Id(anyString(), anyLong())).thenReturn(false);
        when(pacienteRepository.existsByEmailAndIdCliente_Id(anyString(), anyLong())).thenReturn(false);
        when(clienteService.findClienteByIdOrThrow(anyLong())).thenReturn(cliente);
        when(pacienteMapper.toEntity(requestDTO)).thenReturn(paciente);
        when(pacienteRepository.save(any(Paciente.class))).thenReturn(paciente);
        when(pacienteMapper.toDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO result = pacienteService.criar(requestDTO, cliente.getId());

        assertNotNull(result);
        assertEquals("Nome Teste", result.nome());
        verify(pacienteRepository).save(pacienteCaptor.capture());
        assertEquals(cliente.getId(), pacienteCaptor.getValue().getIdCliente().getId());
    }

    @Test
    void deveLancarExcecaoQuandoCpfExistente() {
        when(pacienteRepository.existsByCpfAndIdCliente_Id("12345678900", 1L)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> pacienteService.criar(requestDTO, 1L));

        assertEquals("Já existe um paciente com este CPF para este cliente.", ex.getMessage());
    }

    @Test
    void deveBuscarPacientePorId() {
        when(pacienteRepository.findById(10L)).thenReturn(Optional.of(paciente));
        when(pacienteMapper.toDTO(paciente)).thenReturn(responseDTO);

        PacienteResponseDTO result = pacienteService.buscarPorId(10L);

        assertNotNull(result);
        assertEquals(10L, result.id());
        verify(pacienteRepository).findById(10L);
    }

    @Test
    void deveListarPacientesPaginado() {
        Page<Paciente> page = new PageImpl<>(Collections.singletonList(paciente));
        when(pacienteRepository.findByIdCliente_Id(eq(1L), any(Pageable.class))).thenReturn(page);
        when(pacienteMapper.toDTO(any(Paciente.class))).thenReturn(responseDTO);

        Page<PacienteResponseDTO> result = pacienteService.listarTodos(Pageable.unpaged(), 1L);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(pacienteRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> pacienteService.buscarPorId(999L));

        assertEquals("Paciente não encontrado com ID: 999", ex.getMessage());
    }
}
