package xyz.christianwijasa.tutorial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.Thread.sleep;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            sleep(2000);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } catch(Exception e) {
            Toast.makeText(
                    getBaseContext(),
                    "Error: " + e.toString(),
                    Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }
}
