package com.example.portfolio;

import com.example.portfolio.model.Pessoa;
import com.example.portfolio.model.Projeto;
import com.example.portfolio.repository.PessoaRepository;
import com.example.portfolio.repository.ProjetoRepository;
import com.example.portfolio.service.ProjetoService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjetoServiceTest {

    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private ProjetoService projetoService;

    public ProjetoServiceTest() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateProject() {

        //begin
        Pessoa pessoa = new Pessoa();
        pessoa.setFuncionario(true);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        Projeto projeto = new Projeto();
        projeto.setNome("Projeto Teste");

        when(projetoRepository.save(any(Projeto.class))).thenReturn(projeto);

        //call
        Projeto projetoCriado = projetoService.createProject(projeto);

        // Verify
        assertNotNull(projetoCriado);
        assertEquals("Projeto Teste", projetoCriado.getNome());
    }

    @Test
    void testGetProjectById() {
        // Configuração do cenário
        Long projetoId = 1L;
        Projeto projeto = new Projeto(/* configurar o projeto conforme necessário */);

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projeto));

        // Chama o método e verifica o resultado
        Projeto resultado = projetoService.getProjectById(projetoId);

        assertEquals(projeto, resultado);
    }

    @Test
    void testGetProjectById_NaoEncontrado() {
        // Configuração do cenário
        Long projetoId = 1L;

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.empty());

        // Chama o método e verifica o resultado
        Projeto resultado = projetoService.getProjectById(projetoId);

        assertNull(resultado);
    }

    @Test
    void testAtualizarProjeto() {
        Long projetoId = 1L;
        Projeto projetoExistente = new Projeto();
        Projeto projetoAtualizado = new Projeto();

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(projetoExistente));
        when(projetoRepository.save(projetoExistente)).thenReturn(projetoExistente);

        // Chama o método e verifica o resultado
        Projeto resultado = projetoService.atualizarProjeto(projetoId, projetoAtualizado);

        assertEquals(projetoAtualizado, resultado);
    }

    @Test
    void testAssociarMembroAProjeto() {
        //begin
        Pessoa pessoa = new Pessoa();
        pessoa.setFuncionario(true);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        //call
        boolean associacaoBemSucedida = projetoService.associarMembroAProjeto(null, 1L);

        //assertions
        assertFalse(associacaoBemSucedida);
        verify(pessoaRepository).findById(1L);
        verify(projetoRepository, never()).findById(anyLong());
    }


    @Test
    void testDeletarProjeto() {
        Long projetoId = 1L;
        Projeto existingProjeto = new Projeto();

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.of(existingProjeto));

        boolean resultado = projetoService.deletarProjeto(projetoId);

        assertTrue(resultado);
        verify(projetoRepository, times(1)).delete(existingProjeto);
    }

    @Test
    void testDeletarProjetoNaoEncontrado() {
        Long projetoId = 1L;

        when(projetoRepository.findById(projetoId)).thenReturn(Optional.empty());

        boolean resultado = projetoService.deletarProjeto(projetoId);

        assertFalse(resultado);
    }
}
