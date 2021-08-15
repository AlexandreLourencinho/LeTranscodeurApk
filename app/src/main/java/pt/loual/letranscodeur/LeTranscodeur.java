package pt.loual.letranscodeur;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import pt.loual.letranscodeur.outils.GenClef;
import pt.loual.letranscodeur.outils.Transcodeur;


public class LeTranscodeur extends AppCompatActivity {

    private EditText chmpEncode;
    private EditText chmpDecode;
    private EditText chmpClef;
    private Transcodeur trans;
    private GenClef genClef;
    private Button boutonGenererClef;
    private Button boutonSauverClef;
    final EditText champNom = new EditText(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_le_transcodeur);
        chmpEncode = findViewById(R.id.chmpDecode);
        chmpDecode = findViewById(R.id.chmpEncode);
        chmpClef = findViewById(R.id.chmpClef);
        boutonGenererClef = findViewById(R.id.boutonGenerer);
        boutonSauverClef = findViewById(R.id.boutonSauver);

        TooltipCompat.setTooltipText(chmpEncode,this.getString(R.string.tooltip_encoder));
        TooltipCompat.setTooltipText(chmpDecode,this.getString(R.string.tooltip_decoder));
        TooltipCompat.setTooltipText(chmpClef,this.getString(R.string.tooltip_clef));


        chmpEncode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(chmpEncode.isFocused())
                {
                    if(chmpClef.getText().toString().equals("")){
                        chmpEncode.setEnabled(false);
                        chmpDecode.setEnabled(false);
                    }else{
                        trans = new Transcodeur(chmpClef.getText().toString());
                        chmpDecode.setText(trans.encode(chmpEncode.getText().toString()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        chmpDecode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(chmpDecode.isFocused())
                {
                    if(chmpClef.getText().toString().equals("")){
                        chmpEncode.setEnabled(false);
                        chmpDecode.setEnabled(false);
                    }else{
                        trans = new Transcodeur(chmpClef.getText().toString());
                        chmpEncode.setText(trans.decode(chmpDecode.getText().toString()));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boutonGenererClef.setOnClickListener(this::genererClef);
        boutonSauverClef.setOnClickListener(this::sauverClef);


    }

    public void genererClef(View view)
    {
        genClef = new GenClef();
        chmpClef.setText(genClef.randomKey());
    }

    public void sauverClef(View view)
    {
        AlertDialog.Builder alerte = new AlertDialog.Builder(this);
        alerte.setTitle(R.string.titreEntrerNom);
        alerte.setMessage(R.string.entrerNom);
        alerte.setView(champNom);
        alerte.setPositiveButton(R.string.sauver, (dialogInterface, i) -> {
            if(champNom.getText().toString().equals(""))
            {
                dialogInterface.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(LeTranscodeur.this);
                alert.setTitle(R.string.titreNomVide);
                alert.setMessage(R.string.nomVide);
                alert.setPositiveButton(R.string.OK, (dialogInterface1, i1) -> {
                    dialogInterface1.dismiss();
                    sauverClef(view);
                });
            }else{
                //sauverclef
                chmpDecode.setText(champNom.getText().toString());
            }
        })
        .setNegativeButton(R.string.annuler, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }


}