package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.api.doc.SwaggerDoc;
import fr.eni.tp.filmotheque.bll.GenreService;
import fr.eni.tp.filmotheque.bo.Genre;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "API Genres", description = SwaggerDoc.DESC_GENERIQUE + "genres")
@RestController// obligatoire afin que le controller soit dans le contexte Spring
@CrossOrigin
@RequestMapping("/api/genres")
public class GenreRestController {
    @Autowired
    GenreService genreService;

    /**
     * GET
     */
    @GetMapping
    public  List<Genre>  getGenres() {
        // bonne pratique - ne pas appeler directement la couche DAL depuis le controller, mais passer par une couche service (où seront fait les validations, traitements metiers)
       return genreService.consulterGenres();
    }

    /**
     * POST
     * On laisse l'annotation @Valid
     * => l'API va nous retourer une erreur 400 si le genre n'est pas valide
     */
    @PostMapping
    public void postGenres(@RequestBody @Valid Genre genre) {
        // on crée le genre
        genreService.creerGenre(genre);
    }

    /**
     * PUT
     * On laisse l'annotation @Valid
     * => l'API va nous retourer une erreur 400 si le genre n'est pas valide
     */
    @PutMapping("/{idGenre}")
    public void putGenres(@RequestBody @Valid Genre genre, @PathVariable Long idGenre) {
        // je m'assure que le genre que je vais modifier possède bien un id
        genre.setId(idGenre);
        // modifierGenre va verifier avant la sauvegarde que le genre existe en base de données
        genreService.modifierGenre(genre);
    }

    /**
     * DELETE
     */
    @DeleteMapping("/{idGenre}")
    public void supprimerGenre(@PathVariable Long idGenre) {
        genreService.supprimerGenreParId(idGenre);
    }
}
