package pl.achcinski.gtk.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

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
import java.util.zip.Inflater;

import pl.achcinski.gtk.Adapters.ChatAdapter;
import pl.achcinski.gtk.Models.Chat;
import pl.achcinski.gtk.R;
import pl.achcinski.gtk.databinding.ActivityChatBinding;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseUser, databaseChat;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String currentUId, matchId, chatId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = getSharedPreferences("PREFS",MODE_PRIVATE);
        String userSex = sharedPreferences.getString("userSex","");
        Log.i("seks",userSex);

        matchId = getIntent().getStringExtra("matchId");

        Log.i("meczid",matchId);
        currentUId = mAuth.getCurrentUser().getUid();
        databaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(currentUId).child("Links").child("Matches").child(matchId).child("ChatID");
        databaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        chatId();

        mRecyclerView = findViewById(R.id.chat_list_recycler_view);
        mRecyclerView.setHasFixedSize(false);
        mLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);

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
                    chatId = dataSnapshot.getValue().toString();
                    databaseChat = databaseChat.child(chatId);
                    Log.i("proszem",chatId);
                    getMessages();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void getMessages() {
        databaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    String message = null;
                    String SentBy = null;

                    if(dataSnapshot.child("text").getValue()!=null){
                        message = dataSnapshot.child("text").getValue().toString();
                    }
                    if(dataSnapshot.child("SentBy").getValue()!=null){
                        SentBy = dataSnapshot.child("SentBy").getValue().toString();
                    }
                    if(message!=null && SentBy!=null){
                        boolean currentUserBoolean = false;
                        if(SentBy.equals(currentUId)){
                            currentUserBoolean = true;
                        }
                        Chat newMessage = new Chat(message, currentUserBoolean);
                        resultsChat.add(newMessage);
                        mAdapter.notifyDataSetChanged();

                        binding.chatListRecyclerView.smoothScrollToPosition(binding.chatListRecyclerView.getAdapter().getItemCount());
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }
    private ArrayList<Chat> resultsChat = new ArrayList<>();
    private List<Chat> getDataSetChat() { return resultsChat;}
}
