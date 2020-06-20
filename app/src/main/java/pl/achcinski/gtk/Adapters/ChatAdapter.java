package pl.achcinski.gtk.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.achcinski.gtk.Chat.ChatViewHolders;
import pl.achcinski.gtk.Models.Chat;
import pl.achcinski.gtk.R;


public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolders> {
    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_element, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        ChatViewHolders cvh = new ChatViewHolders(v);

        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolders holder, int position) {
        holder.mText.setText(chatList.get(position).getMessage());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1.0f;
        params.setMargins(0,6,0,0);

        if(chatList.get(position).getCurrentUser()){
            holder.mText.setGravity(Gravity.END);
            holder.mText.setTextColor(Color.parseColor("#404040"));
            holder.mLayout.setBackgroundResource(R.drawable.rounded_sent_chat);
            params.gravity = Gravity.END;
            holder.mLayout.setLayoutParams(params);

        }else {
            holder.mText.setGravity(Gravity.START);
            holder.mText.setTextColor(Color.parseColor("#FFFFFF"));
            holder.mLayout.setBackgroundResource(R.drawable.rounded_received_chat);
            params.gravity = Gravity.START;
            holder.mLayout.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

}

