package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button mRegister, mDateButton;
    private EditText mEmail, mPassword, mName;

    private RadioGroup mRadioGroup;
    private TextView mBirthDay;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterActivity.this, LoadingActivity.class);
                    startActivity(intent);
                }                                                                                   // Jeśli użytkownik jest zalogowany, to zostanie przeniesiony od razu do MainActivity
            }
        };

        mRegister = findViewById(R.id.Register2);

        mEmail = findViewById(R.id.emailRegister);
        mPassword = findViewById(R.id.passwordRegister);
        mName = findViewById(R.id.nameRegister);
        mRadioGroup = findViewById(R.id.radioGroup);
        mBirthDay = findViewById(R.id.dayOfBirthRegister);
        mDateButton = findViewById(R.id.dateButton);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectId);

                if (radioButton.getText() == null) {
                    return;
                }

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                final String name = mName.getText().toString();
                final String age = mBirthDay.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId).child("profileInfo");
                            Map uInfo = new HashMap<>();
                            uInfo.put("name",name);
                            uInfo.put("imageurl","none");
                            uInfo.put("age",age);
                            currentUserDb.updateChildren(uInfo);
                        }
                    }
                });                                                                                 //odczytywanie danych rejestracji i  zapisywanie ich w bazie danych firebase

            }
        });


        mName.addTextChangedListener(registerTextWatcher);
        mEmail.addTextChangedListener(registerTextWatcher);                                            // będziemy sprawdzali czy pola email i password są puste czy nie
        mPassword.addTextChangedListener(registerTextWatcher);                                         // jesli nie to pozwalamy kliknąć przycisk
        mBirthDay.addTextChangedListener(registerTextWatcher);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private TextWatcher registerTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = mName.getText().toString().trim();
            String emailInput = mEmail.getText().toString().trim();                                 // trim po to żeby białych znaków nie zaliczało jako napisu
            String passwordInput = mPassword.getText().toString().trim();
            String ageInput = mBirthDay.getText().toString().trim();

            mRegister.setEnabled(!nameInput.isEmpty() && !emailInput.isEmpty() && !passwordInput.isEmpty() && !ageInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);                                      // rozpoczyna nasłuchiwanie zmian uwierzytelniania, daje znać po tym jak nastąpi rejestracja, logowanie, wylogowanie, obceny użytkownik się zmieni
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);                                   // zatrzymuje nasłuchiwanie zmian uwierzytelniania
    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this,Calendar.getInstance().get(Calendar.YEAR),Calendar.getInstance().get(Calendar.MONTH),Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;
        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(year,month,dayOfMonth);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if(a < 0) {
            Toast.makeText(RegisterActivity.this, "Ustaw poprawną datę urodzenia!", Toast.LENGTH_SHORT).show();
            String age = "";
            mBirthDay.setText(age);
        }
        else if(a > 0 && a<18){
            Toast.makeText(RegisterActivity.this, "Jesteś za młody/a!", Toast.LENGTH_SHORT).show();
            String age = "";
            mBirthDay.setText(age);
        } else {
            String age = a + "";
            mBirthDay.setText(age);
        }


    }

}
