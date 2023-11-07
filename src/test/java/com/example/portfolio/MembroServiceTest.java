package com.example.portfolio;

import com.example.portfolio.model.Membro;
import com.example.portfolio.repository.MembroRepository;
import com.example.portfolio.service.MembroService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class MembroServiceTest {
    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private MembroService membroService;

    public MembroServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testObterMembroPorId() {
        Membro membro = new Membro();
        membro.setId(1L);

        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));

        Membro membroRetornado = membroService.obterMembroPorId(1L);

        assertEquals(membro, membroRetornado);
    }

    @Test
    void testObterMembroPorIdMembroNaoEncontrado() {
        when(membroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            membroService.obterMembroPorId(1L);
        });
    }

    @Test
    void testObterTodosOsMembros() {
        List<Membro> membros = new ArrayList<>();
        membros.add(new Membro());
        membros.add(new Membro());

        when(membroRepository.findAll()).thenReturn(membros);

        List<Membro> membrosRetornados = membroService.obterTodosOsMembros();

        assertEquals(membros, membrosRetornados);
    }
}
