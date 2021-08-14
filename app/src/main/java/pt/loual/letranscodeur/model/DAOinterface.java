package pt.loual.letranscodeur.model;

import java.util.ArrayList;
import java.util.HashMap;

public interface DAOinterface<T> {

    HashMap<Boolean,Object> ajouter(T o);
    HashMap<Boolean,Object> modifier(T o);
    HashMap<Boolean,Object> supprimer(T o);
    ArrayList<T> liste();
    T trouverUn(T o);
}
