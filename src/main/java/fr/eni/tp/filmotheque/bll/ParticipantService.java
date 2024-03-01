package fr.eni.tp.filmotheque.bll;

import fr.eni.tp.filmotheque.bo.Participant;

import java.util.List;

/**
 * Interface
 * Sert à SPECIFIER DES FONCTIONNALITES que vont devoir IMPLEMENTER certaines classes
 */
public interface ParticipantService {

    List<Participant> consulterParticipants();

    Participant consulterParticipantParId(long id);

    void supprimerParticipantParId(long id);

    void creerParticipant(Participant participant);

    void modifierParticipant(Participant participant);
}
