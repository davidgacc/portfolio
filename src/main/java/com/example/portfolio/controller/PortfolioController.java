package com.example.portfolio.controller;

import com.example.portfolio.exceptions.InvalidoProjetoStatusException;
import com.example.portfolio.model.Membro;
import com.example.portfolio.model.Pessoa;
import com.example.portfolio.model.Projeto;
import com.example.portfolio.service.MembroService;
import com.example.portfolio.service.PessoaService;
import com.example.portfolio.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/projetos")
public class PortfolioController {
    @Autowired
    private ProjetoService projetoService;
    @Autowired
    private MembroService membroService;
    @Autowired
    private PessoaService pessoaService;

    @GetMapping()
    public String listarProjetos(Model model) {
        List<Projeto> projetos = projetoService.getAllProjects();
        model.addAttribute("projetos", projetos);
        return "projeto/listarProjetos";
    }

    @GetMapping("/{id}")
    public String obterProjeto(@PathVariable Long id, Model model) {
        Projeto projeto = projetoService.getProjectById(id);
        model.addAttribute("projeto", projeto);
        return "projeto/detalhesProjeto";
    }

    @PostMapping("/")
    public ResponseEntity<Projeto> createProject(@RequestBody Projeto projeto) {
        Projeto createdProjeto = projetoService.createProject(projeto);
        return new ResponseEntity<>(createdProjeto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/editar")
    public ResponseEntity<Projeto> atualizarProjeto(@PathVariable Long id, @RequestBody Projeto projeto) {
        Projeto updatedProjeto = projetoService.atualizarProjeto(id, projeto);
        if (updatedProjeto != null) {
            return ResponseEntity.ok(updatedProjeto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarProjeto(@PathVariable Long id) {
        try {
            boolean deleted = projetoService.deletarProjeto(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (InvalidoProjetoStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/pessoa")
    public ResponseEntity<Pessoa> criarPessoa(@RequestBody Pessoa pessoa) {
        Pessoa novaPessoa = pessoaService.criarPessoa(pessoa);
        return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
    }

    @PostMapping("/{pessoaId}/adicionar-projeto")
    public ResponseEntity<String> adicionarProjetoAPessoa(@PathVariable Long pessoaId, @RequestBody Projeto projeto) {
        try {
            pessoaService.adicionarProjetoAPessoa(pessoaId, projeto);
            return ResponseEntity.ok("Projeto adicionado à pessoa com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao adicionar projeto à pessoa: " + e.getMessage());
        }
    }

    @PostMapping("/{pessoaId}/remover-projeto")
    public ResponseEntity<String> removerProjetoDePessoa(@PathVariable Long pessoaId, @RequestBody Projeto projeto) {
        try {
            pessoaService.removerProjetoDePessoa(pessoaId, projeto);
            return ResponseEntity.ok("Projeto removido da pessoa com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover projeto da pessoa: " + e.getMessage());
        }
    }

    @GetMapping("/membros/{id}")
    public ResponseEntity<Membro> obterMembroPorId(@PathVariable Long id) {
        try {
            Membro membro = membroService.obterMembroPorId(id);
            return ResponseEntity.ok(membro);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/membros")
    public ResponseEntity<List<Membro>> obterTodosOsMembros() {
        List<Membro> membros = membroService.obterTodosOsMembros();
        return ResponseEntity.ok(membros);
    }

    @PostMapping("/{projetoId}/associar-membro/{pessoaId}")
    public ResponseEntity<String> associarMembroAoProjeto(@PathVariable Long projetoId, @PathVariable Long pessoaId) {
        try {
            boolean associacaoBemSucedida = projetoService.associarMembroAProjeto(projetoId, pessoaId);

            if (associacaoBemSucedida) {
                return ResponseEntity.ok("Membro associado ao projeto com sucesso.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Não foi possível associar o membro ao projeto. Verifique os IDs fornecidos.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao associar membro ao projeto: " + e.getMessage());
        }
    }
}
