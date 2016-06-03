package dk.vixo.missing_people;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent splashIntent;
        SharedPreferences userPref = getSharedPreferences("userPref", Context.MODE_PRIVATE);
        if (userPref.getBoolean("isLoggedIn", false)) {
            splashIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        } else {
            splashIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        }

        startActivity(splashIntent);
        finish();
    }
}
