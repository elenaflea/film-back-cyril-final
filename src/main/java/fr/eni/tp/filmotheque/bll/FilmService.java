package fr.eni.tp.filmotheque.bll;

import fr.eni.tp.filmotheque.bo.Avis;
import fr.eni.tp.filmotheque.bo.Film;
import fr.eni.tp.filmotheque.bo.Genre;
import fr.eni.tp.filmotheque.bo.Participant;
import fr.eni.tp.filmotheque.dto.ParametresRecherche;

import java.util.List;

/**
 * Interface
 * Sert à SPECIFIER DES FONCTIONNALITES que vont devoir IMPLEMENTER certaines classes
 */
public interface FilmService {
    /**
     * je DOIS être en mesure de consulter tous les films
     */
    List<Film> consulterFilms();

    /**
     * je DOIS être en mesure de consulter un film à partir de son id
     */
    Film consulterFilmParId(long id);

    void creerFilm(Film film);

    void publierAvis(Avis avis, long idFilm);

    void supprimerFilmParId(Long idFilm);

     List<Film>  rechercherFilms(ParametresRecherche parametresRecherche);
}
