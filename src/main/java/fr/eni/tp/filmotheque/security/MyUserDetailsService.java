package fr.eni.tp.filmotheque.security;

import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * A partir du moment ou on a dans le contexte Spring un service qui implémente UserDetailsService
 * => Spring security va l'utiliser pour aller chercher les utilisateurs dans le processus d'authentification
 */
@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MembreService membreService;

    /**
     * Comment est-ce qu'on va chercher un utilisateur Spring Security à partir d'un pseudo?
     * => à partir du service membreService
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Membre membre = membreService.recupererMembre(username);

        // si le membre n'est pas trouvé : je lance une exception afin que Spring Security affiche une erreur dans le formulaire
        if (membre == null){
            throw new UsernameNotFoundException(username);
        }

        // si on a trouvé un membre, je retourne un utilisateur Spring Security qui englobe le membre
        return new UtilisateurSpringSecurity(membre);
    }
}