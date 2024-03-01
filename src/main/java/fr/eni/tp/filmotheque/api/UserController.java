package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.bo.Membre;
import fr.eni.tp.filmotheque.security.JwtUtils;
import fr.eni.tp.filmotheque.security.MyUserDetailsService;
import fr.eni.tp.filmotheque.security.UtilisateurSpringSecurity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    @Autowired
    private MyUserDetailsService userDetailsService;

    @GetMapping
    public Membre getConnectedUser(HttpServletRequest request) throws Exception {
        // 1 - je recupère le nom d'utilisateur correspondant au token JWT de la requête
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(request));
        // 2 - je recupère l'utilisateur Spring Security correspondant à ce nom d'utilisateur
        UtilisateurSpringSecurity user = (UtilisateurSpringSecurity) userDetailsService.loadUserByUsername(username);

        // 3 - je renvoie le membre correspondant
        return user.getMembre();
    }
}