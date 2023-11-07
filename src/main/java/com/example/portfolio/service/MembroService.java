package com.example.portfolio.service;

import com.example.portfolio.model.Membro;
import com.example.portfolio.repository.MembroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MembroService {

    @Autowired
    private MembroRepository membroRepository;

    public Membro obterMembroPorId(Long id) {
        return membroRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Membro não encontrado"));
    }

    public List<Membro> obterTodosOsMembros() {
        return membroRepository.findAll();
    }

}
