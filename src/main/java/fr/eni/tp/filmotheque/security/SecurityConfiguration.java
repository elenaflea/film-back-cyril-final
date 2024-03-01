package fr.eni.tp.filmotheque.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration // Nécessaire lorsqu'on définit des @Bean dans une classe
@EnableWebSecurity
public class SecurityConfiguration {

    /**
     * Correspond au filtre qui va être executé en amont de chaque verification Spring security
     * Ce filtre va décoder le token JWT et mettre à jour le contexte Spring Security au besoin
     */
    @Autowired
    private JwtFilter jwtFilter;

    /**
     * On définit un filtre de type SecurityFilterChain
     * qui va être ajouté au contexte Spring (car on a mis @Bean)
     *
     * Spring Security va comprendre que c'est ce filtre qu'il va devoir utiliser
     * à la place de son filtre par défaut (qui n'autorise aucune requête non authentifie)
     *
     * dans .requestMatchers, on peut mettre :
     * - soit une url : .requestMatchers("/pageConnecte")
     * - soit une liste d'url : .requestMatchers("/pageConnecte1", "/pageConnect2", "/pageConnecte3")
     * - soit une url avec un wildcard (*) : .requestMatchers("/prive/*")
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // on ignore la vérification csrf sur les requêtes d'API (pas de risque car pas de Cookies)
        http.csrf( (csrf) -> csrf.ignoringRequestMatchers("/api/**"));

        // on autorise la requête http entrante en fonction de ces critères :
        http.authorizeHttpRequests((authorize) -> authorize
                    // si jamais la requête veut aller sur l'url "/pageConnecte" : alors on doit être authentifié (.authenticated())
                    .requestMatchers(regexMatcher("/films/.*/avis")).authenticated()
                    // si jamais la requête veut aller sur l'url "/pageAdmin" : alors on doit avoir le rôle admin (.hasRole("admin"))
                    .requestMatchers("/films/creer").hasRole("admin")
                    .requestMatchers("/genres").hasRole("admin")
                    .requestMatchers("/membres").hasRole("admin")
                     .requestMatchers("/participants").hasRole("admin")
                    // pour toute les autres requête : on autorise sans contraintes
                    .requestMatchers("/**").permitAll()
                )
                // on utilise une authentification basique (utilisateur / mot de passe)
                .httpBasic(Customizer.withDefaults())
                // on utilise le formulaire par défaut proposé par Spring Security
                .formLogin(Customizer.withDefaults())
                // quand on se déconnecte=> on redirige vers l'accueil
                .logout((logout) -> logout.logoutSuccessUrl("/"));

        /*****************************************************************
         * AVANT DE FAIRE LA VERIFICATION DE SECURITE, ON AJOUTE UN FILTRE
         * qui va vérifier la présence ou non d'un Json Web Token
         *
         * il va remplir le contexte Spring Security
         * à partir du token JWT
         ******************************************************************/
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    /**
     * On définit un bean pour la gestion des utilisateurs en mémoire (solution temporaire)
     *
     * A partir du moment ou Spring voit qu'il y a un @Bean de type InMemoryUserDetailsManager dans le contexte Spring
     * Il va l'utiliser pour chercher les utilisateurs

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        // On a une liste d'utilisateurs Spring Security (interface UserDetails)
        List<UserDetails> userDetailsList = new ArrayList<>();
        // on définit un utilisateur avec nom "membre", mot de passe "membre123", role : "user"
        userDetailsList.add(
                User.withUsername("membre").password(passwordEncoder().encode("membre123"))
                .roles("user").build());
        // on définit un utilisateur avec nom "admin", mot de passe "admin123", role : "admin", "user"
        userDetailsList.add( User.withUsername("admin").password(passwordEncoder().encode("admin123"))
                .roles("admin", "user").build());

        return new InMemoryUserDetailsManager(userDetailsList);

    }*/
}