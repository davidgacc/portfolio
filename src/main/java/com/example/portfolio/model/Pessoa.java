package com.example.portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate datanascimento;
    private String cpf;
    private Boolean funcionario;

    @ManyToMany(mappedBy = "membros")
    @ToString.Exclude
    private List<Projeto> projetos = new ArrayList<>();

    public void adicionarProjeto(Projeto projeto) {
        projetos.add(projeto);
        projeto.getMembros().add(this);
    }

    public void removerProjeto(Projeto projeto) {
        projetos.remove(projeto);
        projeto.getMembros().remove(this);
    }

    public Boolean isFuncionario() {
        return funcionario;
    }
}
