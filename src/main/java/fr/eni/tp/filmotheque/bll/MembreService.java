package fr.eni.tp.filmotheque.bll;

import fr.eni.tp.filmotheque.bo.Membre;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

public interface MembreService {
    public Membre recupererMembre(String pseudo);

    List<Membre> consulterMembres();

    Membre consulterMembreParId(long id);

    void supprimerMembreParId(long id);

    void creerMembre(Membre participant) throws Exception;
}
