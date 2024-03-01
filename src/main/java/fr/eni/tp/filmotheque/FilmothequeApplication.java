package fr.eni.tp.filmotheque;

import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import fr.eni.tp.filmotheque.controller.FilmController;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class FilmothequeApplication  {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FilmothequeApplication.class, args);
	}

	/**
	 * On définit un bean pour l'utilitaire d'encryption de mot de passe
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		// BCryptPasswordEncoder : classe utilitaire de Spring Security qui decrypte/encrypte les mots de passe
		return new BCryptPasswordEncoder();
	}


}
