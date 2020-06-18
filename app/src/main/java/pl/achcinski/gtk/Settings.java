package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {


    private EditText mPhone;
    private Button mAccept;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;

    private String currentUId, phone, radio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        mPhone = findViewById(R.id.settingsPhone);
        mAccept = findViewById(R.id.settingsAccept);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        Intent i = getIntent();
        String userSex = i.getStringExtra("userSex");

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUId);

        loadInfo();

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = mPhone.getText().toString().trim();
                userDatabase.child("profileInfo").child("phone").setValue(phone);
                finish();
            }
        });


    }

    private void loadInfo() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.child("profileInfo").getValue();
                    if(map.get("phone")!=null) {
                        phone = map.get("phone").toString();
                        mPhone.setText(phone);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
