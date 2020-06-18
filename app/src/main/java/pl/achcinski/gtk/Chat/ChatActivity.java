package pl.achcinski.gtk.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pl.achcinski.gtk.Adapters.ChatAdapter;
import pl.achcinski.gtk.Models.Chat;
import pl.achcinski.gtk.R;
import pl.achcinski.gtk.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseUser, databaseChat;

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String currentUId, matchId;
    private ArrayList<Chat> chatArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String userSex = prefs.getString("userSex", "no id");

        matchId = getIntent().getStringExtra("matchId");

        Log.i("meczid",matchId);
        currentUId = mAuth.getCurrentUser().getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUId).child("Links").child("Matches").child(matchId).child("chatID");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        chatId();

        mRecyclerView = findViewById(R.id.chat_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ChatAdapter(chatArrayList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        binding.sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String sendMessageText = binding.messageChat.getText().toString();
        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = databaseChat.push();
            HashMap message = new HashMap();
            message.put("SentBy",currentUId);
            message.put("text",sendMessageText);

            newMessageDb.setValue(message);

        }
        binding.messageChat.setText(null);
    }

    private void chatId(){
        databaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String chatId;
                    chatId = dataSnapshot.getValue().toString();
                    databaseChat = databaseChat.child(chatId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}