package fr.eni.tp.filmotheque.dal;

import fr.eni.tp.filmotheque.bo.Genre;
import fr.eni.tp.filmotheque.bo.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

/* Spring va AUTOMATIQUEMENT créer une classe qui implémente cette
 * interface avec les méthodes save()/findAll()/etc...
 * et la rendre disponible dans le contexte comme un bean */
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
