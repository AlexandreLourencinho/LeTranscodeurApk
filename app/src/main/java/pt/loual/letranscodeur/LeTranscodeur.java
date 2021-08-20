package pt.loual.letranscodeur;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.TooltipCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import pt.loual.letranscodeur.model.BaseClefs;
import pt.loual.letranscodeur.model.BoiteAEncryptage;
import pt.loual.letranscodeur.model.Clefs;
import pt.loual.letranscodeur.outils.Outils;
import pt.loual.letranscodeur.outils.GenClef;
import pt.loual.letranscodeur.outils.Transcodeur;


public class LeTranscodeur extends AppCompatActivity
{

    private EditText chmpClair;
    private EditText chmpCrypte;
    private EditText chmpClef;
    private Transcodeur trans;
    private GenClef genClef;
    private Button boutonGenererClef;
    private ImageButton boutonSauverClef;
    private EditText champNom;
    private Spinner spinnerClefs;
    private ImageButton copieClair;
    private ImageButton copieCrypte;
    private ImageButton copieClef;
    private MenuItem gestionClefs;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        gestionClefs = findViewById(R.id.gestionClefs);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.gestionClefs:
                Intent intent = new Intent(LeTranscodeur.this,GestionClefs.class);
                startActivity(intent);
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }



    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Outils outils = new Outils();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_transcodeur);
        //||-------------------------------------------------------||
        //||récupération des variables-----------------------------||
        chmpClair = findViewById(R.id.chmpClair);//----------------||
        chmpCrypte = findViewById(R.id.chmpCrypte);//--------------||
        chmpClef = findViewById(R.id.chmpClef);//------------------||
        boutonGenererClef = findViewById(R.id.boutonGenerer);//----||
        boutonSauverClef = findViewById(R.id.boutonSauver);//------||
        spinnerClefs = findViewById(R.id.spinnerClef);//-----------||
        copieClair = findViewById(R.id.copieClair);//--------------||
        copieCrypte = findViewById(R.id.copieCrypte);//------------||
        copieClef = findViewById(R.id.copieClef);//----------------||
        //||-------------------------------------------------------||

        //mise en place des tooltip sur les champs
        //---------------------------------------------------------------------------------------||
        TooltipCompat.setTooltipText(chmpClair, this.getString(R.string.tooltip_encoder));//-----||
        TooltipCompat.setTooltipText(chmpCrypte, this.getString(R.string.tooltip_decoder));//----||
        TooltipCompat.setTooltipText(chmpClef, this.getString(R.string.tooltip_clef));//---------||
        //---------------------------------------------------------------------------------------||


        // création du menu et de la clef par défaut pour le menu spinner---||
        //------------------------------------------------------------------||
        BaseClefs bdd = new BaseClefs(this);//----------------------||
        bdd.creerDefaut();//------------------------------------------------||
        spinnerClefs = outils.menuSelection(spinnerClefs, LeTranscodeur.this);//--------------------------------------------------||
        registerForContextMenu(spinnerClefs);//
        spinnerClefs.setSelection(0);
        //------------------------------------------------------------------||


        //onclicklisteneur checkant la possibilité du codage/décodage-------------------------------------------||
        //------------------------------------------------------------------------------------------------------||
        chmpClair.setOnClickListener(new AdapterView.OnClickListener()//----------------------------------------||
        {//-----------------------------------------------------------------------------------------------------||
            @RequiresApi(api = Build.VERSION_CODES.O)//---------------------------------------------------------||
            @Override//-----------------------------------------------------------------------------------------||
            public void onClick(View view)//--------------------------------------------------------------------||
            {//-------------------------------------------------------------------------------------------------||
                if (chmpClef.getText().toString().equals("")) {//-----------------------------------------------||
                    chmpClair.setEnabled(false);//--------------------------------------------------------------||
                    chmpCrypte.setEnabled(false);//-------------------------------------------------------------||
                } else {//--------------------------------------------------------------------------------------||
                    Outils test = new Outils();
                    if (!test.testClef(chmpClef.getText().toString())) {//------------------------||
                        chmpClair.setText("");//----------------------------------------------------------------||
                        chmpClair.setEnabled(false);//----------------------------------------------------------||
                        chmpCrypte.setEnabled(false);//---------------------------------------------------------||
                        AlertDialog.Builder alaaarm = new AlertDialog.Builder(LeTranscodeur.this);//-----||
                        alaaarm.setTitle(R.string.titreClefInvalide)//------------------------------------------||
                                .setMessage(R.string.messageClefInvalide)//-------------------------------------||
                                .setNeutralButton(R.string.OK, (dialogInterface, i) -> dialogInterface.dismiss())
                                .show();//----------------------------------------------------------------------||
                    }//-----------------------------------------------------------------------------------------||
                }//---------------------------------------------------------------------------------------------||
            }//-------------------------------------------------------------------------------------------------||
        });//---------------------------------------------------------------------------------------------------||
        //------------------------------------------------------------------------------------------------------||


        //
        //
        chmpClair.addTextChangedListener(new TextWatcher()//
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {

            }

            //
            //
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.O)//
            @Override//
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {//
                if (chmpClair.isFocused()) {
                    if (chmpClef.getText().toString().equals("")) {//
                        //
                        desactiver();
                    }//
                    else {//
                        activer();
//                        System.out.println(chmpClef.getText().toString());//
                        StringBuilder sb = new StringBuilder();
                        for(String ligne : chmpClair.getText().toString().split("\n")){
                            trans = new Transcodeur(chmpClef.getText().toString());//
                            sb.append(ligne);
                            chmpCrypte.setText(trans.encode(sb.toString()));//
                        }

                    }//
                }
            }//
            //

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        //
        //
        chmpCrypte.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            //
            //
            @RequiresApi(api = Build.VERSION_CODES.O)//
            @Override//
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {//
                if (chmpCrypte.isFocused()) {//
                    if (chmpClef.getText().toString().equals("")) {//
                        desactiver();
                    } else {//
                        activer();
                        StringBuilder sb = new StringBuilder();
                        for( String ligne : chmpCrypte.getText().toString().split("\n")){
                            trans = new Transcodeur(chmpClef.getText().toString());//
                            sb.append(ligne);
                            chmpClair.setText(trans.decode(sb.toString()));//
                        }

                    }//
                }//
            }//
            //

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        //
        //
        chmpClef.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            //
            //
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override//
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {//
                Outils test = new Outils();
                if (chmpClef.getText().toString().equals("") || !test.testClef(chmpClef.getText().toString()) ) {//
                    desactiver();
                } else {//
                    activer();
                }//
            }//
            //

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

        //
        //
        spinnerClefs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()//
        {
            @Override//
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)//
            {//
                if (i == 0) {//
                    chmpClef.setText("");//
                } else {//
                    Clefs clef = (Clefs) adapterView.getItemAtPosition(i);//
                    chmpClef.setText(clef.getContenu());//
                }//
            }//
            //

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                chmpClef.setText("");
            }
        });

        //
        //
        boutonGenererClef.setOnClickListener(this::genererClef);//
        boutonSauverClef.setOnClickListener(this::sauverClef);//
        //

    }


    /**
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)//
    public void genererClef(View view)//
    {//
        genClef = new GenClef();//
        chmpClef.setText(BoiteAEncryptage.encrypt(genClef.randomKey()));//
    }//
    //

    /**
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.O)//
    public void sauverClef(View view)//
    {//
        if (chmpClef.getText().toString().equals("")) {//
            Outils alerte = new Outils();//
            alerte.alerteuh(LeTranscodeur.this, R.string.titreErreurClefVide,//
                    R.string.erreurClefVide, R.string.OK);//
        } else {//
            Outils test = new Outils();
            if (!test.testClef(chmpClef.getText().toString())) {//
                Outils alerte = new Outils();//
                alerte.alerteuh(LeTranscodeur.this, R.string.titreClefInvalide, R.string.messageClefInvalide, R.string.OK);//
            } else {//
                AlertDialog.Builder alerte = new AlertDialog.Builder(this);//
                champNom = new EditText(this);//
                alerte.setTitle(R.string.titreEntrerNom)//
                        .setMessage(R.string.entrerNom)//
                        .setView(champNom)//
                        .setPositiveButton(R.string.sauver, (dialogInterface, i) ->//
                        {//
                            if (champNom.getText().toString().equals("")) {//
                                dialogInterface.dismiss();//
                                AlertDialog.Builder alert = new AlertDialog.Builder(LeTranscodeur.this);//
                                alert.setTitle(R.string.titreNomVide)//
                                        .setMessage(R.string.nomVide)//
                                        .setPositiveButton(R.string.OK, (dialogInterface1, i1) ->//
                                        {//
                                            dialogInterface1.dismiss();//
                                            sauverClef(view);//
                                        })//
                                        .show();//
                            } else {//
                                String nom = champNom.getText().toString();//
                                Clefs clef = new Clefs(nom, chmpClef.getText().toString());//
                                BaseClefs bdd = new BaseClefs(this);//
                                HashMap<Boolean, String> resultat = bdd.ajouter(clef);//
                                //
                                if (resultat.containsValue(false)) {//
                                    dialogInterface.dismiss();//
                                    //
                                    Outils alarme = new Outils();//
                                    alarme.alerteuh(LeTranscodeur.this, R.string.titreErreurInsertion, R.string.erreurInsertion, R.string.OK);//
                                } else {//
                                    Outils alarme = new Outils();//
                                    alarme.alerteuh(LeTranscodeur.this, R.string.titreReussiteInsertion, R.string.reussiteInsertion, R.string.OK);//
                                    //
                                    alarme.menuSelection(spinnerClefs, LeTranscodeur.this);//
                                }//
                            }//
                        })//
                        .setNegativeButton(R.string.annuler, (dialogInterface, i) -> dialogInterface.dismiss())//
                        .show();//
            }//
        }//
    }//
    //




    /**
     * @param view
     */
    public void copierCrypte(View view)//
    {//
        Outils copier = new Outils();//
        copier.copierText(LeTranscodeur.this, chmpCrypte);//
    }//
    //

    /**
     * @param view
     */
    public void copierClair(View view)//
    {//
        Outils copier = new Outils();//
        copier.copierText(LeTranscodeur.this, chmpClair);//
    }//
    //

    /**
     * @param view
     */
    public void copierClef(View view)//
    {//
        Outils copier = new Outils();//
        copier.copierText(LeTranscodeur.this, chmpClef);//
    }//




    /**
     *
     */
    public void desactiver()//
    {//
        chmpClair.setEnabled(false);//
        chmpClair.setBackgroundResource(R.color.grey_200);//
        chmpCrypte.setEnabled(false);//
        chmpCrypte.setBackgroundResource(R.color.grey_200);//
        chmpCrypte.setHint(R.string.conseilCrypt);
        chmpClair.setHint(R.string.conseilClair);
    }//
    //

    /**
     *
     */
    public void activer()//
    {//
        chmpClair.setEnabled(true);//
        chmpClair.setBackgroundResource(R.color.white);//
        chmpCrypte.setEnabled(true);//
        chmpCrypte.setBackgroundResource(R.color.white);//
        chmpCrypte.setHint(R.string.vide);
        chmpClair.setHint(R.string.vide);
    }//
    //
}