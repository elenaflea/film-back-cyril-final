package fr.eni.tp.filmotheque.bo;

import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

/**
 * Lombok ne marche pas avec l'heritage
 * => obligé de definir les constructeurs manuellement
 */
@Entity
@NoArgsConstructor
public class Participant extends Personne{
    public Participant(long id, String nom, String prenom) {
        super(id, nom, prenom);
    }

    /**
     * Je surcharge la méthode toString afin d'afficher de base dans TOUS mes templates les participants sous le format "prenom nom"
     */
    @Override
    public String toString() {
        return this.getPrenom() + " " + this.getNom();
    }
}
