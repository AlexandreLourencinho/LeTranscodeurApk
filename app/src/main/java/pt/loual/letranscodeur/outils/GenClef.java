package pt.loual.letranscodeur.outils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.loual.letranscodeur.constantes.Constantes;

public class GenClef
{

    public String randomKey()
    {
        List<Character> charList = Arrays.asList(Constantes.ALPHABET);

        Collections.shuffle(charList);

        StringBuilder chaineCode = new StringBuilder();
        for (Character car : charList)
        {
            chaineCode.append(car);
        }
        return String.valueOf(chaineCode);
    }

}
