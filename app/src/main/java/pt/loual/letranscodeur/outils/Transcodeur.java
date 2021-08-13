package pt.loual.letranscodeur.outils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import pt.loual.letranscodeur.model.BoiteAEncryptage;

public class Transcodeur {

    // tableau permettant le décodage
    private HashMap<String, Character> tableauDecode = new HashMap<>();
    //tableau permettant l'encodage
    private HashMap<Character, String> tableauEncode = new HashMap<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Transcodeur(String clef)
    {
        // initialisation des variables, ici tempchar sera uniquement une variable tampon
        char tempchar;

        System.out.println(clef);
        // décryptage de la clef
        String clearKey = BoiteAEncryptage.decrypt(clef);
        System.out.println(clearKey);
        String debut = "AA";

        // pour chaque character de la clef décryptée
        for (char c : clearKey.toCharArray()) {

            // entrer clef valeur / valeur clef dans les tableaux de codage et décodage
            tableauDecode.put(debut, c);
            tableauEncode.put(c, debut);

            // if permettant l'incrémentation des char et donc de passer de AA à AB, etc...
            // + si le char position (1) est Z, il repasse à A et c'est le premier char (position 0) qui s'incrémente de un
            // permettant de passer de AZ à BA
            if (debut.charAt(1) != 'Z') {
                tempchar = debut.charAt(1);
                tempchar++;
                debut = "" + debut.charAt(0) + tempchar;
            } else {
                tempchar = debut.charAt(0);
                tempchar++;
                debut = "" + tempchar + "A";
            }
        }
    }

    public String encode(String phrase)
    {
        // déclaration de la string qui sera retournée
        String encoded="";
        // strippage des accents
        phrase = StringUtils.stripAccents(phrase);
        // codage de chaque char de la phrase + return la string encodé
        for(char ch : phrase.toCharArray()){
            encoded+=tableauEncode.get(ch);
        }
        return encoded;
    }

    public String decode(String phrase)
    {
        // déclaration du string qui sera retourné
        String decoded="";
        // récupération de la taille de la phrase
        int taille = phrase.length();
// boucle for prenant 2 charactères par deux charactères pour la correspondance tableau
        for(int i=0;i<=taille-2; i=i+2){
            decoded+=tableauDecode.get(phrase.substring(i,i+2));
        }
        return decoded;
    }






}
