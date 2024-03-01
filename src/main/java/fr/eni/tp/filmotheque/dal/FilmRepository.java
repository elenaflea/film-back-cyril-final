package fr.eni.tp.filmotheque.dal;

import fr.eni.tp.filmotheque.bo.Film;
import fr.eni.tp.filmotheque.bo.Participant;
import fr.eni.tp.filmotheque.dto.ParametresRecherche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/* Spring va AUTOMATIQUEMENT créer une classe qui implémente cette
 * interface avec les méthodes save()/findAll()/etc...
 * et la rendre disponible dans le contexte comme un bean */
public interface FilmRepository extends JpaRepository<Film, Long> {

    /**
     * Méthode qui renvoie une liste de films filtrés par les paramètres de recherche passés en argument
     * Ici, j'utilise du JPQL plutôt que du SQL natif (je n'ai pas mis nativeQuery = true)
     * Le seul désavantage c'est que je ne peux pas rechercher dans la liste des acteurs
     */
    @Query(
            // on recupère tous les films
            "select f from Film f " +

            // qui ont le paramètre de recherche #{#param.search} SOIT :

            // non specifié
            "where ( :#{#param.search} is null OR " +

            // dans le titre
            "( f.titre like %:#{#param.search}% " +

                    // dans le titre du genre
            "or f.genre.titre like :#{#param.search}% " +
                    // dans le prenom du réalisateur
            "or f.realisateur.prenom like :#{#param.search}% " +
                    // dans le nom du réalisateur
            "or f.realisateur.nom like :#{#param.search}% ) )" +

            // ET EN PLUS si le genre est défini, je filtre par genre
            "AND ( :#{#param.idGenre} is null OR f.genre.id = :#{#param.idGenre}) " +

            // ET EN PLUS si l'annéeMin  est défini, je filtre par annéeMin
            "AND ( :#{#param.anneeMin} is null OR f.annee >= :#{#param.anneeMin}) " +

            // ET EN PLUS si l'annéeMax  est définie, je filtre par annéeMin
            "AND ( :#{#param.anneeMax} is null OR f.annee <= :#{#param.anneeMax}) " +

            // ET EN PLUS si la durée Min est définie, je filtre par annéeMin
            "AND ( :#{#param.dureeMin} is null OR f.duree >= :#{#param.dureeMin}) " +

            // ET EN PLUS si la durée Max  est définie, je filtre par durée Max
            "AND ( :#{#param.dureeMax} is null OR f.duree <= :#{#param.dureeMax})")


    List<Film> rechercher(ParametresRecherche param);
}
