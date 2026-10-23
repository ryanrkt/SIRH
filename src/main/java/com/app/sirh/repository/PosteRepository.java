package com.app.sirh.repository;

import com.app.sirh.model.Poste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PosteRepository extends JpaRepository<Poste, Long> {
    List<Poste> findByDepartementId(Long departementId);

    boolean existsByDepartementIdAndTitre(Long departementId, String titre);

     long countByDepartement_Id(Long departementId);
}
