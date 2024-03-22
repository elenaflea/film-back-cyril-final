package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.api.doc.SwaggerDoc;
import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "API Membres", description = SwaggerDoc.DESC_GENERIQUE + "membres")
@RestController// obligatoire afin que le controller soit dans le contexte Spring
@CrossOrigin
@RequestMapping("/api/membres") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class MembreRestController {
    @Autowired
    MembreService membreService;


    /**
     * GET
     */
    @GetMapping
    public  List<Membre>  getFormulaireMembres() {
        return membreService.consulterMembres();
    }

    /**
     * POST
     */
    @PostMapping
    public void postMembres(@RequestBody @Valid Membre membre) {
        membreService.creerMembre(membre);
    }


    /**
     * DELETE : supprimer
     */
    @DeleteMapping("/{idMembre}")
    public void supprimerMembre(@PathVariable Long idMembre) {
        membreService.supprimerMembreParId(idMembre);
    }

}
