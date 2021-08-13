package pt.loual.letranscodeur.constantes;

public class Constantes {
    public static final Character[] ALPHABET = {
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            ' ',',','.','!',':','\'','?',':',')','(','-','=','^','0','1','2','3','4','5','6','7','8','9','*','-','+','='
    };

    private static final String SECRET_KEY = "Afpa Rocks!!!!";

    public static String getSecretKey() {
        return SECRET_KEY;
    }
}
