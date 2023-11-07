package com.example.portfolio;

import com.example.portfolio.model.Pessoa;
import com.example.portfolio.model.Projeto;
import com.example.portfolio.repository.PessoaRepository;
import com.example.portfolio.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private PessoaService pessoaService;

    public PessoaServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCriarPessoa() {
        // Create a mock Pessoa object
        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome("John Doe");
        novaPessoa.setDatanascimento(LocalDate.of(1990, 1, 1));
        novaPessoa.setCpf("1234567890");
        novaPessoa.setFuncionario(true);

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(novaPessoa);

        Pessoa result = pessoaService.criarPessoa(novaPessoa);

        verify(pessoaRepository).save(novaPessoa);

        assertNotNull(result);

        assertEquals("John Doe", result.getNome());
        assertEquals(LocalDate.of(1990, 1, 1), result.getDatanascimento());
        assertEquals("1234567890", result.getCpf());
        assertTrue(result.getFuncionario());
    }

    @Test
    void testObterPessoaPorId() {
        Long id = 1L;
        Pessoa mockPessoa = new Pessoa();
        when(pessoaRepository.findById(id)).thenReturn(Optional.of(mockPessoa));

        Pessoa result = pessoaService.obterPessoaPorId(id);

        assertNotNull(result);
        assertEquals(mockPessoa, result);
    }

    @Test
    void testObterPessoaPorIdPessoaNaoEncontrada() {
        Long id = 1L;
        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> pessoaService.obterPessoaPorId(id));
    }

    @Test
    void testAdicionarProjetoAPessoa() {
        Long pessoaId = 1L;
        Projeto projeto = new Projeto(/* Initialize with necessary data */);

        Pessoa pessoa = new Pessoa();
        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.of(pessoa));

        pessoaService.adicionarProjetoAPessoa(pessoaId, projeto);

        verify(pessoaRepository, times(1)).save(pessoa);
    }

    @Test
    void testAdicionarProjetoAPessoaPessoaNaoEncontrada() {
        Long pessoaId = 1L;
        Projeto projeto = new Projeto();

        when(pessoaRepository.findById(pessoaId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            pessoaService.adicionarProjetoAPessoa(pessoaId, projeto);
        });

        verify(pessoaRepository, never()).save(any());
    }

    @Test
    void testRemoverProjetoDePessoa() {
        Projeto projeto = new Projeto();

        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.of(new Pessoa(/* parâmetros da pessoa */)));

        pessoaService.removerProjetoDePessoa(1L, projeto);

        verify(pessoaRepository, times(1)).save(any(Pessoa.class));
    }
}
