package pl.achcinski.gtk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogRegActivity extends AppCompatActivity {

    Button bLogin, bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        getSupportActionBar().setTitle("Get To Know");

        bLogin = findViewById(R.id.login);
        bRegister = findViewById(R.id.register);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}

// Ekran początkowy do logowania lub rejestracji
