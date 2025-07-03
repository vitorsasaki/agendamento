package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalRequestDTO;
import br.com.sasaki.solution.agendamento.dto.profissional.ProfissionalResponseDTO;
import br.com.sasaki.solution.agendamento.service.profissional.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profissionais")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalResponseDTO> criar(@RequestBody @Valid ProfissionalRequestDTO dto) {
        ProfissionalResponseDTO response = profissionalService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProfissionalResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(profissionalService.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(profissionalService.buscarPorId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProfissionalResponseDTO>> buscarPorNome(@RequestParam String nome, Pageable pageable) {
        return ResponseEntity.ok(profissionalService.buscarPorNome(nome, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDTO> atualizar(@PathVariable Long id,
                                                             @RequestBody @Valid ProfissionalRequestDTO dto) {
        return ResponseEntity.ok(profissionalService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        profissionalService.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
