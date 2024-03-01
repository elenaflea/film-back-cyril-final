package fr.eni.tp.filmotheque.bo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* @MappedSuperclass : peut être utilisé à la place d'@Entity pour définir la classe mère dna siune relation d'heritage
* qu'on souhait représenter avec la stratégie TABLE_PER_CLASS (une table par classe)
* Ca permet d'éviter de créer une table pour la classe abstraite
 */
//
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Data @AllArgsConstructor @NoArgsConstructor
public abstract class Personne {
    // Avec une startégie d'héritage TABLE_PER_CLASS il faut mettre pour SQL Server :  @GeneratedValue(strategy = GenerationType.TABLE)
    // note : pour MySQL, mettre GenerationType.AUTO
    @Id @GeneratedValue(strategy = GenerationType.TABLE)
    private long id;
    @NotEmpty
    private String nom;
    @NotEmpty
    private String prenom;

    /**
     * Constructeur sans id
     */
    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }
}
