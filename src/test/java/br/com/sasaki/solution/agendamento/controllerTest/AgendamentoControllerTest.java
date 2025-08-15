package br.com.sasaki.solution.agendamento.controllerTest;


import br.com.sasaki.solution.agendamento.config.TenantContext;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import br.com.sasaki.solution.agendamento.model.Cliente;
import br.com.sasaki.solution.agendamento.model.Usuario;
import br.com.sasaki.solution.agendamento.repository.AgendamentoRepository;
import br.com.sasaki.solution.agendamento.repository.ClienteRepository;
import br.com.sasaki.solution.agendamento.repository.UsuarioRepository;
import br.com.sasaki.solution.agendamento.security.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AgendamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private Long idCliente;
    private AgendamentoRequestDTO requestDTO;
    private AgendamentoResponseDTO responseDTO;
    private String token;

    @BeforeEach
    void setup() {
        idCliente = 1L;
        TenantContext.setCurrentTenant(idCliente);
        agendamentoRepository.deleteAll(); // Limpa para garantir testes consistentes

        Cliente cliente = clienteRepository.save(new Cliente(null, "Clinica Teste", "27008086000155", "67991220268", "clinicateste@gmail.com"));

        Usuario usuario = Usuario.builder()
                .nome("Usuário Teste")
                .email("usuario@teste.com")
                .senha("senha123") // criptografar se necessário
                .idCliente(cliente)
                .build();

        usuarioRepository.save(usuario);

        token = jwtTokenProvider.generateToken(usuario);
    }

    @Test
    void deveCriarAgendamentoComSucesso() throws Exception {


        requestDTO = new AgendamentoRequestDTO(LocalDateTime.parse("2025-07-30T14:00:00"), 1L, 2L, "Primeira Consulta");

        mockMvc.perform(get("/api/agendamentos")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }


}