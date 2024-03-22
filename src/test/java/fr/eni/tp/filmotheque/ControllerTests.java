package fr.eni.tp.filmotheque;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
// @AutoConfigureTestDatabase : durant la phase de test on va utiliser une base de donnée h2 (en mémoire Java) plutôt que celle de production
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
// @Sql : on va pouvoir lancer un script SQL présent dans le dossier "resources" de mon dossier tests
// avant d'executer les tests
// il va falloir rajouter en plus un fichier personnalisé application.properties qui va contenir notre configuration applicable uniquement durant la phase de test
@Sql(scripts = {"/creation_films.sql"}, executionPhase = BEFORE_TEST_CLASS)

// addFilters = false : permet de ne pas appliquer les filtres de securité sur ce mock (on va pouvoir créer des films)
@AutoConfigureMockMvc(addFilters = false) // va permettre de recupérer une instance de MockMVC qui fait des requêtes sur nos controller
class ControllerTests {

    @Autowired
    private MockMvc mockMvc; // va permettre de faire des requêtes HTTP GET/POST sur les controllers

    /**
     * Je vais tester que quand je fais une requête HTTP GET sur la page des formateurs
     * cela me retourne une réponse OK et que dans le modèle j'ai bien une liste de 4 formateurs (que j'aurai initialisé préalablement)
     */
    @Test
    void testAffichageFilms() throws Exception {
        // je vérifie qu'une requête GET sur la route / est OK
        mockMvc.perform(get("/films")).andExpect(status().isOk())
        // je vérifie en plus que mon controller me mets bien 4 formateurs dans l'attribut de modèle : listeFormateurs
        .andExpect(model().attribute("listeFilms", Matchers.hasSize(3)));
    }

    /**
     * Je vais tester la recherche
     *
     * avec le mot-clé Lucas => doit me retourner 1 film (Star Wars)
     * avec le mot-clé Fant => doit me retourner 2 film (Star Wars et Le seigneur des anneaux)
     * avec le mot-clé ZZZZZ => doit me retourner 0 film sans planter (pas de film trouvé)
     */
    @Test
    void testRechercheFilm() throws Exception {
        // avec le mot-clé Lucas => doit me retourner 1 film (Star Wars)
        mockMvc.perform(get("/films").param("search", "Lucas")).andExpect(status().isOk())
                // je vérifie en plus que mon controller me mets bien 4 formateurs dans l'attribut de modèle : listeFormateurs
                .andExpect(model().attribute("listeFilms", Matchers.hasSize(1)));


        // avec le mot-clé ZZZZZ => doit me retourner 0 film sans planter (pas de film trouvé)
        mockMvc.perform(get("/films").param("search", "ZZZZZ")).andExpect(status().isOk())
                // je vérifie en plus que mon controller me mets bien 4 formateurs dans l'attribut de modèle : listeFormateurs
                .andExpect(model().attribute("listeFilms", Matchers.hasSize(0)));
    }


}