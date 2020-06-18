package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import pl.achcinski.gtk.Adapters.CardAdapter;
import pl.achcinski.gtk.Models.Card;

public class MainActivity extends AppCompatActivity {

    private CardAdapter arrayAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    String currentUId;

    private DatabaseReference usersDb;

    List<Card> rowItems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        currentUId = mAuth.getCurrentUser().getUid();

        userSex();

        rowItems = new ArrayList<Card>();

        arrayAdapter = new CardAdapter(this, R.layout.item, rowItems );

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Card dataObj = (Card) dataObject;
                String userId = dataObj.getUserId();
                usersDb.child(oppositeUserSex).child(userId).child("Links").child("Like").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Hey", Toast.LENGTH_SHORT).show();
                match(userId);
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Card dataObj = (Card) dataObject;
                String userId = dataObj.getUserId();
                usersDb.child(oppositeUserSex).child(userId).child("Links").child("noLike").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, "Bye", Toast.LENGTH_SHORT).show();
            }

                                                                                                                        // reakcje na przesuniecie karty w lewo lub prawo

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }

        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show();
            }
        });


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userSex", userSex);
        editor.commit();
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
                    String imageurl = "none";
                    if (!dataSnapshot.child("profileInfo").child("imageurl").getValue().equals("none")){
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

    public void logItOut(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LogRegActivity.class);
        startActivity(intent);
        finish();
    }                                                                                                                             //wylogowanie z aplikacji

    private void match (String userId){
        DatabaseReference links = usersDb.child(userSex).child(currentUId).child("Links").child("Like").child(userId);   // pobiernaie ID osób które dały Ci polubienie
        links.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){                                                                              // jesli sprawdzany wlasnie  user dał Ci lajka to:
                    Toast.makeText(MainActivity.this,"macz wow", Toast.LENGTH_SHORT).show();
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    usersDb.child(oppositeUserSex).child(dataSnapshot.getKey()).child("Links").child("Matches").child(currentUId).setValue(true);
                    usersDb.child(userSex).child(currentUId).child("Links").child("Matches").child(dataSnapshot.getKey()).setValue(true);

                    usersDb.child(userSex).child(currentUId).child("Links").child("Matches").child(dataSnapshot.getKey()).child("ChatID").setValue(key);
                    usersDb.child(oppositeUserSex).child(dataSnapshot.getKey()).child("Links").child("Matches").child(currentUId).child("ChatID").setValue(key);


                }                                                                                                       // a ta funkcja wywoływana jest kiedy przesuwasz w lewo (Lajkujesz)
            }                                                                                                           // wiec jesli przesuwasz to sprawdza czy ta osoba dała ci lajka
                                                                                                                        // no i jesli tak to zapisuje w bazie danych matcha el0

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    public void goSettings(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,Settings.class);
        intent.putExtra("userSex", userSex);                                                  // dzieki temu mozemy korzystać z userSex w aktywnosci profile
        startActivity(intent);
    }

    public void goProfile(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,Profile.class);
        intent.putExtra("userSex", userSex);                                                 // dzieki temu mozemy korzystać z userSex w aktywnosci profile
        startActivity(intent);
    }

    public void goMatches(MenuItem item) {
        Intent intent = new Intent(MainActivity.this,MatchListActivity.class);
        intent.putExtra("userSex", userSex);                                                 // dzieki temu mozemy korzystać z userSex w aktywnosci profile
        intent.putExtra("oppositeUserSex",oppositeUserSex);
        startActivity(intent);
    }
}