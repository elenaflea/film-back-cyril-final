package fr.eni.tp.filmotheque.bll.jpa;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bo.*;
import fr.eni.tp.filmotheque.dal.AvisRepository;
import fr.eni.tp.filmotheque.dal.FilmRepository;
import fr.eni.tp.filmotheque.dto.ParametresRecherche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("prod")
public class FilmServiceJpaImpl implements FilmService {

	@Autowired
	FilmRepository filmRepository;

	@Autowired
	AvisRepository avisRepository;


	@Override
	public List<Film> consulterFilms() {
		return filmRepository.findAll();
	}


	@Override
	public Film consulterFilmParId(long id) {
		return filmRepository.findById(id).get();
		// fait la même chose avec une transaction séparée : return filmRepository.getReferenceById(id);
	}


	@Override
	public void creerFilm(Film film) {
		filmRepository.save(film);
	}

	@Override
	public void modifierFilm(Film film) {
		// on vérifie d'abord que le film existe
		if (filmRepository.existsById(film.getId())){
			filmRepository.save(film);
		}
		// si ca n'est pas le cas => on lance une exception
		else{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le film n'existe pas");
		}
	}


	@Override
	public void publierAvis(Avis avis, long idFilm) {
		// quand on publie un avis

		// 1 - on sauvegarde l'avis
		avisRepository.save(avis);

		// 2 - on met à jour la relation entre film et avis
		Film filmAMettreAJour = consulterFilmParId(idFilm);
		filmAMettreAJour.getAvis().add(avis);
		filmRepository.save(filmAMettreAJour);

	}

	@Override
	public void supprimerFilmParId(Long idFilm) {
		// on vérifie d'abord que le film existe
		if (filmRepository.existsById(idFilm)){
			filmRepository.deleteById(idFilm);
		}
		// si ca n'est pas le cas => on lance une exception
		else{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le film n'existe pas");
		}
	}

	@Override
	public List<Film> rechercherFilms(ParametresRecherche parametresRecherche) {
		return filmRepository.rechercher(parametresRecherche);
	}
}
