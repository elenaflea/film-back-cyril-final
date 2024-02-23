package fr.eni.tp.filmotheque.controller;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bll.GenreService;
import fr.eni.tp.filmotheque.bo.Avis;
import fr.eni.tp.filmotheque.bo.Film;
import fr.eni.tp.filmotheque.bo.Genre;
import fr.eni.tp.filmotheque.bo.Participant;
import fr.eni.tp.filmotheque.security.UtilisateurSpringSecurity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller// obligatoire afin que le controller soit dans le contexte Spring
@RequestMapping("/genres") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class GenreController {
    @Autowired
    GenreService genreService;

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
     * Est appelé lorsqu'on accède à l'url /genres et qu'on veut afficher le formulaire
     */
    @GetMapping
    public String getFormulaireGenres(@RequestParam(required=false) Long edit, Model model) {

        // afin d'utiliser le formulaire avec th:object, on crée dans le modèle un attribut "genre"
        model.addAttribute("genre", new Genre());

        if (edit != null){
            model.addAttribute("genreEdit", genreService.consulterGenreParId(edit));
        }

        // 3 - on redirige sur le template films.html
        return "genres";
    }

    /**
     * Est appelé lorsqu'on valide le formulaire d'ajout de genre
     * Je vais recupérer un genre en paramètre
     */
    @PostMapping
    public String postGenres(@Valid Genre genre, BindingResult bindingResult) {
        // 1 - si il y a des erreurs de validation, on renvoie le template
        if (bindingResult.hasErrors()){
            return "genres";
        }

        // Sinon

        // 2.1 - on crée le genre
        genreService.creerGenre(genre);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des genres
        return "redirect:/genres";
    }


    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des genres
     * Je vais recupérer un id en paramètre d'url
     */
    @GetMapping("/{idGenre}/supprimer")
    public String supprimerGenre(@PathVariable Long idGenre, Model model) {

        Genre genre = genreService.consulterGenreParId(idGenre);

        model.addAttribute("message", "Êtes vous sur de vouloir supprimer le genre : " + genre.getTitre());
        model.addAttribute("action", "/genres/" + idGenre + "/supprimer");
        model.addAttribute("back", "/genres");


        // 2 - on redirige sur la page qui affiche le formulaire + la liste des genres
        return "confirmation";
    }

    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des genres
     * Je vais recupérer un id en paramètre d'url
     */
    @PostMapping("/{idGenre}/supprimer")
    public String supprimerGenre(@PathVariable Long idGenre) {

        // 2.1 - on supprime le genre
        genreService.supprimerGenreParId(idGenre);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des genres
        return "redirect:/genres";
    }


}
