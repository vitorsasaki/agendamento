package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeRequestDTO;
import br.com.sasaki.solution.agendamento.dto.especialidade.EspecialidadeResponseDTO;
import br.com.sasaki.solution.agendamento.service.especialidade.EspecialidadeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/especilidade")
@RequiredArgsConstructor
public class EspecialidadeController {

    private final EspecialidadeService especialidadeService;

    @PostMapping
    public ResponseEntity<EspecialidadeResponseDTO> criar(@RequestBody @Valid EspecialidadeRequestDTO dto) {
        EspecialidadeResponseDTO response = especialidadeService.criar(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<EspecialidadeResponseDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "nomeEspecialidade") Pageable pageable) {
        return ResponseEntity.ok(especialidadeService.listaTodas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EspecialidadeResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid EspecialidadeRequestDTO dto) {
        return ResponseEntity.ok(especialidadeService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        especialidadeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
