package br.com.sasaki.solution.agendamento.controller;

import br.com.sasaki.solution.agendamento.dto.auth.AuthRequestDTO;
import br.com.sasaki.solution.agendamento.dto.auth.AuthResponseDTO;
import br.com.sasaki.solution.agendamento.service.auth.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO request) {
        return ResponseEntity.ok(authenticationService.autenticar(request));
    }
}
