package pt.loual.letranscodeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AccueilTranscodeur extends AppCompatActivity {

    private Button boutonLancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_transcodeur);

        boutonLancer = (Button) findViewById(R.id.boutonLancer);

        boutonLancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lancerTranscodeur();
            }
        });

    }

    public void lancerTranscodeur()
    {
        Intent intent = new Intent(AccueilTranscodeur.this, LeTranscodeur.class);
        startActivity(intent);
    }
}