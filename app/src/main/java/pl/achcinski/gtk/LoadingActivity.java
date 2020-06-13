package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import pl.achcinski.gtk.Adapters.tinderAdapter;

public class LoadingActivity extends AppCompatActivity {

    private tinderAdapter arrayAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String currentUId;

    private DatabaseReference usersDb;

    List<Card> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        userSex();
        nextActivity();
    }

    String userSex;
    private String oppositeUserSex;

    public void userSex(){
        DatabaseReference maleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Male");             // pobranie z bazy danych osob płci męskiej
        maleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(currentUId)){                                                             // jesli obecne id konta zgadza się z kluczem snapshotu płci męskiej to:
                    Log.i("patrz ", dataSnapshot.getKey());
                    Log.i("patrz ", currentUId);
                    userSex = "Male";
                    oppositeUserSex = "Female";
                    getOppositeSexUsers();                                                                                 // pobieranie z bazy danych osób płci przeciwnej aby je wyświetlić użytkownikowi
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference femaleDb = FirebaseDatabase.getInstance().getReference().child("Users").child("Female");           // pobranie z bazy danych osob płci żenskiej
        femaleDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals(currentUId)){                                                               // jesli obecne id konta zgadza się z kluczem snapshotu płci żenskiej to:
                    userSex = "Female";
                    oppositeUserSex = "Male";
                    getOppositeSexUsers();                                                                                   // pobieranie z bazy danych osób płci przeciwnej aby je wyświetlić użytkownikowi
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getOppositeSexUsers(){
        DatabaseReference oppositeDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex);      // pobranie bazy danych płci przeciwnej do użytkownika
        oppositeDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists() && !dataSnapshot.child("Links").child("noLike").hasChild(currentUId) && !dataSnapshot.child("Links").child("Like").hasChild(currentUId)){                                              // jesli baza nie jest pusta to:
                    //al.add(dataSnapshot.child("name").getValue().toString());      ^^ jesli w database płci przeciwnej są uzytkownicy + jesli nie ma ich w podkatalogu like albo dislike to wykonujemy:
                    String imageurl = "null";
                    if (!dataSnapshot.child("profileInfo").child("imageurl").getValue().equals("null")){
                        imageurl = dataSnapshot.child("profileInfo").child("imageurl").getValue().toString();
                    }
                    Card item = new Card(dataSnapshot.getKey(),dataSnapshot.child("profileInfo").child("name").getValue().toString(),imageurl);                // tworzenie kart z osobami z płci przeciwnej
                    rowItems.add(item);                                                                                          // dodawanie ich do arrayList
                    arrayAdapter.notifyDataSetChanged();                                                                         // powiadomienie o zmieanie danych i odswiezenie
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void nextActivity()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this,MainActivity.class);
                intent.putExtra("userSex", userSex);                                                  // dzieki temu mozemy korzystać z userSex w aktywnosci profile
                startActivity(intent);
            }
        }, 2500);
    }
}