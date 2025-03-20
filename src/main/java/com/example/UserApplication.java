package com.example;

import com.example.model.Adresse;
import com.example.model.Utilisateur;
import com.example.repository.UtilisateurRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.example")
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(UtilisateurRepository utilisateurRepository) {
		return args -> {
			// Création des utilisateurs avec leurs adresses
			Utilisateur utilisateur1 = Utilisateur.builder()
					.nom("Mouktar")
					.mail("mouktar.hf@gmail.com")
					.build();

			utilisateur1.ajouterAdresse(
					Adresse.builder().rue("123 Rue Exemple").ville("Paris").pays("France").build()
			);
			utilisateur1.ajouterAdresse(
					Adresse.builder().rue("456 Avenue Test").ville("Lyon").pays("France").build()
			);

			Utilisateur utilisateur2 = Utilisateur.builder()
					.nom("Fozia")
					.mail("fozia@live.fr")
					.build();

			utilisateur2.ajouterAdresse(
					Adresse.builder().rue("789 Boulevard Essai").ville("Marseille").pays("France").build()
			);

			Utilisateur utilisateur3 = Utilisateur.builder()
					.nom("Hassan")
					.mail("hassan@outlook.fr")
					.build();

			utilisateur3.ajouterAdresse(
					Adresse.builder().rue("789 Boulevard Essai").ville("Marseille").pays("France").build()
			);

			// Sauvegarder les utilisateurs dans la base de données
			utilisateurRepository.save(utilisateur1);
			utilisateurRepository.save(utilisateur2);
			utilisateurRepository.save(utilisateur3);

			// Affichage dans la console pour vérifier les données ajoutées
			utilisateurRepository.findAll().forEach(System.out::println);

			// Charger explicitement les adresses pour éviter LazyInitializationException
			utilisateurRepository.findAll().forEach(utilisateur -> {
				utilisateur.getAdresses().size(); // Force l'initialisation des adresses
				System.out.println(utilisateur); // Affiche l'utilisateur avec ses adresses
			});
		};
	}


}
