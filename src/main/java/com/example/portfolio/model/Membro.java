package com.example.portfolio.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "membros")
public class Membro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idprojeto")
    private Projeto projeto;

    @ManyToOne
    @JoinColumn(name = "idpessoa", referencedColumnName = "id")
    private Pessoa pessoa;
}
