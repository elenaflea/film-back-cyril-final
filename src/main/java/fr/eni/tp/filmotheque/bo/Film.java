package fr.eni.tp.filmotheque.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Film {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank @Size(max = 250)// le titre ne doit pas être vide (@NotBlank fonctione uniquement sur du texte) et max de 250 caractère
    private String titre;
    @NotNull @Min(1900) // l'année doit renseignée être supérieure à 1900
    private int annee;
    @Min(1) // la durée doit être supérieure à 0 (minimum : 1)
    private int duree;

    @Size(min = 20, max = 250) // le synopsis doit faire entre 20 et 250 caractère
    private String synopsis;

    /**
     * attributs qui représentent des associations
     */
    @NotNull // le genre doit être non null
    /*
    * Cardinalités
    * Il suffit de lire dans le diagramme de classe les cardinalités en suivant le sens FIlm -> ClasseAddociée
     */
    @ManyToOne // On a Film (*) -> (1) Genre   = Many(*) To One
    private Genre genre;

    // on a envie de supprimer les avis quand on supprime le film
    @OneToMany(cascade = CascadeType.REMOVE) // On a Film (1) -> (*) Avis   = One To  Many(*)
    @JoinColumn(name = "film_id") // plutôt que d'avoir une table de jointure, je préfère avoir une colonne supplémentaire "film_id" sur la table Avis
    @ToString.Exclude // permet de ne pas afficher les avis dans le toString() des tests (fait planter les tests)
    private List<Avis> avis = new ArrayList<>();

    @ToString.Exclude // permet de ne pas afficher les acteurs dans le toString() des tests (fait planter les tests)
    @ManyToMany // On a Film (*) -> (*) Participant   =  Many(*) To  Many(*)
    private List<Participant> acteurs = new ArrayList<>();

    @ManyToOne // On a Film (*) -> (1) Participant   = Many(*) To One
    private Participant realisateur;

    /**
     * Pour les constructeurs particuliers (en dehors de ceux avec tous/aucun argument)
     * on n'utilise pas Lombok
     */
    public Film(long id, String titre, int annee, int duree, String synopsis) {
        this.id = id;
        this.titre = titre;
        this.annee = annee;
        this.duree = duree;
        this.synopsis = synopsis;
    }
}
