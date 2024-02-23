package fr.eni.tp.filmotheque.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe de mapping qui va recupérer les paramètres de notre recherche de films
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class ParametresRecherche {
    private String search; // le texte de notre champs de saisie de recherche
    private Long idGenre; // le genre sur lequel on veut filtrer nos films
    private Integer dureeMin;
    private Integer dureeMax;
    private Integer anneeMin;
    private Integer anneeMax;

    /**
     * Méthode utilitaire pour savoir dans le controller si on a rempli des paramètres de recherche
     */
    public boolean isNotEmpty(){
        return (search != null) ||
                (idGenre != null) ||
                (dureeMin != null) ||
                (dureeMax != null) ||
                (anneeMin != null) ||
                (anneeMax != null);
    }
}
