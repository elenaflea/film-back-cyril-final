package fr.eni.tp.filmotheque.security;

import fr.eni.tp.filmotheque.bo.Membre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * On crée notre PROPRE classe d'utilisateur qui va pouvoir être utilisée par Spring security
 * pour la gestion de l'authentification / autorisations
 *
 * Pour que ca soit possible, il FAUT que cette classe respecte certaines contraintes
 * => elle doit implémenter l'interface UserDetails
 *
 * Notre utilisateur Spring security va être un "wrapper" qui va possèder un attribut de type "Membre"
 * => quand on recuperera un utilisateur Spring security en session, on pourra avoir le mmebre associé
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class UtilisateurSpringSecurity implements UserDetails {
    /*
    * un utilisateur Spring Security va possèder un membre (qui sera donc accessible lorsqu'on recuperera l'utilisateur connexcté)
     */
    private Membre membre;

    /**
     * Spring Security doit pouvoir savoir comment il recupère la liste des Permissions (GrantedAuthority)
     * => à partir du membre
     *
     * note : dans Spring security, on a Permission = "ROLE_xxx" correspond au rôle xxx
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // si jamais le mebre possède l'attribut admin à true
        // => il a le rôle admin (= la permission "ROLE_admin") + le rôle user (= la permission "ROLE_user")
        if (membre.isAdmin()) {
            return List.of(new SimpleGrantedAuthority("ROLE_admin"), new SimpleGrantedAuthority("ROLE_user"));
        }
        // sinon il a le role "user"
        return List.of(new SimpleGrantedAuthority("ROLE_user"));
    }

    /**
     * Spring Security doit pouvoir savoir comment il recupère le mot de passe
     * => à partir du membre
     */
    @Override
    public String getPassword() {
        return membre.getMotDePasse();
    }

    /**
     * Spring Security doit pouvoir savoir comment il recupère le nom d'utilisateur
     * => à partir du membre
     */
    @Override
    public String getUsername() {
        return membre.getPseudo();
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur a son compte non expiré
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur est non bloqué o
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur a ses identifiés non expirés
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Spring Security doit pouvoir savoir si notre utilisateur est activé ou non
     * => on va metre "true" tout le temps
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
