package fr.eni.tp.filmotheque.converter;

import fr.eni.tp.filmotheque.bll.FilmService;
import fr.eni.tp.filmotheque.bll.ParticipantService;
import fr.eni.tp.filmotheque.bo.Participant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * PARTOUT ou Spring va avoir besoin de convertir du texte => Adresse
 * Il va utiliser ce converter (à condition que celui-ci soit dans le contexte Spring)
 */
@Component // Ne pas oublier de mettre @Component afin que le converter soit dans le contexte Spring
public class ParticipantConverter implements Converter<String, Participant> {

    @Autowired
    private ParticipantService participantService;

    /**
     * On définit ici comment à partir d'un id de participant au format texte envoyée dans la requête HTTP
     * Spring va pouvoir recupérer une instance de participant
     * afin de remplir correctement l'objet Film dans le @PostMapping de FilmController
     */
    @Override
    public Participant convert(String idFormatTexte) {
        long idFormatLong = Long.parseLong(idFormatTexte);
        return participantService.consulterParticipantParId(idFormatLong);
    }
}
