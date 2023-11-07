package com.example.portfolio.repository;

import com.example.portfolio.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Projeto, Long> {
}
