package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bll.GenreService;
import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bo.Avis;
import fr.eni.tp.filmotheque.bo.Film;
import fr.eni.tp.filmotheque.bo.Membre;
import fr.eni.tp.filmotheque.dto.ParametresRecherche;
import fr.eni.tp.filmotheque.security.UtilisateurSpringSecurity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Pour créer un film, faire requête POST sur /api/films avec un JSON du type :
 *
 * {
 *   "titre":"FilmAPI",
 *   "annee":2010,
 *   "duree":122,
 *   "synopsis":"Ce film a été cré avec l'API, le scenario n'ets pas très cohérent.",
 *   "genre":{"id":16,"titre":"Science-Fiction"},
 *   "acteurs":[
 *     {"id":452,"nom":"Boon","prenom":"Dany"},
 *     {"id":453,"nom":"Spielberg","prenom":"Steven"},
 *     {"id":454,"nom":"Norris","prenom":"Chuck"}
 *   ],
 *   "realisateur":{"id":503,"nom":"Abrams","prenom":"J. J."}}
 */
@RestController// obligatoire afin que le controller soit dans le contexte Spring
@CrossOrigin
@RequestMapping("/api/films") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class FilmRestController {

    /* idéalement, le controller ne doit pas avoir connaissance de l'implémentation utilisée
     * FilmServiceBouchon filmService = new FilmServiceBouchon();
     * SOLUTION : passer par une interface
     */
    // en utilisant le type FilmService (qui n'est pas une classe mais une interface), je dis que filmService peut contenir une instance de n'importe quelle classe qui implémente les fonctionnalités requises par FilmService
    @Autowired // ici, Spring va automatiquement injecter une instance de classe (qui implémente l'interface FilmService) depuis le contexte Spring
    FilmService filmService;
    @Autowired // ici, Spring va automatiquement injecter une instance de classe (qui implémente l'interface FilmService) depuis le contexte Spring
    GenreService genreService;
    @Autowired // ici, Spring va automatiquement injecter une instance de classe (qui implémente l'interface FilmService) depuis le contexte Spring
    ParticipantService participantService;
    @Autowired // ici, Spring va automatiquement injecter une instance de classe (qui implémente l'interface FilmService) depuis le contexte Spring
    MembreService membreService;


    /**
     * GET pour recupérer tous les films
     */
    @GetMapping
    public List<Film> getFilms(ParametresRecherche parametresRecherche) {
        // on va vérifier si on a des paramètres de recherche
        if (parametresRecherche.isNotEmpty()){
            // si on a des paramètres de recherche, on va filtrer en fonction des paramètres
            return filmService.rechercherFilms(parametresRecherche);
        }
        // sinon, on affiche tous les films
        else{
             return  filmService.consulterFilms();
        }
    }

    /**
     * GET pour recupérer uns seul film
     */
    @GetMapping("/{filmId}")
    public Film getFilmDetail(@PathVariable("filmId") long filmId) {
        // 1 - on va aller chercher le film depuis le service : filmService
        return  filmService.consulterFilmParId(filmId);
    }


    /**
     * POST pour créer
     * Ne pas oublier le @RequestBody
     */
    @PostMapping
    public void postFilm(@RequestBody @Valid Film film) {
        filmService.creerFilm(film);
    }

    /**
     * PUT pour modifier
     * Ne pas oublier le @RequestBody
     */
    @PutMapping("/{filmId}")
    public void putFilm(@RequestBody @Valid Film film, @PathVariable("filmId") long filmId) {
        // je m'assure que le film possède l'id correspondant au chemin d'api
        film.setId(filmId);
        // on va s'assurer dans la modification que le film existe en base
        filmService.modifierFilm(film);
    }

    /**
     * DELETE
     * pour supprimer
     */
    @DeleteMapping("/{idFilm}")
    public void supprimerFilm(@PathVariable Long idFilm) {
        filmService.supprimerFilmParId(idFilm);
    }



    /**
     * SOUS-RESOURCE AVIS
     * Est toujours liée à un film
     */

    /**
     * Ajouter un avis à un film
     * POST /{filmId}/avis
     */
    @PostMapping("/{filmId}/avis")
    public void postAvis(@RequestBody @Valid Avis avis, @PathVariable("filmId") long filmId, @AuthenticationPrincipal UtilisateurSpringSecurity utilisateurSpringSecurity) {

        // 1 - on va ajouter à l'avis le membre correspondant à l'utilisateur connecté
        // avis.setMembre(utilisateurConnecte.getMembre());
        // pour l'instant, on ne sait pas se connecter avec l'API => on prend le premier membre qu'on trouve
        avis.setMembre(membreService.consulterMembres().get(0));

        // 2 - j'ajoute l'avis au film à partir de filmService
        filmService.publierAvis(avis, filmId);
    }


}
