package com.app.sirh.repository;

import com.app.sirh.model.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PosteRepository extends JpaRepository<Poste, Long> {
    List<Poste> findByDepartementId(Long departementId);
    
    // Recherche sécurisée d'un poste par son département et son titre exact
    Optional<Poste> findByDepartementIdAndTitreIgnoreCase(Long departementId, String titre);
}