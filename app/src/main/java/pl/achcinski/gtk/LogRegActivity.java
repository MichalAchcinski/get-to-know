package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogRegActivity extends AppCompatActivity {

    Button bLogin, bRegister;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_reg);

        bLogin = findViewById(R.id.login);
        bRegister = findViewById(R.id.register);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogRegActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LogRegActivity.this, LoadingActivity.class);
                    startActivity(intent);
                }
            } // sprawdzanie czy dany użytkownik jest już zalogowany, jeśli tak to aktywność zmienia się od razu na mainacitivity
        };

    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);  // rozpoczyna nasłuchiwanie zmian uwierzytelniania, daje znać po tym jak nastąpi rejestracja, logowanie, wylogowanie, obceny użytkownik się zmieni
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener); // zatrzymuje nasłuchiwanie zmian uwierzytelniania
    }
}

// Ekran początkowy do logowania lub rejestracji
