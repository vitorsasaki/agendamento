package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.dto.paciente.PacienteRequestDTO;
import br.com.sasaki.solution.agendamento.dto.paciente.PacienteResponseDTO;
import br.com.sasaki.solution.agendamento.service.paciente.PacienteService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criar(@Valid @RequestBody PacienteRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.criar(dto));
    }

    @GetMapping

    public ResponseEntity<Page<PacienteResponseDTO>> listar(Pageable pageable){
        return ResponseEntity.ok(pacienteService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pacienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody PacienteRequestDTO dto) {
        return ResponseEntity.ok(pacienteService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PacienteResponseDTO>> buscarPorNome(
            @RequestParam String nome,
            @RequestParam Long idCliente,
            Pageable pageable) {
        return ResponseEntity.ok(pacienteService.buscarPorNome(nome, idCliente, pageable));
    }


}
