package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.config.TenantContext;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoRequestDTO;
import br.com.sasaki.solution.agendamento.dto.agendamento.AgendamentoResponseDTO;
import br.com.sasaki.solution.agendamento.service.agendamento.AgendamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> criar(@RequestBody @Valid AgendamentoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoService.criar(dto, TenantContext.getCurrentTenant()));
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDTO>> listar(Pageable pageable){
        return ResponseEntity.ok(agendamentoService.listarTodos(pageable, TenantContext.getCurrentTenant()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AgendamentoRequestDTO dto){
        return ResponseEntity.ok(agendamentoService.atualizar(id, dto, TenantContext.getCurrentTenant()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id){
        agendamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarPorNome")
    public ResponseEntity<Page<AgendamentoResponseDTO>> buscarPorNome(@RequestParam String nome, Pageable pageable){
        return ResponseEntity.ok(agendamentoService.buscarPorPaciente(nome, TenantContext.getCurrentTenant(), pageable));
    }
}
