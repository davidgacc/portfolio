package com.example.portfolio.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "projeto")
public class Projeto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate dataInicio;
    private LocalDate dataPrevisaoFim;
    private LocalDate dataFim;
    private String descricao;

    public enum Status {
        EM_ANALISE, ANALISE_REALIZADA, ANALISE_APROVADA, INICIADO, PLANEJADO, EM_ANDAMENTO, ENCERRADO, CANCELADO
    }

    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private Status status;
    private Float orcamento;

    private Long idGerente;

    public enum Risco {
        BAIXO,
        MEDIO,
        ALTO
    }

    @Enumerated(EnumType.STRING)
    @JsonProperty("risco")
    private Risco risco;


    @ManyToMany
    @JoinTable(
            name = "membros",
            joinColumns = @JoinColumn(name = "idprojeto"),
            inverseJoinColumns = @JoinColumn(name = "idpessoa")
    )
    private List<Pessoa> membros = new ArrayList<>();

    public void adicionarMembro(Pessoa pessoa) {
        if (pessoa.isFuncionario()) {
            membros.add(pessoa);
            pessoa.getProjetos().add(this);
        }
    }
}
