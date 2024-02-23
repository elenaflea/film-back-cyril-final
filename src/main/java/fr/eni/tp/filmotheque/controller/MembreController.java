package fr.eni.tp.filmotheque.controller;

import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller// obligatoire afin que le controller soit dans le contexte Spring
@RequestMapping("/membres") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class MembreController {
    @Autowired
    MembreService membreService;

    /**
     * Une méthode annotée @ModelAttribute(nom_attribut)
     * va définir un attribut de modèle accessible dans tous les templates référencés par mon controller
     * Ici, "listePersonnes" n'a pas besoin d'être remis en modèle dans mes @GetMapping/@PostMapping
     * @return
     */
    @ModelAttribute("listePersonnes")
    public  List<Membre> getListeMembres(){
        return membreService.consulterMembres();
    }


    /**
     * Est appelé lorsqu'on accède à l'url /membres et qu'on veut afficher le formulaire
     */
    @GetMapping
    public String getFormulaireMembres(@RequestParam(required=false) Long edit, Model model) {

        // afin d'utiliser le formulaire avec th:object, on crée dans le modèle un attribut "personne"
        model.addAttribute("personne", new Membre());


      // si jamais on a un paramètre "edit" dans la requête HTTP (avce un id de personne), on va mettre en modèle la personne qu'on souhaite modifier
        if (edit != null){
            model.addAttribute("personneEdit", membreService.consulterMembreParId(edit));
        }


        /*
        * Comme on va utiliser le template personne.html qui est commun avec la gestiond es mmebres
        * Je vais devoir mettre quelques attributs pour définir le titre / la route du formulaire etc...
         */
        model.addAttribute("titre", "Gestion des membres");
        model.addAttribute("titreSectionTable", "Liste des membres");
        model.addAttribute("action", "/membres");

        // 3 - on redirige sur le template personnes.html (commun à la gestion des membres et des membres)
        return "personnes";
    }

    /**
     * Est appelé lorsqu'on valide le formulaire d'ajout de membre
     * Je vais recupérer un membre en paramètre
     */
    @PostMapping
    public String postMembres(@Valid Membre membre, BindingResult bindingResult, Model model) {
        // 1 - si il y a des erreurs de validation, on renvoie le template
        if (bindingResult.hasErrors()){
            return "personnes";
        }

        // Sinon

        try{
            // 2.1 - on crée le membre
            membreService.creerMembre(membre);
        }
        // si il y a une erreur, on redirige sur le template
        // TODO : améliorer pour se servir de l'exception standard catchée par Theamelaf
        catch(Exception e){
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("titre", "Gestion des membres");
            model.addAttribute("titreSectionTable", "Liste des membres");
            model.addAttribute("action", "/membres");
            model.addAttribute("personne", membre);
            return "personnes";
        }

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des membres
        return "redirect:/membres";
    }


    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des membres
     * Je vais recupérer un id en paramètre d'url
     */
    @GetMapping("/{idMembre}/supprimer")
    public String supprimerMembre(@PathVariable Long idMembre, Model model) {

        Membre membre = membreService.consulterMembreParId(idMembre);

        model.addAttribute("message", "Êtes vous sur de vouloir supprimer le membre : " + membre);
        model.addAttribute("action", "/membres/" + idMembre + "/supprimer");
        model.addAttribute("back", "/membres");


        // 2 - on redirige sur la page qui affiche le formulaire + la liste des membres
        return "confirmation";
    }

    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des membres
     * Je vais recupérer un id en paramètre d'url
     */
    @PostMapping("/{idMembre}/supprimer")
    public String supprimerMembre(@PathVariable Long idMembre) {

        // 2.1 - on supprime le membre
        membreService.supprimerMembreParId(idMembre);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des membres
        return "redirect:/membres";
    }


}
