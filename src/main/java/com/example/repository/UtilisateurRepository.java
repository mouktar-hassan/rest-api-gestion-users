package com.example.repository;

import com.example.model.Adresse;
import com.example.model.Utilisateur;
import jdk.jshell.execution.Util;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    void findByNom(String nom);

    /*@Query("SELECT u FROM Utilisateur u LEFT JOIN FETCH u.adresses")
    List<Utilisateur> findAllWithAdresses();*/

}
