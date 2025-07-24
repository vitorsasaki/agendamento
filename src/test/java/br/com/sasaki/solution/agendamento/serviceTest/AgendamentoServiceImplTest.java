package br.com.sasaki.solution.agendamento.serviceTest;

import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import br.com.sasaki.solution.agendamento.exception.BusinessException;
import br.com.sasaki.solution.agendamento.mapper.AgendamentoMapper;
import br.com.sasaki.solution.agendamento.model.Agendamento;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Paciente;
import br.com.sasaki.solution.agendamento.model.Profissional;
import br.com.sasaki.solution.agendamento.repository.AgendamentoRepository;
import br.com.sasaki.solution.agendamento.service.agendamento.AgendamentoServiceImpl;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import br.com.sasaki.solution.agendamento.service.paciente.PacienteServiceImpl;
import br.com.sasaki.solution.agendamento.service.profissional.ProfissionalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AgendamentoServiceImplTest {

    @InjectMocks
    private AgendamentoServiceImpl agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private PacienteServiceImpl pacienteService;

    @Mock
    private ProfissionalServiceImpl profissionalService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private AgendamentoMapper agendamentoMapper;

    private Agendamento agendamento;
    private AgendamentoRequestDTO requestDTO;
    private AgendamentoResponseDTO responseDTO;
    private Paciente paciente;
    private Profissional profissional;
    private Cliente cliente;

    @BeforeEach
    void setup() {
        paciente = new Paciente();
        paciente.setId(1L);

        profissional = new Profissional();
        profissional.setId(2L);

        cliente = new Cliente();
        cliente.setId(3L);

        agendamento = new Agendamento();
        agendamento.setId(10L);
        agendamento.setPaciente(paciente);
        agendamento.setProfissional(profissional);
        agendamento.setCliente(cliente);

        requestDTO = new AgendamentoRequestDTO(LocalDateTime.parse("2025-07-30T14:00:00"), 1L, 2L, "Primeira Consulta");
        responseDTO = new AgendamentoResponseDTO(10L, LocalDateTime.parse("2025-07-30T14:00:00"), "Paciente Teste", "Dr. Fulano","Primeira Consulta");
    }

    @Test
    void deveCriarAgendamentoComSucesso() {
        when(pacienteService.buscarPacienteOuFalhar(1L)).thenReturn(paciente);
        when(profissionalService.buscarProfissionalOuFalhar(2L)).thenReturn(profissional);
        when(clienteService.findClienteByIdOrThrow(3L)).thenReturn(cliente);
        when(agendamentoMapper.toEntity(requestDTO)).thenReturn(agendamento);
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(agendamento)).thenReturn(responseDTO);

        AgendamentoResponseDTO result = agendamentoService.criar(requestDTO, 3L);

        assertNotNull(result);
        assertEquals("Paciente Teste", result.nomePaciente());
        assertEquals("Dr. Fulano", result.nomeProfissional());

        verify(agendamentoRepository, times(1)).save(agendamento);
        verify(agendamentoMapper, times(1)).toDTO(agendamento);
    }

    @Test
    void deveAtualizarAgendamentoComSucesso() {
        LocalDateTime novaData = LocalDateTime.of(2025, 8, 1, 10, 30);
        AgendamentoRequestDTO novoDTO = new AgendamentoRequestDTO(novaData,1L, 2L, "");

        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(agendamento));
        when(pacienteService.buscarPacienteOuFalhar(1L)).thenReturn(paciente);
        when(profissionalService.buscarProfissionalOuFalhar(2L)).thenReturn(profissional);
        when(clienteService.findClienteByIdOrThrow(3L)).thenReturn(cliente);
        doNothing().when(agendamentoMapper).updateEntityFromDTO(novoDTO, agendamento);
        when(agendamentoRepository.save(agendamento)).thenReturn(agendamento);
        when(agendamentoMapper.toDTO(agendamento)).thenReturn(responseDTO);

        AgendamentoResponseDTO result = agendamentoService.atualizar(10L, novoDTO, 3L);

        assertNotNull(result);
        assertEquals("Paciente Teste", result.nomePaciente());
        assertEquals("Dr. Fulano", result.nomeProfissional());
        verify(agendamentoRepository, times(1)).save(agendamento);
    }

    @Test
    void deveListarTodosAgendamentosComSucesso() {
        Page<Agendamento> page = new PageImpl<>(List.of(agendamento));
        when(agendamentoRepository.findByCliente_Id(3L, Pageable.unpaged())).thenReturn(page);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(responseDTO);

        Page<AgendamentoResponseDTO> result = agendamentoService.listarTodos(Pageable.unpaged(), 3L);

        assertEquals(1, result.getTotalElements());
        assertEquals("Paciente Teste", result.getContent().get(0).nomePaciente());
    }

    @Test
    void deveBuscarAgendamentosPorNomePaciente() {
        Page<Agendamento> page = new PageImpl<>(List.of(agendamento));
        when(agendamentoRepository.findByPacienteNomeContainingIgnoreCaseAndCliente_Id("ana", 3L, Pageable.unpaged()))
                .thenReturn(page);
        when(agendamentoMapper.toDTO(any(Agendamento.class))).thenReturn(responseDTO);

        Page<AgendamentoResponseDTO> result = agendamentoService.buscarPorPaciente("ana", 3L, Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals("Paciente Teste", result.getContent().get(0).nomePaciente());
    }


    @Test
    void deveDeletarAgendamentoComSucesso() {
        when(agendamentoRepository.findById(10L)).thenReturn(Optional.of(agendamento));

        agendamentoService.deletar(10L);

        verify(agendamentoRepository).delete(agendamento);
    }

    @Test
    void deveLancarExcecaoAoBuscarAgendamentoInexistente() {
        when(agendamentoRepository.findById(999L)).thenReturn(Optional.empty());

        BusinessException ex = assertThrows(BusinessException.class,
                () -> agendamentoService.buscarAgendamento(999L));

        assertEquals("Agendamento não encontrado", ex.getMessage());
    }



}