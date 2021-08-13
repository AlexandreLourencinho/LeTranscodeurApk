package pt.loual.letranscodeur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ImageView logoApp;
    private TextView textApp;
    private TextView trnsLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Animation animationLogo = AnimationUtils.loadAnimation(this,R.anim.animation_logo);
        Animation animationTexte = AnimationUtils.loadAnimation(this,R.anim.animation_texte );
        Animation animationTexteDeux= AnimationUtils.loadAnimation(this,R.anim.animation_texte_deux);
        logoApp = (ImageView) findViewById(R.id.logoApp);
        textApp = (TextView) findViewById(R.id.textAcc);
        trnsLogo = (TextView) findViewById(R.id.trns_logo);

        logoApp.setAnimation(animationLogo);
        textApp.setAnimation(animationTexte);
        trnsLogo.setAnimation(animationTexteDeux);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,AccueilTranscodeur.class);
                startActivity(intent);
                finish();
            }
        },5000);





    }
}