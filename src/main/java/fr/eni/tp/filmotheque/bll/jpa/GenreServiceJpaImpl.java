package fr.eni.tp.filmotheque.bll.jpa;

import fr.eni.tp.filmotheque.bll.GenreService;
import fr.eni.tp.filmotheque.bo.Genre;
import fr.eni.tp.filmotheque.dal.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class GenreServiceJpaImpl implements GenreService {
    /**
     * Plutôt que de gérer une liste de genres dans le service,
     * Je vais me servir de GenreRepository pour créer/recupérer les genres depuis la base de donnée
     */
    @Autowired
    private GenreRepository genreRepository;

    @Override
    public List<Genre> consulterGenres() {
        // on recupère les genres depuis la base de donnée
        return genreRepository.findAll();
    }

    @Override
    public Genre consulterGenreParId(long id) {
        // On recupère un genre depuis la base de donnée

        // findById renvoie un objet de type Optional<Genre> (c'est à dire : un genre potentiel)
        /*
        * *
        *  2 façon de gérer ca :
        *  - soit on rajoute .get() => va renvoyer l'élément si présent, sinon va lancer une exception : NoSuchElementException
        * - soit on gère nous même le cas ou y'a pas d'éléments
        *
        * Optional<Genre> reponse = genreRepository.findById(id);
        * if (reponse.isEmpty()){
        *     return null;
        * }
        * else{
        *     return reponse.get();
        * }
         */
        return genreRepository.findById(id).get();
    }

    @Override
    public void supprimerGenreParId(long id) throws ResponseStatusException{

        // on vérifie d'abord que le genre existe
        if (genreRepository.existsById(id)){
            genreRepository.deleteById(id);
        }
        // si ca n'est pas le cas => on lance une exception
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le genre n'a pas été trouvé");
        }
    }

    @Override
    public void creerGenre(Genre genre) {
        System.out.println("creerGenre() : à compléter");
        // On crée le genre dans la base de donnée
        genreRepository.save(genre);
    }

    @Override
    public void modifierGenre(Genre genre) {
        // on vérifie d'abord que le genre existe
        if (genreRepository.existsById(genre.getId())){
            genreRepository.save(genre);
        }
        // si ca n'est pas le cas => on lance une exception
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le genre n'existe pas");
        }
    }
}
