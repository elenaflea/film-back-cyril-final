package fr.eni.tp.filmotheque.bo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Avis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int note;
    private String commentaire;

    /**
     * attributs qui représentent des associations
     */
    @ManyToOne // En suivant dans le diagramme de classe les cardinalités, on a Film (*) -> (1) Membre   = Many(*) To One
    private Membre membre;
}
