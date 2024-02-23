package fr.eni.tp.filmotheque;

import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import fr.eni.tp.filmotheque.controller.FilmController;

import java.util.List;

@SpringBootApplication
public class FilmothequeApplication implements CommandLineRunner {

	@Autowired
	MembreService membreService;

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FilmothequeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		 List<Membre> membres = membreService.consulterMembres();

		 // si il n'y a pas de membre dans l'application, on en crée un
		 if (membres.isEmpty()){
			 Membre admin = new Membre("Cyril", "Mace", "admin", "admin", true);
			 membreService.creerMembre(admin);
		 }
	}
}
