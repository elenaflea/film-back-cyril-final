package fr.eni.tp.filmotheque.controller;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bll.GenreService;
import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.dto.ParametresRecherche;
import fr.eni.tp.filmotheque.security.UtilisateurSpringSecurity;
import fr.eni.tp.filmotheque.bo.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller// obligatoire afin que le controller soit dans le contexte Spring
@RequestMapping("/films") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class FilmController {

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


    /**
     * Une méthode annotée @ModelAttribute(nom_attribut)
     * va définir un attribut de modèle accessible dans tous les templates référencés par mon controller
     * Ici, "listeGenres" n'a pas besoin d'être remis en modèle dans mes @GetMapping/@PostMapping
     * @return
     */
    @ModelAttribute("listeGenres")
    public  List<Genre> getListeGenres(){
        return genreService.consulterGenres();
    }

    /**
     * Une méthode annotée @ModelAttribute(nom_attribut)
     * va définir un attribut de modèle accessible dans tous les templates référencés par mon controller
     * Ici, "listeParticipants" n'a pas besoin d'être remis en modèle dans mes @GetMapping/@PostMapping
     * @return
     */
    @ModelAttribute("listeParticipants")
    public  List<Participant> getListeParticipants(){
        return participantService.consulterParticipants();
    }

    /**
     * Est appelé lorsqu'on accède à l'url /films (page qui liste les films dans une table)
     */
    @GetMapping
    public String getFilms(ParametresRecherche parametresRecherche, Model model) {

        // 1 - on va vérifier si on a des paramètres de recherche
        if (parametresRecherche.isNotEmpty()){
            // si on a des paramètres de recherche, on va filtrer en fonction des paramètres
            model.addAttribute("listeFilms", filmService.rechercherFilms(parametresRecherche));
        }
        // sinon, on affiche tous les films
        else{
            model.addAttribute("listeFilms", filmService.consulterFilms());
        }

       // 2 - on remet les paramètres de recherche dans le model (afin de les afficher dans la vue)
        model.addAttribute("parametresRecherche", parametresRecherche);

        // 3 - on redirige sur le template films.html
        return "films";
    }

    /**
     * Est appelé lorsqu'on accède à l'url /films/{filmId} (page qui affiche les détails d'un film)
     * Je recupère l'id du film depuis l'url /{filmId} avec @PathVariable("filmId")
     */
    @GetMapping("/{filmId}")
    public String getFilmDetail(@PathVariable("filmId") long filmId, Model model) {
        // 1 - on va aller chercher le film depuis le service : filmService
        Film film = filmService.consulterFilmParId(filmId);

        // 2 - on va mettre ce film dans le modèle afin de pouvoir y accéder dans notre template
        model.addAttribute("film", film);

        // 3 - on redirige sur le template filmDetails.html
        return "filmDetail";
    }

    /**
     * Est appelé lorsqu'on accède à l'url /films/creer (page qui affiche le formulaire d'ajout de film)
     */
    @GetMapping("/creer")
    public String getFilmForm(Model model) {
        // 1 - on met dans le modèle un objet avis film qui va être referencé par le formulaire du template
        model.addAttribute("film", new Film());

        // 2 - on redirige sur le template filmCreation.html
        return "filmCreation";
    }

    /**
     * Est appelé lorsqu'on valide le formulaire d'ajout de film
     * Je recupère le film rempli avec th:object directement dans le paramètre Film film
     */
    @PostMapping("/creer")
    public String postFilmForm(@Valid Film film, BindingResult bindingResult) {

        // 1 - je vérifie que je n'ai pas d'erreur
        if (bindingResult.hasErrors()){
            // si c'est le cas, je renvoie sur le template qui va afficher les erreurs
            return "filmCreation";
        }

        // 2 - sinon, j'ajoute le film à partir de filmService
        filmService.creerFilm(film);

        // 3 - on redirige en GET (:/redirect) sur la page des films
        return "redirect:/films";
    }

    /**
     * Est appelé lorsqu'on accède à l'url /films/{filmId}/edit (page qui affiche le formulaire de modification d'un film)
     * Je recupère l'id du film depuis l'url /{filmId} avec @PathVariable("filmId")
     */
    @GetMapping("/{filmId}/edit")
    public String getFilmEditForm(@PathVariable("filmId") long filmId, Model model) {
        // 1 - on va aller chercher le film depuis le service : filmService
        Film film = filmService.consulterFilmParId(filmId);

        // 2 - on va mettre ce film dans le modèle afin de pouvoir y accéder dans notre template et le référencer avec th:object
        model.addAttribute("film", film);

        // 4 - on redirige sur le template filmCreation.html
        return "filmCreation";
    }

    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des films
     * Je vais recupérer un id en paramètre d'url
     */
    @GetMapping("/{idFilm}/supprimer")
    public String supprimerFilm(@PathVariable Long idFilm, Model model) {

        Film film = filmService.consulterFilmParId(idFilm);

        model.addAttribute("message", "Êtes vous sur de vouloir supprimer le film : " + film.getTitre());
        model.addAttribute("action", "/films/" + idFilm + "/supprimer");
        model.addAttribute("back", "/films");


        // 2 - on redirige sur une page de confirmation ou l'utilisateur va devoir valider son choix
        return "confirmation";
    }

    /**
     * Est appelé lorsqu'on a validé dans le template "confirmation" qu'on souhaite effectuer la suppression du film
     * Je vais recupérer un id en paramètre d'url
     */
    @PostMapping("/{idFilm}/supprimer")
    public String supprimerFilm(@PathVariable Long idFilm) {

        // 2.1 - on supprime le film
        filmService.supprimerFilmParId(idFilm);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des films
        return "redirect:/films";
    }

    /**
     * Est appelé lorsqu'on accède à l'url /films/{filmId}/avis (page qui affiche le formulaire d'ajout d'avis d'un film)
     * Je recupère l'id du film depuis l'url /{filmId} avec @PathVariable("filmId")
     */
    @GetMapping("/{filmId}/avis")
    public String getAvisForm(@PathVariable("filmId") long filmId, Model model) {
        // 1 - on va aller chercher le film depuis le service : filmService
        Film film = filmService.consulterFilmParId(filmId);

        // 2 - on va mettre ce film dans le modèle afin de pouvoir y accéder dans notre template
        model.addAttribute("film", film);

        // 3 - on met dans le modèle un objet avis vide qui va être referencé par le formulaire du template
        model.addAttribute("avis", new Avis());

        // 4 - on redirige sur le template avis.html
        return "avis";
    }

    /**
     * Est appelé lorsqu'on valide le formulaire d'ajout d'avis
     * Je recupère l'id du film depuis l'url /{filmId} avec @PathVariable("filmId")
     * Je recupère l'avis rempli avec th:object directement dans le paramètre AVis avis
     *
     * @AuthenticationPrincipal nous permet de recupérer l'utilisateur connecté
     */
    @PostMapping("/{filmId}/avis")
    public String postAvis(@PathVariable("filmId") long filmId, Avis avis, @AuthenticationPrincipal UtilisateurSpringSecurity utilisateurConnecte) {
        // 1 - on va ajouter à l'avis le membre correspondant à l'utilisateur connecté
        avis.setMembre(utilisateurConnecte.getMembre());

        // 2 - j'ajoute l'avis au film à partir de filmService
        filmService.publierAvis(avis, filmId);

        // 3 - on redirige en GET (:/redirect) sur la page de détail du film
        return "redirect:/films/" + filmId;
    }
}
