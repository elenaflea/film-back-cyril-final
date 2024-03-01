package fr.eni.tp.filmotheque.bll;

import fr.eni.tp.filmotheque.bo.Avis;
import fr.eni.tp.filmotheque.bo.Film;
import fr.eni.tp.filmotheque.bo.Genre;
import fr.eni.tp.filmotheque.bo.Participant;

import java.util.List;

/**
 * Interface
 * Sert à SPECIFIER DES FONCTIONNALITES que vont devoir IMPLEMENTER certaines classes
 */
public interface GenreService {

    List<Genre> consulterGenres();

    Genre consulterGenreParId(long id);

    void supprimerGenreParId(long id);

    void creerGenre(Genre genre);

    void modifierGenre(Genre genre);
}
