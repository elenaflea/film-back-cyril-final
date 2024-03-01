package fr.eni.tp.filmotheque.bll.jpa;

import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bo.Participant;
import fr.eni.tp.filmotheque.dal.ParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Profile("prod")
public class ParticipantServiceJpaImpl implements ParticipantService {
    /**
     * Plutôt que de gérer une liste de participants dans le service,
     * Je vais me servir de ParticipantRepository pour créer/recupérer les participants depuis la base de donnée
     */
    @Autowired
    private ParticipantRepository participantRepository;

    @Override
    public List<Participant> consulterParticipants() {
        // on recupère les participants depuis la base de donnée
        return participantRepository.findAll();
    }

    @Override
    public Participant consulterParticipantParId(long id) {
        // On recupère un participant depuis la base de donnée

        // findById renvoie un objet de type Optional<Participant> (c'est à dire : un participant potentiel)
        /*
        * *
        *  2 façon de gérer ca :
        *  - soit on rajoute .get() => va renvoyer l'élément si présent, sinon va lancer une exception : NoSuchElementException
        * - soit on gère nous même le cas ou y'a pas d'éléments
        *
        * Optional<Participant> reponse = participantRepository.findById(id);
        * if (reponse.isEmpty()){
        *     return null;
        * }
        * else{
        *     return reponse.get();
        * }
         */
        return participantRepository.findById(id).get();
    }

    @Override
    public void supprimerParticipantParId(long id) {

        // on vérifie d'abord que le participant existe
        if (participantRepository.existsById(id)){
            participantRepository.deleteById(id);
        }
        // si ca n'est pas le cas => on lance une exception
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le participant n'a pas été trouvé");
        }
    }

    @Override
    public void creerParticipant(Participant participant) {
        System.out.println("creerParticipant() : à compléter");
        // On crée le participant dans la base de donnée
        participantRepository.save(participant);
    }

    @Override
    public void modifierParticipant(Participant participant) {
        // on vérifie d'abord que le participant existe
        if (participantRepository.existsById(participant.getId())){
            participantRepository.save(participant);
        }
        // si ca n'est pas le cas => on lance une exception
        else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "le participant n'existe pas");
        }
    }
}
