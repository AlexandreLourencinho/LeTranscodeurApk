package pt.loual.letranscodeur.outils;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.EditText;

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
}
