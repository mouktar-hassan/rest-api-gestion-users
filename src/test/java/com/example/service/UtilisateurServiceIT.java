package com.example.service;

import com.example.dto.AdresseDto;
import com.example.dto.UtilisateurDTO;
import com.example.mappers.UtilisateurMapper;
import com.example.model.Utilisateur;
import com.example.repository.UtilisateurRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // pour nettoyer le context afin d'éviter l'interférence et isoler les tests
public class UtilisateurServiceIT {
    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Test
    public void TestCreerUtilisateur(){
        // **Arrange** : Création des objets
        UtilisateurDTO utilisateurDTO = UtilisateurDTO.builder()
                .nom("Mouktar")
                .mail("mouktar.hf@gmail.com")
                .adresses(List.of(AdresseDto.builder()
                        .rue("8 rue Denis Papin")
                        .ville("Orléans")
                        .pays("France")
                        .build()))
                .build();

        // **Act** : Sauvegarde en base
        UtilisateurDTO utilisateurSauvegarde = utilisateurService.ajouterUtilisateur(utilisateurDTO);

        // **Assert ** : vérification des données en base **
        assertThat(utilisateurSauvegarde).isNotNull();
        assertThat(utilisateurSauvegarde.getNom()).isEqualTo("Mouktar");
        assertThat(utilisateurSauvegarde.getMail()).isEqualTo("mouktar.hf@gmail.com");
        assertThat(utilisateurSauvegarde.getAdresses()).hasSize(1);

        // Vérification de la persistance de la relation Utilisateur ↔ Adresse
        Utilisateur utilisateurEnBase = utilisateurRepository.findById(utilisateurSauvegarde.getId()).orElseThrow();
        assertThat(utilisateurEnBase.getAdresses()).hasSize(1);
        assertThat(utilisateurEnBase.getAdresses().get(0).getVille()).isEqualTo("Orléans");


    }

}
