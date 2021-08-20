package pt.loual.letranscodeur;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import pt.loual.letranscodeur.model.BaseClefs;
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


//        nomDeLaClef.setFocusable(false);
        nomDeLaClef.setClickable(false);
//        contenuDeLaClef.setClickable(false);
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
                    nomDeLaClef.setFocusable(true);
                    nomDeLaClef.setClickable(true);
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

        contenuDeLaClef.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

                Outils test = new Outils();
                if(!test.testClef(contenuDeLaClef.getText().toString())){
                    boutonModifierLaClef.setEnabled(false);
                }else{
                    boutonModifierLaClef.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        Outils outils = new Outils();
        outils.menuSelection(spinnerDesClefs,GestionClefs.this);
        registerForContextMenu(spinnerDesClefs);
        spinnerDesClefs.setSelection(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void modifierLaClef(View view)
    {
        if(view==boutonModifierLaClef){
            Outils test = new Outils();
            if(!test.testClef(contenuDeLaClef.getText().toString())){
                boutonModifierLaClef.setEnabled(false);
                boutonSupprimerLaClef.setEnabled(false);
                test.alerteuh(GestionClefs.this,R.string.titreClefInvalide,R.string.messageClefInvalide,R.string.OK);
            }else{

                BaseClefs bdd = new BaseClefs(this);//
                Clefs clef = new Clefs();
                Clefs clefRecup = (Clefs) spinnerDesClefs.getSelectedItem();
                int id = clefRecup.getId();
                clef.setId(id);
                clef.setNom(nomDeLaClef.getText().toString());
                clef.setContenu(contenuDeLaClef.getText().toString());
//                System.out.println(clef.getId());
                HashMap<Boolean,Object> resultat = bdd.modifier(clef);
                bdd.close();
                if(resultat.get(false)!=null){
                    test.alerteuh(GestionClefs.this,R.string.titreErreurModif,R.string.messageErreurModif,R.string.OK);
                }else{
                    test.alerteuh(GestionClefs.this,R.string.titreReussiteModif,R.string.messageReussiteModif,R.string.OK);
//                    AlertDialog.Builder tente = new AlertDialog.Builder(GestionClefs.this);
//                    tente.setMessage(String.valueOf(clef.getId())).show();
                    int positionSpinner = spinnerDesClefs.getSelectedItemPosition();
                    Outils outil = new Outils();
                    outil.menuSelection(spinnerDesClefs,GestionClefs.this);
                    registerForContextMenu(spinnerDesClefs);
                    spinnerDesClefs.setSelection(positionSpinner);
                }
            }
        }
    }



    public void supprimerLaClef(View view)
    {
        if(view == boutonSupprimerLaClef){
            BaseClefs bdd = new BaseClefs(this);//
            Clefs clef = (Clefs)spinnerDesClefs.getSelectedItem();
            HashMap<Boolean,Object> resultat = bdd.supprimer(clef);
            bdd.close();
            if(resultat.get(false)!=null){
                Outils outil = new Outils();
                outil.alerteuh(GestionClefs.this,R.string.titreErreurSuppression,R.string.messageErreurSuppression,R.string.OK);

            }else{
                Outils outil = new Outils();
                outil.alerteuh(GestionClefs.this,R.string.titreSuppressionReussie,R.string.messageSuppressionReussie,R.string.OK);
                outil.menuSelection(spinnerDesClefs,GestionClefs.this);
                registerForContextMenu(spinnerDesClefs);
                spinnerDesClefs.setSelection(0);
            }
        }
    }


}