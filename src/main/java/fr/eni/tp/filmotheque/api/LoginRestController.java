package fr.eni.tp.filmotheque.api;

import fr.eni.tp.filmotheque.bo.Membre;
import fr.eni.tp.filmotheque.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/login")
public class LoginRestController {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @PostMapping
    public String login(@RequestBody Membre membre) throws Exception {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(membre.getPseudo(), membre.getMotDePasse());
        Authentication authentication = authenticationConfiguration.getAuthenticationManager().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // j'appèle la méthode qui génère un JWT à partir du contexte Spring d'autentification
        String jwt = jwtUtils.generateJwtToken(authentication);

        // je retourne ce token
        return jwt;
    }
}