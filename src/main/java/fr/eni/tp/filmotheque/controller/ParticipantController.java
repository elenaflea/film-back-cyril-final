package fr.eni.tp.filmotheque.controller;

import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bo.Participant;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller// obligatoire afin que le controller soit dans le contexte Spring
@RequestMapping("/participants") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class ParticipantController {
    @Autowired
    ParticipantService participantService;

    /**
     * Une méthode annotée @ModelAttribute(nom_attribut)
     * va définir un attribut de modèle accessible dans tous les templates référencés par mon controller
     * Ici, "listePersonnes" n'a pas besoin d'être remis en modèle dans mes @GetMapping/@PostMapping
     * @return
     */
    @ModelAttribute("listePersonnes")
    public  List<Participant> getListeParticipants(){
        return participantService.consulterParticipants();
    }


    /**
     * Est appelé lorsqu'on accède à l'url /participants et qu'on veut afficher le formulaire
     */
    @GetMapping
    public String getFormulaireParticipants(@RequestParam(required=false) Long edit, Model model) {

        // afin d'utiliser le formulaire avec th:object, on crée dans le modèle un attribut "personne"
        model.addAttribute("personne", new Participant());


      // si jamais on a un paramètre "edit" dans la requête HTTP (avce un id de personne), on va mettre en modèle la personne qu'on souhaite modifier
        if (edit != null){
            model.addAttribute("personneEdit", participantService.consulterParticipantParId(edit));
        }


        /*
        * Comme on va utiliser le template personne.html qui est commun avec la gestiond es mmebres
        * Je vais devoir mettre quelques attributs pour définir le titre / la route du formulaire etc...
         */
        model.addAttribute("titre", "Gestion des participants");
        model.addAttribute("titreSectionTable", "Liste des participants");
        model.addAttribute("action", "/participants");

        // 3 - on redirige sur le template personnes.html (commun à la gestion des participants et des membres)
        return "personnes";
    }

    /**
     * Est appelé lorsqu'on valide le formulaire d'ajout de participant
     * Je vais recupérer un participant en paramètre
     */
    @PostMapping
    public String postParticipants(@Valid Participant participant, BindingResult bindingResult) {
        // 1 - si il y a des erreurs de validation, on renvoie le template
        if (bindingResult.hasErrors()){
            return "personnes";
        }

        // Sinon

        // 2.1 - on crée le participant
        participantService.creerParticipant(participant);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des participants
        return "redirect:/participants";
    }


    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des participants
     * Je vais recupérer un id en paramètre d'url
     */
    @GetMapping("/{idParticipant}/supprimer")
    public String supprimerParticipant(@PathVariable Long idParticipant, Model model) {

        Participant participant = participantService.consulterParticipantParId(idParticipant);

        model.addAttribute("message", "Êtes vous sur de vouloir supprimer le participant : " + participant);
        model.addAttribute("action", "/participants/" + idParticipant + "/supprimer");
        model.addAttribute("back", "/participants");


        // 2 - on redirige sur la page qui affiche le formulaire + la liste des participants
        return "confirmation";
    }

    /**
     * Est appelé lorsqu'on clique sur un bouton de suppression de la page des participants
     * Je vais recupérer un id en paramètre d'url
     */
    @PostMapping("/{idParticipant}/supprimer")
    public String supprimerParticipant(@PathVariable Long idParticipant) {

        // 2.1 - on supprime le participant
        participantService.supprimerParticipantParId(idParticipant);

        // 2 - on redirige sur la page qui affiche le formulaire + la liste des participants
        return "redirect:/participants";
    }


}
