package pt.loual.letranscodeur;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;


import pt.loual.letranscodeur.model.BoiteAEncryptage;
import pt.loual.letranscodeur.outils.GenClef;
import pt.loual.letranscodeur.outils.Transcodeur;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//    @Test
//    public void testClef()
//    {
////        Transcodeur trans = new Transcodeur(BoiteAEncryptage.encrypt("ahba"));
//        GenClef keygen = new GenClef();
//        String str = keygen.randomKey();
//        System.out.println(str);
//        str=BoiteAEncryptage.encrypt(str);
//        System.out.println(str);
//        Assert.assertEquals(true,(BoiteAEncryptage.encrypt(keygen.randomKey())).);
////        Assert.assertArrayEquals(false, Base64.get);
////        Assert.assertNull(trans.getTableauDecode());
////        System.out.println(trans.getTableauEncode());
//    }
}