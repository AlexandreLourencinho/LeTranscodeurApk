package pt.loual.letranscodeur.model;

import androidx.annotation.NonNull;

public class Clefs {


    private int id;
    private String nom;
    private String contenu;

    public Clefs() {
    }

    public Clefs(int id, String nom, String contenu) {
        this.id = id;
        this.nom = nom;
        this.contenu = contenu;
    }

    public Clefs(String nom, String contenu) {
        this.nom = nom;
        this.contenu = contenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nom;
    }
}
