package fr.eni.tp.filmotheque.bo;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor
@Entity
public class Membre extends Personne{
    private String pseudo;
    @ToString.Exclude // permet de ne pas afficher le mot de passe dans le toString()
    private String motDePasse;
    private boolean admin = false;

    /**
     * Lombok ne marche pas avec l'heritage
     * => obligé de definir les constructeurs manuellement
     */
    public Membre(long id, String nom, String prenom, String pseudo, String motDePasse) {
        super(id, nom, prenom);
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
    }

    /**
     * Lombok ne marche pas avec l'heritage
     * => obligé de definir les constructeurs manuellement
     */
    public Membre(long id, String nom, String prenom, String pseudo, String motDePasse, boolean admin) {
        super(id, nom, prenom);
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.admin = admin;
    }

    /**
     * Lombok ne marche pas avec l'heritage
     * => obligé de definir les constructeurs manuellement
     */
    public Membre(String nom, String prenom, String pseudo, String motDePasse, boolean admin) {
        super(nom, prenom);
        this.pseudo = pseudo;
        this.motDePasse = motDePasse;
        this.admin = admin;
    }
}
