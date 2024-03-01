package fr.eni.tp.filmotheque.bll.jpa;

import fr.eni.tp.filmotheque.bll.MembreService;
import fr.eni.tp.filmotheque.bo.Membre;
import fr.eni.tp.filmotheque.dal.MembreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MembreServiceJpaImpl implements MembreService {
    /**
     * Plutôt que de gérer une liste de membres dans le service,
     * Je vais me servir de MembreRepository pour créer/recupérer les membres depuis la base de donnée
     */
    @Autowired
    private MembreRepository membreRepository;

    // on injecte le bean PasswordEncoder qui a été défini dans la configuration Spring Security
    // on va s'en servir pour encoder le mot de passe du membre créé
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Membre recupererMembre(String pseudo) {
         List<Membre> membres = membreRepository.findAll();
        // pour l'instant, on n'arrirve pas à recupérer un membre par pseudo
        // donc on va chercher tous les membres en base et on fait une itération
        for (Membre membre : membres) {
            if (membre.getPseudo().equals(pseudo)) {
                return membre;
            }
        }
        return null;
    }

    @Override
    public List<Membre> consulterMembres() {
        // on recupère les membres depuis la base de donnée
        return membreRepository.findAll();
    }

    @Override
    public Membre consulterMembreParId(long id) {
        // On recupère un membre depuis la base de donnée

        // findById renvoie un objet de type Optional<Membre> (c'est à dire : un membre potentiel)
        /*
        * *
        *  2 façon de gérer ca :
        *  - soit on rajoute .get() => va renvoyer l'élément si présent, sinon va lancer une exception : NoSuchElementException
        * - soit on gère nous même le cas ou y'a pas d'éléments
        *
        * Optional<Membre> reponse = membreRepository.findById(id);
        * if (reponse.isEmpty()){
        *     return null;
        * }
        * else{
        *     return reponse.get();
        * }
         */
        return membreRepository.findById(id).get();
    }

    @Override
    public void supprimerMembreParId(long id) {
        membreRepository.deleteById(id);
    }

    @Override
    public void creerMembre(Membre membre){
        // VALIDATION
        // on va vérifier qu'aucun membre avec ce pseudo n'existe déjà
        Membre membreAVecLeMemePseudo = recupererMembre(membre.getPseudo());

        // si un membre différent (id différent) existe avec ce pseudo : on balance une exception
        if (membreAVecLeMemePseudo != null && membreAVecLeMemePseudo.getId() != membre.getId()){
            // on balance une exception (qui sera recupérée par Thymeleaf)
            // TODO : gérer ca avec un affichage dans le formulaire
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un membre avec ce pseudo existe déjà");
        }

        // avant de créer le membre dans la base de donnée, il va falloir encoder son mot de passe
        // en effet, Spring Security va comparer le mot de passe de connecion qu'il aura encodé avec celui présent en base
        membre.setMotDePasse(passwordEncoder.encode(membre.getMotDePasse()));
        // On crée le membre dans la base de donnée
        membreRepository.save(membre);
    }
}
