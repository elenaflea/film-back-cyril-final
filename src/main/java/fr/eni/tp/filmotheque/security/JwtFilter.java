package fr.eni.tp.filmotheque.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private MyUserDetailsService userDetailsService;

    /**
     * Méthode appelée avant la verification de Spring Security
     * C'est là qu'on vérifie le Json Web Token et met à jour le Contexte de sécurité
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1 - on va récupérer deouis la requête HTTP le Json Web Token (toujours encrypté)
            String jwt = jwtUtils.parseJwt(request);

            // 2 - si jamais le JWT est valide (encodé avec le bon secret et le bon algorithme)
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 2.1 - on recupère le nom d'utilisateur depuis le token decrypté
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 2.2 - on recupère l'utilisateur depuis notre base de donnée (à partir de son nom d'utilisateur)
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 2.3 - on va mettre à jour le contexte Spring Security pour lui dire que durant le temp sde la requête HTTP,
                // l'utilisateur connecté est celui qui correspond au token JWT
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return false;
    }
}