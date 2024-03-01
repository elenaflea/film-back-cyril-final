package fr.eni.tp.filmotheque.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtUtils {
    private String jwtSecret = "secret";

    /**
     * On va génerer un JWT depuis l'utilisateur courant en incluant son nom d'utilisateur dans le payload
     * Pour cela, on va se baser sur la bibliothèque JWT ('com.auth0:java-jwt:4.0.0')
     */
    public String generateJwtToken(Authentication authentication) {
        UtilisateurSpringSecurity userPrincipal = (UtilisateurSpringSecurity) authentication.getPrincipal();
        // JWT.create().withClaim(ATTR) crée un token JWT en mettant dans le payload l'attribut ATTR
        return JWT.create().withClaim("username", userPrincipal.getUsername()).sign(Algorithm.HMAC256(jwtSecret));
    }

    /**
     * On va recupérer depuis le token un attribut (username) mis dans le payload
     * Pour cela, on va se baser sur la bibliothèque JWT ('com.auth0:java-jwt:4.0.0')
     */
    public String getUserNameFromJwtToken(String token) {
        // JWT.require().verify().getClaim(ATTR) : recupère l'attribut  ATTR contenu dans le token JWT decrypté
        return JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(token).getClaim("username").toString().replaceAll("\"", "");
    }

    /**
     * On va recupérer l'entête Authorization de notre requête HTTP
     * et le JWT va correspondre à la chaîne de caractère après Bearer XXXXXXX
     */
    public static String parseJwt(HttpServletRequest request) {
        // 1 - on recupère l'entête "Authorization"
        String headerAuth = request.getHeader("Authorization");

        // 2 - on recupère à la chaîne de caractère après Bearer XXXXXXX
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7, headerAuth.length());
        }
        return null;
    }

    /**
     * On va valider que le token correspond à l'algorithme choisi (HMAC256) et au secret définit dans l'attribut jwtSecret
     * Pour cela, on va se baser sur la bibliothèque JWT ('com.auth0:java-jwt:4.0.0')
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // JWT.require().verify() : vérifie qu'un token est valide et conforme à l'algorithme et secret défini
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(authToken);
            return true;
        } catch (Exception e) {
            System.out.println("error : " + e.getStackTrace());
        }
        return false;
    }
}