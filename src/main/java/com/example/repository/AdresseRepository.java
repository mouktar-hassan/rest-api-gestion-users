package com.example.repository;

import com.example.model.Adresse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {

    Optional<Adresse> findById(Long id);

    List<Adresse> findAll();



}
