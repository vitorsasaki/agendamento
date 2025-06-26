package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.dto.cliente.ClienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.cliente.ClienteResponseDTO;
import br.com.sasaki.solution.agendamento.service.cliente.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve retornar cliente por ID com sucesso")
    void deveRetornarClientePorId() throws Exception {
        ClienteResponseDTO dto = new ClienteResponseDTO(
                1L,
                "Clínica B",
                "12345678000100",
                "11999999999",
                "clinica@b.com"
        );

        when(clienteService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Clínica B"))
                .andExpect(jsonPath("$.cpfCnpj").value("12345678000100"))
                .andExpect(jsonPath("$.email").value("clinica@b.com"));
    }

    @Test
    @DisplayName("Deve criar cliente com sucesso")
    void deveCriarClienteComSucesso() throws Exception {
        ClienteRequestDTO request = new ClienteRequestDTO(
                "Clínica C",
                "11122233344455",
                "11988887777",
                "contato@clinicac.com"
        );

        ClienteResponseDTO response = new ClienteResponseDTO(
                1L,
                request.nome(),
                request.cpfCnpj(),
                request.telefone(),
                request.email()
        );

        when(clienteService.create(any(ClienteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Clínica C"))
                .andExpect(jsonPath("$.cpfCnpj").value("11122233344455"))
                .andExpect(jsonPath("$.email").value("contato@clinicac.com"));
    }
}
