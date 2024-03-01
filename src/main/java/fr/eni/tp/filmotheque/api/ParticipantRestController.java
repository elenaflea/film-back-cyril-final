package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bo.Participant;
import fr.eni.tp.filmotheque.bo.Personne;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController// obligatoire afin que le controller soit dans le contexte Spring
@CrossOrigin
@RequestMapping("/api/participants") // tous les @Getmapping/@Postmapping de mon controller auront le chemin de base /films
public class ParticipantRestController {
    @Autowired
    ParticipantService participantService;



    /**
     *GET
     */
    @GetMapping
    public  List<Participant>  getParticipants() {
        return participantService.consulterParticipants();
    }

    /**
     * POST
     */
    @PostMapping
    public void postParticipants(@RequestBody @Valid Participant participant) {
        participantService.creerParticipant(participant);
    }


    /**
     * DELETE
     */
    @DeleteMapping("/{idParticipant}")
    public void supprimerParticipant(@PathVariable Long idParticipant) {
        participantService.supprimerParticipantParId(idParticipant);
    }

    /**
     *PUT
     */
    @PutMapping("/{idParticipant}")
    public void modifierParticipant(@RequestBody @Valid Participant participant, @PathVariable Long idParticipant) {

        // je m'assure que le participant que je vais modifier possède bien un id
        participant.setId(idParticipant);
        // modifierParticipant va verifier avant la sauvegarde que le participant existe en base de données
        participantService.modifierParticipant(participant);
    }


}
