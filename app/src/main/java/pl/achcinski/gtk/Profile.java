package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {

    private EditText mName, mAbout;
    private Button mAccept;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;

    private String currentUId, name, about;

    //private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.profileName);
        mAbout = findViewById(R.id.profileAbout);
        mAccept = findViewById(R.id.profileAccept);

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();
        String userSex = getIntent().getExtras().getString("userSex");

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUId);

        loadInfo();
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

    }

    private void saveInfo() {
        name = mName.getText().toString();
        about = mAbout.getText().toString();

        Map uInfo = new HashMap();
        uInfo.put("name", name);
        uInfo.put("about", about);
        userDatabase.updateChildren(uInfo);
    }

    private void loadInfo() {
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        name = map.get("name").toString();
                        mName.setText(name);
                    }
                    if(map.get("about")!=null){
                        about = map.get("about").toString();
                        mAbout.setText(about);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}