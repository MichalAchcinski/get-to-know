package pl.achcinski.gtk.Adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.achcinski.gtk.Chat.ChatActivity;
import pl.achcinski.gtk.Models.Chat;
import pl.achcinski.gtk.R;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.chatViewHolder> {
    private ArrayList<Chat> mChatList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class chatViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public chatViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.matchListImageView);
            mTextView1 = itemView.findViewById(R.id.matchListTextViewAge);
            mTextView2 = itemView.findViewById(R.id.matchListTextViewName);
            mTextView3 = itemView.findViewById(R.id.matchListTextViewId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                    Bundle b = new Bundle();
                    b.putString("matchId",mTextView3.getText().toString());
                    intent.putExtras(b);
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public ChatAdapter(ArrayList<Chat> chatArrayList) {
        mChatList = chatArrayList;
    }

    @NonNull
    @Override
    public chatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_element, parent, false);
        chatViewHolder mvh = new chatViewHolder(v, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull chatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

}

