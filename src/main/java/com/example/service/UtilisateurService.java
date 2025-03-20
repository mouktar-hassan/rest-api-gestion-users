package com.example.service;

import com.example.dto.AdresseDto;
import com.example.dto.UtilisateurDTO;
import com.example.exeptions.AdresseNotFoundException;
import com.example.exeptions.UtilisateurNotFoundException;
import com.example.mappers.AdresseMapper;
import com.example.mappers.UtilisateurMapper;
import com.example.model.Adresse;
import com.example.model.Utilisateur;
import com.example.repository.AdresseRepository;
import com.example.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilisateurService {

    private static final Logger logger = LoggerFactory.getLogger(UtilisateurService.class);

    private final UtilisateurRepository utilisateurRepository;
    private final AdresseRepository adresseRepository;
    private final UtilisateurMapper utilisateurMapper;
    private final AdresseMapper adresseMapper;

    /*
    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository,
                              AdresseRepository adresseRepository,
                              UtilisateurMapper utilisateurMapper,
                              AdresseMapper adresseMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.adresseRepository = adresseRepository;
        this.utilisateurMapper = utilisateurMapper;
        this.adresseMapper = adresseMapper;
    }*/

    @Transactional
    public UtilisateurDTO  ajouterUtilisateur(UtilisateurDTO utilisateurDTO) {
        if(utilisateurDTO.getAdresses() == null || utilisateurDTO.getAdresses().isEmpty()){
            logger.error("Tentative d'ajout d'un utilisateur sans adresse");
            throw new IllegalArgumentException("L'utilisateur doit avoir au moins une adresse");
        }
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur.getAdresses().forEach(adresse -> adresse.setUtilisateur(utilisateur));
        Utilisateur savedUtilisateur = utilisateurRepository.save(utilisateur);
        logger.info("L'utilisateur avec l'id {} est ajouté", savedUtilisateur.getId());
        return utilisateurMapper.toDto(savedUtilisateur);
    }

    public List<UtilisateurDTO> afficherListUtilisateur() {
        return utilisateurRepository.findAll().stream()
                .map(utilisateurMapper::toDto)
                .toList();
    }

    public UtilisateurDTO getUtilisateurId(long id) {
        Utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + id));
        return utilisateurMapper.toDto(utilisateur);
    }

    public void supprimerUtilisateur(Long id) {
        if (!utilisateurRepository.existsById(id)) {
            logger.error("Utilisateur non trouvé avec l'ID : " + id);
            throw new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + id);
        }
        utilisateurRepository.deleteById(id);
    }

    public AdresseDto ajouterAdresse(Long utilisateurId, AdresseDto adresseDto) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));

        Adresse adresse = adresseMapper.toEntity(adresseDto);
        utilisateur.ajouterAdresse(adresse);
        utilisateurRepository.save(utilisateur);
        logger.debug("Utilisateur crée avec succès !");
        return adresseMapper.toDto(adresse);
    }

    public void supprimerAdresse(Long utilisateurId, Long adresseId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));
        Adresse adresse = adresseRepository.findById(adresseId)
                .orElseThrow(() -> new AdresseNotFoundException("Adresse non trouvée avec l'ID : " + adresseId));

        utilisateur.supprimerAdresse(adresse);
        utilisateurRepository.save(utilisateur);
    }

    public List<AdresseDto> afficherAdressesUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));

        return utilisateur.getAdresses().stream()
                .map(adresseMapper::toDto)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO modifierUtilisateur(Long utilisateurId, UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));

        // Mettre à jour les champs simples
        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setMail(utilisateurDTO.getMail());

        // Gestion des adresses
        if (utilisateurDTO.getAdresses() != null) {
            Map<Long, Adresse> adresseMap = utilisateur.getAdresses().stream()
                    .collect(Collectors.toMap(Adresse::getId, Function.identity()));

            List<Adresse> updatedAdresses = new ArrayList<>();

            for (AdresseDto adresseDTO : utilisateurDTO.getAdresses()) {
                if (adresseDTO.getId() != null && adresseMap.containsKey(adresseDTO.getId())) {
                    // Mettre à jour une adresse existante
                    Adresse adresse = adresseMap.get(adresseDTO.getId());
                    adresse.setRue(adresseDTO.getRue());
                    adresse.setVille(adresseDTO.getVille());
                    adresse.setPays(adresseDTO.getPays());
                    updatedAdresses.add(adresse);
                } else {
                    // Ajouter une nouvelle adresse
                    Adresse newAdresse = new Adresse();
                    newAdresse.setRue(adresseDTO.getRue());
                    newAdresse.setVille(adresseDTO.getVille());
                    newAdresse.setPays(adresseDTO.getPays());
                    newAdresse.setUtilisateur(utilisateur);
                    updatedAdresses.add(newAdresse);
                }
            }

            // Remplacer les adresses dans l'utilisateur
            utilisateur.getAdresses().clear();
            utilisateur.getAdresses().addAll(updatedAdresses);
        }

        return utilisateurMapper.toDto(utilisateurRepository.save(utilisateur));
    }


    /*public UtilisateurDTO modifierUtilisateur(Long utilisateurId, UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));

        utilisateurMapper.updateUtilisateurFromDto(utilisateurDTO, utilisateur);

        return utilisateurMapper.toDto(utilisateurRepository.save(utilisateur));
    }*/

    public AdresseDto modifierAdresseUtilisateur(Long utilisateurId, Long adresseId, AdresseDto adresseDto) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur non trouvé avec l'ID : " + utilisateurId));

        Adresse adresse = utilisateur.getAdresses().stream()
                .filter(a -> a.getId().equals(adresseId))
                .findFirst()
                .orElseThrow(() -> new AdresseNotFoundException("Adresse non trouvée avec l'ID : " + adresseId));

        adresseMapper.updateAdresseFromDto(adresseDto, adresse);

        adresseRepository.save(adresse);
        return adresseMapper.toDto(adresse);
    }
}
