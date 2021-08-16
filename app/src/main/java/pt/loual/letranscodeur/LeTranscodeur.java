package pt.loual.letranscodeur;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import java.util.ArrayList;
import java.util.HashMap;

import pt.loual.letranscodeur.model.BaseClefs;
import pt.loual.letranscodeur.model.BoiteAEncryptage;
import pt.loual.letranscodeur.model.Clefs;
import pt.loual.letranscodeur.outils.GenClef;
import pt.loual.letranscodeur.outils.Transcodeur;


public class LeTranscodeur extends AppCompatActivity
{

    private EditText chmpEncode;
    private EditText chmpDecode;
    private EditText chmpClef;
    private Transcodeur trans;
    private GenClef genClef;
    private Button boutonGenererClef;
    private Button boutonSauverClef;
    private EditText champNom;
    private Spinner spinnerClefs;
    private ArrayList<Clefs> arrayListClefs;
    private ArrayAdapter<Clefs> adapterListeClefs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_transcodeur);
        chmpEncode = findViewById(R.id.chmpDecode);
        chmpDecode = findViewById(R.id.chmpEncode);
        chmpClef = findViewById(R.id.chmpClef);
        boutonGenererClef = findViewById(R.id.boutonGenerer);
        boutonSauverClef = findViewById(R.id.boutonSauver);
        spinnerClefs = findViewById(R.id.spinnerClef);

        TooltipCompat.setTooltipText(chmpEncode, this.getString(R.string.tooltip_encoder));
        TooltipCompat.setTooltipText(chmpDecode, this.getString(R.string.tooltip_decoder));
        TooltipCompat.setTooltipText(chmpClef, this.getString(R.string.tooltip_clef));

        BaseClefs bdd = new BaseClefs(this);
        bdd.creerDefaut();
        arrayListClefs = bdd.liste();
        adapterListeClefs = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, this.arrayListClefs);
        spinnerClefs.setAdapter(adapterListeClefs);
        registerForContextMenu(spinnerClefs);
        spinnerClefs.setSelection(0);


        chmpEncode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (chmpClef.getText().toString().equals("")) {
                    if (chmpEncode.isFocused()) {
                        chmpEncode.setEnabled(false);
                        chmpDecode.setEnabled(false);
                    }
                }else {
                    chmpEncode.setEnabled(true);
                    chmpDecode.setEnabled(true);
                    System.out.println(chmpClef.getText().toString());
                    trans = new Transcodeur(chmpClef.getText().toString());
                    chmpDecode.setText(trans.encode(chmpEncode.getText().toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        chmpDecode.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (chmpDecode.isFocused()) {
                    if (chmpClef.getText().toString().equals("")) {
                        chmpEncode.setEnabled(false);
                        chmpDecode.setEnabled(false);
                    } else {
                        trans = new Transcodeur(chmpClef.getText().toString());
                        chmpEncode.setText(trans.decode(chmpDecode.getText().toString()));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        chmpClef.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(chmpClef.getText().toString().equals("")){
                    chmpEncode.setEnabled(false);
                    chmpDecode.setEnabled(false);
                }else{
                    chmpEncode.setEnabled(true);
                    chmpDecode.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        spinnerClefs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0) {
                    chmpClef.setText("");
                } else {
                    Clefs clef = (Clefs) adapterView.getItemAtPosition(i);
                    chmpClef.setText(clef.getContenu());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                chmpClef.setText("");
            }
        });

        boutonGenererClef.setOnClickListener(this::genererClef);
        boutonSauverClef.setOnClickListener(this::sauverClef);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void genererClef(View view)
    {
        genClef = new GenClef();
        chmpClef.setText(BoiteAEncryptage.encrypt(genClef.randomKey()));
    }

    public void sauverClef(View view)
    {
        if (chmpClef.getText().toString().equals("")) {
            AlertDialog.Builder alertons = new AlertDialog.Builder(LeTranscodeur.this);
            alertons.setTitle(R.string.titreErreurClefVide)
                    .setMessage(R.string.erreurClefVide)
                    .setNeutralButton(R.string.OK, ((dialogInterface, i) ->
                    {
                        dialogInterface.dismiss();
                    }))
                    .show();
        } else {
            if(!testClef(chmpClef.getText().toString())){
                AlertDialog.Builder alarm = new AlertDialog.Builder(LeTranscodeur.this);
                alarm.setTitle(R.string.titreClefInvalide)
                        .setMessage(R.string.messageClefInvalide)
                        .setNegativeButton(R.string.OK,(dialogInterface, i) ->dialogInterface.dismiss())
                        .show();

            }else{
                AlertDialog.Builder alerte = new AlertDialog.Builder(this);
                champNom = new EditText(this);
                alerte.setTitle(R.string.titreEntrerNom)
                        .setMessage(R.string.entrerNom)
                        .setView(champNom)
                        .setPositiveButton(R.string.sauver, (dialogInterface, i) ->
                        {
                            if (champNom.getText().toString().equals("")) {
                                dialogInterface.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(LeTranscodeur.this);
                                alert.setTitle(R.string.titreNomVide)
                                        .setMessage(R.string.nomVide)
                                        .setPositiveButton(R.string.OK, (dialogInterface1, i1) ->
                                        {
                                            dialogInterface1.dismiss();
                                            sauverClef(view);
                                        })
                                        .show();
                            } else {
                                String nom = champNom.getText().toString();
                                Clefs clef = new Clefs(nom, chmpClef.getText().toString());
                                BaseClefs bdd = new BaseClefs(this);
                                HashMap<Boolean, String> resultat = bdd.ajouter(clef);
                                if (resultat.containsValue(true)) {
                                    dialogInterface.dismiss();
                                    String str = resultat.get(false);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(LeTranscodeur.this);
                                    alert.setMessage(R.string.erreurInsertion + " " + str)
                                            .setTitle(R.string.titreErreurInsertion)
                                            .setNeutralButton(R.string.OK, ((dialogInterface1, i1) ->
                                            {
                                                dialogInterface1.dismiss();
                                            }))
                                            .show();
                                } else {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(LeTranscodeur.this);
                                    alert.setMessage(R.string.reussiteInsertion)
                                            .setTitle(R.string.titreReussiteInsertion)
                                            .setPositiveButton(R.string.OK, ((dialogInterface1, i1) ->
                                            {
                                                dialogInterface1.dismiss();
                                            }))
                                            .show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.annuler, (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }



        }
    }

    public Boolean testClef(String clef)
    {
        if(chmpClef.getText().toString().matches("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$") && chmpClef.length()%4 ==0){
            return true;
        }else{
            return false;
        }
    }


}