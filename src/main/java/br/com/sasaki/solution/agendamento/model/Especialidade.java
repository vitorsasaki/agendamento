package br.com.sasaki.solution.agendamento.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidade")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Especialidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, name = "nome_especialidade")
    private String nomeEspecialidade;
}
