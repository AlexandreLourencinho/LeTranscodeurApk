package pt.loual.letranscodeur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import pt.loual.letranscodeur.model.Clefs;
import pt.loual.letranscodeur.outils.Outils;
import pt.loual.letranscodeur.outils.Transcodeur;

public class GestionClefs extends AppCompatActivity
{

    private Spinner spinnerDesClefs;
    private EditText nomDeLaClef;
    private EditText contenuDeLaClef;
    private Button boutonModifierLaClef;
    private Button boutonSupprimerLaClef;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_clefs);

        spinnerDesClefs = findViewById(R.id.spinnerDesClefs);
        nomDeLaClef = findViewById(R.id.nomDeLaClef);
        contenuDeLaClef = findViewById(R.id.contenuDeLaClef);
        boutonModifierLaClef = findViewById(R.id.boutonModifier);
        boutonSupprimerLaClef = findViewById(R.id.boutonSupprimer);


        nomDeLaClef.setFocusable(false);
        nomDeLaClef.setClickable(false);
        contenuDeLaClef.setClickable(false);
        contenuDeLaClef.setClickable(false);
        boutonModifierLaClef.setEnabled(false);
        boutonSupprimerLaClef.setEnabled(false);


        spinnerDesClefs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()//
        {
            @Override//
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)//
            {//

                if (i == 0) {
                    boutonModifierLaClef.setEnabled(false);
                    boutonSupprimerLaClef.setEnabled(false);
                    nomDeLaClef.setText("");
                    contenuDeLaClef.setText("");
                    contenuDeLaClef.setClickable(false);
                    contenuDeLaClef.setClickable(false);
                    boutonModifierLaClef.setEnabled(false);
                    boutonSupprimerLaClef.setEnabled(false);
                } else {
                    boutonModifierLaClef.setEnabled(true);
                    boutonSupprimerLaClef.setEnabled(true);
                    contenuDeLaClef.setClickable(true);
                    contenuDeLaClef.setClickable(true);
                    boutonModifierLaClef.setEnabled(true);
                    boutonSupprimerLaClef.setEnabled(true);

                    Clefs clef = (Clefs) adapterView.getItemAtPosition(i);
                    nomDeLaClef.setText(clef.getNom());
                    contenuDeLaClef.setText(clef.getContenu());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void modifierLaClef()
    {

        Outils test = new Outils();
        if(!test.testClef(contenuDeLaClef.getText().toString())){
            boutonModifierLaClef.setEnabled(false);
            boutonSupprimerLaClef.setEnabled(false);
            test.alerteuh(GestionClefs.this,R.string.titreClefInvalide,R.string.messageClefInvalide,R.string.OK);
        }





    }


}