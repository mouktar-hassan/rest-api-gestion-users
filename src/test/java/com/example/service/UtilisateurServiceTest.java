package com.example.service;

import com.example.dto.AdresseDto;
import com.example.dto.UtilisateurDTO;
import com.example.mappers.UtilisateurMapper;
import com.example.model.Adresse;
import com.example.model.Utilisateur;
import com.example.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    private UtilisateurDTO utilisateurDTO;
    private Utilisateur utilisateur;
    private AdresseDto adresseDTO;
    private Adresse adresse;

    @BeforeEach
    void setUp() {
        adresseDTO = AdresseDto.builder()
                .rue("5 rue")
                .ville("Schleswig")
                .pays("Allemagne")
                .build();

        adresse = Adresse.builder()
                .rue("5 rue")
                .ville("Schleswig")
                .pays("Allemagne")
                .build();

        utilisateurDTO = UtilisateurDTO.builder()
                .nom("Hodan")
                .mail("hodan@gmail.com")
                .adresses(List.of(adresseDTO))
                .build();

        utilisateur = Utilisateur.builder()
                .nom("Hodan")
                .mail("hodan@gmail.com")
                .adresses(List.of(adresse))
                .build();
    }


    @Test
    public void testAjouterUtilisateur() {
        // **Arrange**
        when(utilisateurMapper.toEntity(utilisateurDTO)).thenReturn(utilisateur);
        when(utilisateurRepository.save(utilisateur)).thenReturn(utilisateur);
        when(utilisateurMapper.toDto(utilisateur)).thenReturn(utilisateurDTO);

        // **Act**
        UtilisateurDTO result = utilisateurService.ajouterUtilisateur(utilisateurDTO);

        // **Assert**
        assertThat(result).isNotNull()
                .extracting(UtilisateurDTO::getNom, UtilisateurDTO::getMail)
                .containsExactly("Hodan", "hodan@gmail.com");

        assertThat(result.getAdresses()).hasSize(1)
                .extracting(AdresseDto::getRue, AdresseDto::getVille, AdresseDto::getPays)
                .containsExactly(tuple("5 rue", "Schleswig", "Allemagne"));

        verify(utilisateurMapper).toEntity(utilisateurDTO);
        verify(utilisateurRepository).save(utilisateur);
        verify(utilisateurMapper).toDto(utilisateur);
    }

    @Test
    public void testAfficherListUtilisateur() {
        // **Arrange**
        when(utilisateurRepository.findAll()).thenReturn(List.of(utilisateur));
        when(utilisateurMapper.toDto(utilisateur)).thenReturn(utilisateurDTO);

        // **Act**
        List<UtilisateurDTO> utilisateurDTOList = utilisateurService.afficherListUtilisateur();

        // **Assert**
        assertThat(utilisateurDTOList).isNotNull().hasSize(1);
        //usingRecursiveComparison() : permet de comparer les objets en prenant en compte tous leurs champs (y compris les champs des objets imbriqués) de manière récursive
        assertThat(utilisateurDTOList.get(0)).usingRecursiveComparison().isEqualTo(utilisateurDTO);

        verify(utilisateurMapper).toDto(utilisateur);
        verify(utilisateurRepository).findAll();
    }

}

/*
import com.example.dto.AdresseDto;
import com.example.dto.UtilisateurDTO;
import com.example.mappers.UtilisateurMapper;
import com.example.model.Adresse;
import com.example.model.Utilisateur;
import com.example.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
// Imports pour Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

// Imports pour les assertions
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;



@ExtendWith(MockitoExtension.class)// cette annotation permet d'initialiser les mock marqué avec @Mock
public class UtilisateurServiceTest {

    @InjectMocks
    private UtilisateurService utilisateurService;

    @Mock
    private UtilisateurRepository utilisateurRepository;
    @Mock
    private UtilisateurMapper utilisateurMapper;


    @Test
    public void TestajouterUtilisateur(){
        // **Arrage**
        // Simulation adresseDTO
        AdresseDto adresseDTO = new AdresseDto();
        adresseDTO.setRue("5 rue");
        adresseDTO.setVille("Schleswig");
        adresseDTO.setPays("Allemagne");

        ArrayList<AdresseDto> adresseDtoList = new ArrayList<>();
        adresseDtoList.add(adresseDTO);

        // Simulation adresse
        Adresse adresseEntity = new Adresse();
        adresseEntity.setRue("5 rue");
        adresseEntity.setVille("Schleswig");
        adresseEntity.setPays("Allemagne");

        ArrayList<Adresse> adresseEntityList = new ArrayList<>();
        adresseEntityList.add(adresseEntity);

        // Simulation utilisateurDTO
        UtilisateurDTO utilisateurDtoInput = new UtilisateurDTO();
        utilisateurDtoInput.setNom("Hodan");
        utilisateurDtoInput.setMail("hodan@gmail.com");
        utilisateurDtoInput.setAdresses(adresseDtoList);

        // Simulation utilisateur
        Utilisateur utilisateurEntity = new Utilisateur();
        utilisateurEntity.setNom("Hodan");
        utilisateurEntity.setMail("hodan@gmail.com");
        utilisateurEntity.setAdresses(adresseEntityList);

        Utilisateur utilisateurEntitySaved = new Utilisateur();
        utilisateurEntitySaved.setNom("Hodan");
        utilisateurEntitySaved.setMail("hodan@gmail.com");
        utilisateurEntitySaved.setAdresses(adresseEntityList);


        UtilisateurDTO utilisateurDtoOutput = new UtilisateurDTO();
        utilisateurDtoOutput.setNom("Hodan");
        utilisateurDtoOutput.setMail("hodan@gmail.com");
        utilisateurDtoOutput.setAdresses(adresseDtoList);

        // Mock des interactions avec utilisateurMapper et utilisateurRepository
        when(utilisateurMapper.toEntity(utilisateurDtoInput)).thenReturn(utilisateurEntity);
        when(utilisateurRepository.save(utilisateurEntity)).thenReturn(utilisateurEntitySaved);
        when(utilisateurMapper.toDto(utilisateurEntitySaved)).thenReturn(utilisateurDtoOutput);

        // **Act**
        UtilisateurDTO result = utilisateurService.ajouterUtilisateur(utilisateurDtoInput);

        // **Assert**
        assertNotNull(result, "l'objet retourné ne doit pas etre null");
        assertEquals(result.getNom(), "Hodan");
        assertEquals(result.getMail(), "hodan@gmail.com");
        assertEquals(result.getAdresses().get(0).getRue(),"5 rue");
        assertEquals(result.getAdresses().get(0).getVille(),"Schleswig");
        assertEquals(result.getAdresses().get(0).getPays(),"Allemagne");
        assertEquals(result.getAdresses().size(), 1);

        // Vérification des appels
        verify(utilisateurMapper).toEntity(utilisateurDtoInput);
        verify(utilisateurRepository).save(utilisateurEntity);
        verify(utilisateurMapper).toDto(utilisateurEntitySaved);

    }

    @Test
    public void TestafficherListUtilisateur(){
        //**Arrange**

        // Simulation adresseDto
        AdresseDto adresse = new AdresseDto();
        adresse.setRue("5 rue");
        adresse.setVille("Schleswig");
        adresse.setPays("Allemagne");

        ArrayList<AdresseDto> adresseDtoList = new ArrayList<>();
        adresseDtoList.add(adresse);

        // Simulation adresse
        Adresse adresseEntity = new Adresse();
        adresseEntity.setRue("5 rue");
        adresseEntity.setVille("Schleswig");
        adresseEntity.setPays("Allemagne");

        ArrayList<Adresse> adresseEntityList = new ArrayList<>();
        adresseEntityList.add(adresseEntity);

        // Simulation utilisateurDto
        UtilisateurDTO utilisateurDto = new UtilisateurDTO();
        utilisateurDto.setNom("Hodan");
        utilisateurDto.setMail("hodan@gmail.com");
        utilisateurDto.setAdresses(adresseDtoList);

        // Simulation utilisateur
        Utilisateur utilisateurEntity = new Utilisateur();
        utilisateurEntity.setNom("Hodan");
        utilisateurEntity.setMail("hodan@gmail.com");
        utilisateurEntity.setAdresses(adresseEntityList);

        ArrayList<Utilisateur> utilisateurs = new ArrayList<>();
        utilisateurs.add(utilisateurEntity);

        // Mock des interactions
        when(utilisateurMapper.toDto(utilisateurEntity)).thenReturn(utilisateurDto);
        when(utilisateurRepository.findAll()).thenReturn(utilisateurs);

        //**Act**
        List<UtilisateurDTO> utilisateurDTOList = utilisateurService.afficherListUtilisateur();

        //**Assert**
        assertNotNull(utilisateurDTOList, "la liste d'objet retourné ne doit pas etre null.");
        assertEquals(utilisateurDTOList.size(), 1);

        UtilisateurDTO utilisateurDTOResult = utilisateurDTOList.get(0);
        assertEquals(utilisateurDTOResult.getNom(), "Hodan");
        assertEquals(utilisateurDTOResult.getMail(), "hodan@gmail.com");

        //**Verify**
        verify(utilisateurMapper).toDto(utilisateurEntity);
        verify(utilisateurRepository).findAll();
    }

}

 */
