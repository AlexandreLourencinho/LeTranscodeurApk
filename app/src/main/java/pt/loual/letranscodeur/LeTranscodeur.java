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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import java.util.HashMap;
import pt.loual.letranscodeur.model.BaseClefs;
import pt.loual.letranscodeur.model.BoiteAEncryptage;
import pt.loual.letranscodeur.model.Clefs;
import pt.loual.letranscodeur.outils.Outils;
import pt.loual.letranscodeur.outils.GenClef;
import pt.loual.letranscodeur.outils.Transcodeur;


public class LeTranscodeur extends AppCompatActivity implements View.OnClickListener
{

    //--------------------------------------------------------------------------------------------||
    // --------------- Déclaration centralisée des éléments utilisés dans la classe --------------||
    //--------------------------------------------------------------------------------------------||
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
    //**************************fin déclaration éléments de la classe ****************************||


    /**
     * -------------------------------------------------------------------------------------------||
     * ------------------ Méthode permettant la gestion du menu 3 points ------------------------||
     *
     * @param menu Le menu 3 points en question
     * @return un booléen
     * -------------------------------------------------------------------------------------------||
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        gestionClefs = findViewById(R.id.gestionClefs);
        return true;
    }
    //**************************fin gestion menu 3 points ****************************************||


    /**
     * -------------------------------------------------------------------------------------------||
     * ---- Méthode permettant la gestion des actions quand une option du menu est séléctionnée --||
     *
     * @param item un paramètre MenuItem
     * @return un booléen
     * -------------------------------------------------------------------------------------------||
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.gestionClefs:
                Intent intent = new Intent(LeTranscodeur.this, GestionClefs.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //**************************fin du onitemselected ********************************************||


    /**-------------------------------------------------------------------------------------------||
     * --------------------- Méthode oncreate de l'activité --------------------------------------||
     * @param savedInstanceState bundle obligatoire du oncreate de l'activité
     * -------------------------------------------------------------------------------------------||
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Outils outils = new Outils();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_transcodeur);

        //----------------------------------------------------------------------------------------||
        //-------------------------------récupération des variables-------------------------------||
        //----------------------------------------------------------------------------------------||
        chmpClair = findViewById(R.id.chmpClair);
        chmpCrypte = findViewById(R.id.chmpCrypte);
        chmpClef = findViewById(R.id.chmpClef);
        boutonGenererClef = findViewById(R.id.boutonGenerer);
        boutonSauverClef = findViewById(R.id.boutonSauver);
        spinnerClefs = findViewById(R.id.spinnerClef);
        copieClair = findViewById(R.id.copieClair);
        copieCrypte = findViewById(R.id.copieCrypte);
        copieClef = findViewById(R.id.copieClef);
        //*****************************fin récupération des variables ****************************||


        //----------------------------------------------------------------------------------------||
        //--------------------mise en place des tooltip sur les champs----------------------------||
        //----------------------------------------------------------------------------------------||
        TooltipCompat.setTooltipText(chmpClair, this.getString(R.string.tooltip_encoder));
        TooltipCompat.setTooltipText(chmpCrypte, this.getString(R.string.tooltip_decoder));
        TooltipCompat.setTooltipText(chmpClef, this.getString(R.string.tooltip_clef));
        //****************************************************************************************||


        //----------------------------------------------------------------------------------------||
        //---------création du menu et de la clef par défaut pour le menu spinner-----------------||
        //----------------------------------------------------------------------------------------||
        BaseClefs bdd = new BaseClefs(this);
        bdd.creerDefaut();
        spinnerClefs = outils.menuSelection(spinnerClefs, LeTranscodeur.this);
        registerForContextMenu(spinnerClefs);
        spinnerClefs.setSelection(0);
        //************************fin création menu et clef par défaut ***************************||


        //----------------------------------------------------------------------------------------||
        //----------------onclicklisteneur checkant la possibilité du codage/décodage-------------||
        //----------------------------------------------------------------------------------------||
        chmpClair.setOnClickListener(this);
        copieClair.setOnClickListener(this);
        copieCrypte.setOnClickListener(this);
        copieClef.setOnClickListener(this);
        boutonGenererClef.setOnClickListener(this);
        boutonSauverClef.setOnClickListener(this);
        //****************************************************************************************||


        //----------------------------------------------------------------------------------------||
        // -------------------- Listener sur le champ du texte en clair --------------------------||
        //----------------------------------------------------------------------------------------||
        chmpClair.addTextChangedListener(new TextWatcher()//
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {

            }

            // *** sur le changement de texte dans le champ : check si la clef est vide ou non ***
            @SuppressLint("ResourceAsColor")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                checkClefVide(chmpClair, chmpCrypte);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        //************************fin du textchange listener du chmp clair ***********************||


        //----------------------------------------------------------------------------------------||
        // -------------------- Listener sur le champ du texte codé ------------------------------||
        //----------------------------------------------------------------------------------------||
        chmpCrypte.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            // *** sujr le changement de texte, vérifie si la clef est bien présente ***
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override//
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {//
//                if (chmpCrypte.isFocused()) {
//                    if (chmpClef.getText().toString().equals("")) {
//                        desactiver();
//                    } else {
//                        activer();
//                        StringBuilder sb = new StringBuilder();
//                        for (String ligne : chmpCrypte.getText().toString().split("\n")) {
//                            trans = new Transcodeur(chmpClef.getText().toString());
//                            sb.append(ligne);
//                            chmpClair.setText(trans.decode(sb.toString()));
//                        }
//
//                    }
//                }
                checkClefVide(chmpCrypte, chmpClair);
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        //************************fin du textchange listener du chmp crypté **********************||


        //----------------------------------------------------------------------------------------||
        // -------------- Listener sur le champ du champ texte de la clef ------------------------||
        //----------------------------------------------------------------------------------------||
        chmpClef.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            // ***  active ou désactive les champs en fonction de la validité de la clef ***
            @SuppressLint("ResourceType")
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)//
            {
                activerOuDesactiver();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        //************************fin du textchange listener du chmp clef*************************||

        //----------------------------------------------------------------------------------------||
        // -------------- listener sur la séléction d'objet dans le spinner ----------------------||
        //----------------------------------------------------------------------------------------||
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
        //***************************fin du select item listener du spinner **********************||
    }
    //****************************fin du OnCreate ************************************************||

    //--------------------------------------------------------------------------------------------||
    // ---------Gestion des Onclick avec redirection vers la méthode correspondante---------------||
    //--------------------------------------------------------------------------------------------||
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view)
    {
        Outils copier = new Outils();

        switch (view.getId()) {
            case (R.id.chmpClair):
                testClefValide();
                break;
            case (R.id.boutonGenerer):
                genererClef();
                break;
            case (R.id.boutonSauver):
                sauverClef();
                break;
            case (R.id.copieClair):
                copier.copierText(LeTranscodeur.this, chmpClair);
                break;
            case (R.id.copieCrypte):
                copier.copierText(LeTranscodeur.this, chmpCrypte);
                break;
            case (R.id.copieClef):
                copier.copierText(LeTranscodeur.this, chmpClef);
                break;
        }
    }
    //***************************fin de la fonction onclick **************************************||


    /**
     * -------------------------------------------------------------------------------------------||
     * -----------------méthode pour les textchangelistener : verrouille, encode -----------------||
     * -------------------------------------ou décode---------------------------------------------||
     *
     * @param champsPremier l'editText qui sera focus et dont le texte sera récupéré
     * @param champSecond   l'EditTexte qui recevra le codage ou le décodage
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void checkClefVide(EditText champsPremier, EditText champSecond)
    {
        if (champsPremier.isFocused()) {
            if (chmpClef.getText().toString().equals("")) {
                activerOuDesactiver();
            } else {
                activerOuDesactiver();
                StringBuilder sb = new StringBuilder();
                for (String ligne : champsPremier.getText().toString().split("\n")) {
                    trans = new Transcodeur(chmpClef.getText().toString());
                    sb.append(ligne);
                    if (champSecond.getId() == R.id.chmpCrypte) {
                        champSecond.setText(trans.encode(sb.toString()));
                    } else if (champSecond.getId() == R.id.chmpClair) {
                        champSecond.setText(trans.decode(sb.toString()));
                    }
                }
            }
        }
    }
    //*********************fin de la méthode de vérification de présence de clef *****************||


    /**
     * -------------------------------------------------------------------------------------------||
     * ------------------méthode permettant de générer une clef de transcodeur -------------------||
     * -------------------------------------------------------------------------------------------||
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void genererClef()
    {
        genClef = new GenClef();
        chmpClef.setText(BoiteAEncryptage.encrypt(genClef.randomKey()));
    }
    //************************fin de la méthode de création de clef ******************************||


    /**
     * -------------------------------------------------------------------------------------------||
     * ------------------------Méthode permettant de lancer la sauvegarde de clefs----------------||
     * -------------------------------------------------------------------------------------------||
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sauverClef()
    {
        if (chmpClef.getText().toString().equals("")) {
            // *** si le champ de la clef est vide  ***
            Outils alerte = new Outils();
            alerte.alerteuh(LeTranscodeur.this, R.string.titreErreurClefVide,
                    R.string.erreurClefVide, R.string.OK);
            // ***              -----               ***
        } else {
            // *** si le champ de la clef n'est pas vide => test de la clef  ***
            Outils test = new Outils();
            if (!test.testClef(chmpClef.getText().toString())) {
                // *** si la clef ne passe pas le test de validité ***
                Outils alerte = new Outils();
                alerte.alerteuh(LeTranscodeur.this, R.string.titreClefInvalide,
                        R.string.messageClefInvalide, R.string.OK);
                // ***     -----     -----     -----    -----      ***
            } else {
                // *** si la clef passe le test de validité ***
                AlertDialog.Builder alerte = new AlertDialog.Builder(this);
                champNom = new EditText(this);
                alerte.setTitle(R.string.titreEntrerNom)
                        .setMessage(R.string.entrerNom)
                        .setView(champNom)
                        .setPositiveButton(R.string.sauver, (dialogInterface, i) ->
                        {
                            // *** en cas d'appui sur le bouton sauver de l'alerte ***
                            if (champNom.getText().toString().equals("")) {
                                // *** si le champ du nom de la clef est vide ***
                                // *** pop d'une alerte rappelant que le nom est obligatoire
                                // *** puis retour au début de la fonction afin de redemander un nom
                                dialogInterface.dismiss();
                                AlertDialog.Builder alert = new AlertDialog.Builder(
                                        LeTranscodeur.this);
                                alert.setTitle(R.string.titreNomVide)
                                        .setMessage(R.string.nomVide)
                                        .setPositiveButton(R.string.OK, (dialogInterface1, i1) ->
                                        {
                                            dialogInterface1.dismiss();
                                            sauverClef();
                                        })
                                        .show();
                                // ***    -----   -----  ------   ------   ------   ***
                            } else {
                                // *** si le champ nom est bien rempli ***
                                // *** récupération du nom de la clef, création d'un nouvel
                                // *** objet Clef et tentative d'ajout à la base SQLite
                                String nom = champNom.getText().toString();
                                Clefs clef = new Clefs(nom, chmpClef.getText().toString());
                                BaseClefs bdd = new BaseClefs(this);
                                HashMap<Boolean, String> resultat = bdd.ajouter(clef);

                                if (resultat.containsValue(false)) {
                                    // *** si le HashMap contient false, la clef n'est pas ajoutée
                                    // *** => message d'erreur
                                    dialogInterface.dismiss();
                                    System.out.println(resultat.get(false).toString());
                                    Outils alarme = new Outils();
                                    alarme.alerteuh(LeTranscodeur.this,
                                            R.string.titreErreurInsertion, R.string.erreurInsertion,
                                            R.string.OK);
                                    // *** -----    -----     -----    -----    -----    -----   --
                                } else {
                                    // *** sinon, la clef à bien été ajoutée , alerte avec la confirmation de l'ajout
                                    Outils alarme = new Outils();
                                    alarme.alerteuh(LeTranscodeur.this,
                                            R.string.titreReussiteInsertion,
                                            R.string.reussiteInsertion, R.string.OK);
                                    // *** mise à jour du menu déroulant / spinner
                                    alarme.menuSelection(spinnerClefs, LeTranscodeur.this);
                                    // ***   -----     -----    ------     -----     -----    ------
                                }
                            }
                        })
                        // *** si bouton annulé => simplement fermeture de l'alerte
                        .setNegativeButton(R.string.annuler,
                                (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }
            // *** -----     ------      -----     -----     ------    ------    -----    -----  ***
        }
    }
    //*****************************fin de la méthode de sauvegarde de clef ***********************||


    /**
     * -------------------------------------------------------------------------------------------||
     * ----------------- Méthode permettant d'activer ou désactiver les champs  ------------------||
     * -------------------------------------------------------------------------------------------||
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void activerOuDesactiver()
    {
        Outils test = new Outils();
        if (chmpClef.getText().toString().equals("") || !test.testClef(
                chmpClef.getText().toString())) {
            chmpClair.setEnabled(false);
            chmpClair.setBackgroundResource(R.color.grey_200);
            chmpCrypte.setEnabled(false);
            chmpCrypte.setBackgroundResource(R.color.grey_200);
            chmpCrypte.setHint(R.string.conseilCrypt);
            chmpClair.setHint(R.string.conseilClair);
        } else {
            chmpClair.setEnabled(true);
            chmpClair.setBackgroundResource(R.color.white);
            chmpCrypte.setEnabled(true);
            chmpCrypte.setBackgroundResource(R.color.white);
            chmpCrypte.setHint(R.string.vide);
            chmpClair.setHint(R.string.vide);
        }
    }
    //**********************fin de la méthode activer / désactiver champs ************************||


    /**
     * -------------------------------------------------------------------------------------------||
     * -------------------------méthode testant la validité de la clef----------------------------||
     * -------------------------------------------------------------------------------------------||
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void testClefValide()
    {
        if (chmpClef.getText().toString().equals("")) {
            chmpClair.setEnabled(false);
            chmpCrypte.setEnabled(false);
        } else {
            Outils test = new Outils();
            if (!test.testClef(chmpClef.getText().toString())) {
                chmpClair.setText("");
                chmpClair.setEnabled(false);
                chmpCrypte.setEnabled(false);
                AlertDialog.Builder alaaarm = new AlertDialog.Builder(LeTranscodeur.this);
                alaaarm.setTitle(R.string.titreClefInvalide)
                        .setMessage(R.string.messageClefInvalide)
                        .setNeutralButton(R.string.OK,
                                (dialogInterface, i) -> dialogInterface.dismiss())
                        .show();
            }
        }
    }
    //************************fin de la méthode de test de la validité de la clef ****************||
}
// ************************** fin de la classe ***************************************************||