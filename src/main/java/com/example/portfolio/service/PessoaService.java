package com.example.portfolio.service;

import com.example.portfolio.model.Pessoa;
import com.example.portfolio.model.Projeto;
import com.example.portfolio.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PessoaService {
    @Autowired
    private PessoaRepository pessoaRepository;

    public Pessoa criarPessoa(Pessoa novaPessoa) {
        return pessoaRepository.save(novaPessoa);
    }

    public Pessoa obterPessoaPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pessoa não encontrada"));
    }

    public void adicionarProjetoAPessoa(Long pessoaId, Projeto projeto) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);
        if (pessoaOptional.isPresent()) {
            Pessoa pessoa = pessoaOptional.get();
            pessoa.adicionarProjeto(projeto);
            pessoaRepository.save(pessoa);
        } else {
            throw new NoSuchElementException("Pessoa não encontrada");
        }
    }

    public void removerProjetoDePessoa(Long pessoaId, Projeto projeto) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);
        pessoaOptional.ifPresent(pessoa -> {
            pessoa.removerProjeto(projeto);
            pessoaRepository.save(pessoa);
        });
    }
}
