package com.example.controller;

import com.example.dto.AdresseDto;
import com.example.dto.UtilisateurDTO;
import com.example.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")  // Permet les requêtes depuis localhost:4200
@RequiredArgsConstructor
public class UtilisateurController {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurController.class);

    private final UtilisateurService utilisateurService;

    /*
    @Autowired
    public UtilisateurController(UtilisateurService utilisateurService){
        this.utilisateurService = utilisateurService;
    }*/

    @GetMapping("/utilisateurs")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<UtilisateurDTO>> getAllUtilisateurs(){
        List<UtilisateurDTO> utilisateurList = utilisateurService.afficherListUtilisateur();
        if(utilisateurList.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(utilisateurList);
    }

    @GetMapping("/utilisateur/{id}")
    public ResponseEntity<UtilisateurDTO> getUtilisateurId(@PathVariable Long id){        ;
        return ResponseEntity.ok(utilisateurService.getUtilisateurId(id));
    }

    @PostMapping("/utilisateur")
    public ResponseEntity<UtilisateurDTO> addUtilisateur(@Valid @RequestBody UtilisateurDTO utilisateurDTO){
        logger.info("Requête reçue pour la création d'un utilisateur : {} " + utilisateurDTO);
        UtilisateurDTO newUtilisateur = utilisateurService.ajouterUtilisateur(utilisateurDTO);
        logger.info("Utilisateur crée avec succès !");
        return ResponseEntity.status(HttpStatus.CREATED).body(newUtilisateur);
    }

    @DeleteMapping("/utilisateur/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id){
        utilisateurService.supprimerUtilisateur(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/utilisateur/{id}")
    public ResponseEntity<UtilisateurDTO> updateUtilisateur(@Valid @RequestBody UtilisateurDTO utilisateurDTO, @PathVariable Long id){
        UtilisateurDTO updatedUtilisateur = utilisateurService.modifierUtilisateur(id, utilisateurDTO);
        return ResponseEntity.ok(updatedUtilisateur);
    }

    @PostMapping("/utilisateur/{id}/adresse")
    public ResponseEntity<AdresseDto> addAdresse(@PathVariable Long id, @Valid @RequestBody AdresseDto adresseDto){
        AdresseDto newAdresse = utilisateurService.ajouterAdresse(id, adresseDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdresse);
    }

    @DeleteMapping("/utilisateur/{utilisateurId}/adresse/{adresseId}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long utilisateurId, @PathVariable Long adresseId){
        utilisateurService.supprimerAdresse(utilisateurId, adresseId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/utilisateur/{utilisateurId}/adresses")
    public ResponseEntity<List<AdresseDto>> getAdressesUtilisateur(@PathVariable Long utilisateurId){
        List<AdresseDto> adresseList = utilisateurService.afficherAdressesUtilisateur(utilisateurId);
        return ResponseEntity.ok(adresseList);
    }

    @PutMapping("/utilisateur/{utilisateurId}/adresse/{adresseId}")
    public ResponseEntity<AdresseDto> updateAdresseUtilisateur(@PathVariable Long utilisateurId, @PathVariable Long adresseId, @Valid @RequestBody AdresseDto adresseDto){
        AdresseDto updatedAdresse = utilisateurService.modifierAdresseUtilisateur(utilisateurId, adresseId, adresseDto);
        return ResponseEntity.ok(updatedAdresse);
    }

    //Endppoint pour génerer de JWT
    @PostMapping("/login")
    public String login(@RequestParam String username){
        return null;
    }
}