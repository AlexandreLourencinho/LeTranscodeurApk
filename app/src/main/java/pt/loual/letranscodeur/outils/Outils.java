package pt.loual.letranscodeur.outils;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.widget.EditText;

import androidx.annotation.RequiresApi;

import pt.loual.letranscodeur.LeTranscodeur;
import pt.loual.letranscodeur.R;

public class Outils
{
    public void copierText(Context context, EditText editexte)
    {

        ClipboardManager clavier = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData copie = ClipData.newPlainText(null,editexte.getText().toString());
        clavier.setPrimaryClip(copie);
    }


    public void alerteuh(Context context, int titre, int message, int bouton)
    {
        AlertDialog.Builder alertons = new AlertDialog.Builder(context);
        alertons.setTitle(titre)
                .setMessage(message)
                .setNeutralButton(bouton, ((dialogInterface, i) ->dialogInterface.dismiss()))
                .show();
    }

    /**
     * @param clef
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)//
    public Boolean testClef(String clef)//
    {//
        Transcodeur transverif = new Transcodeur();//
        if (transverif.testTranscodeur(clef, true)) {//
            return true;//
        } else {//
            return false;//
        }//
    }//
//
}
