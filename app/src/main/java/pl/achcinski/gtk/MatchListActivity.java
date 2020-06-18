package pl.achcinski.gtk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.achcinski.gtk.Adapters.CardAdapter;
import pl.achcinski.gtk.Adapters.MatchAdapter;
import pl.achcinski.gtk.Chat.ChatActivity;
import pl.achcinski.gtk.Models.Match;


public class MatchListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MatchAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String currentUId = mAuth.getCurrentUser().getUid();

    private ArrayList<Match> matchArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_list);

        mRecyclerView = findViewById(R.id.match_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new MatchAdapter(matchArrayList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new MatchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked"); ///////////////////////////////////////////////////////////////////////
            }
        });

        getMatches();

    }

    private void getMatches(){
        String userSex = getIntent().getExtras().getString("userSex");
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUId).child("Links").child("Matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot match : dataSnapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        String oppositeUserSex = getIntent().getExtras().getString("oppositeUserSex");
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(oppositeUserSex).child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String age = "";
                    String name = "";
                    String imageurl = "";
                    String ID = dataSnapshot.getKey();
                    if(dataSnapshot.child("profileInfo").child("age").getValue()!=null){
                        age = dataSnapshot.child("profileInfo").child("age").getValue().toString();
                    }
                    if(dataSnapshot.child("profileInfo").child("name").getValue()!=null){
                        name = dataSnapshot.child("profileInfo").child("name").getValue().toString();
                    }
                    if(dataSnapshot.child("profileInfo").child("imageurl").getValue()!=null){
                        imageurl = dataSnapshot.child("profileInfo").child("imageurl").getValue().toString();
                    }

                    Match obj = new Match(age, name, imageurl, ID);
                    matchArrayList.add(obj);
                    mAdapter.notifyDataSetChanged();

                        HashMap log = new HashMap();
                        log.put("name",name);
                        log.put("age",age);
                        log.put("imageurl",imageurl);
                        log.put("ID",ID);
                        Log.i("siema",log.toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void changeItem (int position, String text){
        matchArrayList.get(position).changeText1(text);
        mAdapter.notifyItemChanged(position);
    }

}