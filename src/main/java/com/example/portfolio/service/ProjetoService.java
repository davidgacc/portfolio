package com.example.portfolio.service;

import com.example.portfolio.exceptions.InvalidoProjetoStatusException;
import com.example.portfolio.model.Pessoa;
import com.example.portfolio.model.Projeto;
import com.example.portfolio.repository.PessoaRepository;
import com.example.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjetoService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private PessoaRepository pessoaRepository;

    public boolean associarMembroAProjeto(Long projetoId, Long pessoaId) {
        Projeto projeto = projectRepository.findById(projetoId).orElse(null);
        Pessoa pessoa = pessoaRepository.findById(pessoaId).orElse(null);

        if (projeto != null && pessoa != null && pessoa.getFuncionario()) {
            projeto.adicionarMembro(pessoa);
            projectRepository.save(projeto);
            return true;
        }

        return false;
    }

    public List<Projeto> getAllProjects() {
        return projectRepository.findAll();
    }

    public Projeto getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Projeto createProject(Projeto projeto) {
        return projectRepository.save(projeto);
    }

    public Projeto atualizarProjeto(Long id, Projeto projeto) {

        Projeto projetoExistente = projectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Projeto não encontrado"));

        projetoExistente.setNome(projeto.getNome());
        projetoExistente.setDataInicio(projeto.getDataInicio());
        projetoExistente.setIdGerente(projeto.getIdGerente());
        projetoExistente.setDataPrevisaoFim(projeto.getDataPrevisaoFim());
        projetoExistente.setDataFim(projeto.getDataFim());
        projetoExistente.setOrcamento(projeto.getOrcamento());
        projetoExistente.setDescricao(projeto.getDescricao());
        projetoExistente.setStatus(projeto.getStatus());
        projetoExistente.setRisco(projeto.getRisco());

        return projectRepository.save(projetoExistente);

    }

    public boolean deletarProjeto(Long id) {
        Projeto existingProjeto = projectRepository.findById(id).orElse(null);
        if (existingProjeto != null) {
            if (isDeletionAllowed(existingProjeto.getStatus())) {
                projectRepository.delete(existingProjeto);
                return true;
            } else {
                throw new InvalidoProjetoStatusException("Não é possível deletar um projeto com status " + existingProjeto.getStatus());
            }
        }
        return false;
    }

    private boolean isDeletionAllowed(Projeto.Status status) {
        return !EnumSet.of(
                Projeto.Status.INICIADO,
                Projeto.Status.EM_ANDAMENTO,
                Projeto.Status.ENCERRADO
        ).contains(status);
    }
}
