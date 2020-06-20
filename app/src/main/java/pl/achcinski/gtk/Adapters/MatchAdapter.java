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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.achcinski.gtk.Chat.ChatActivity;
import pl.achcinski.gtk.Models.Match;
import pl.achcinski.gtk.R;


public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.matchViewHolder> {
    private ArrayList<Match> mMatchList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class matchViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public matchViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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

    public MatchAdapter(ArrayList<Match> matchArrayList) {
        mMatchList = matchArrayList;
    }

    @NonNull
    @Override
    public matchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_element, parent, false);
        matchViewHolder mvh = new matchViewHolder(v, mListener);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull matchViewHolder holder, int position) {
        Match item = mMatchList.get(position);

        Picasso.get().load(item.getProfilePic()).into(holder.mImageView);
        holder.mTextView1.setText(item.getAge());
        holder.mTextView2.setText(item.getName());
        holder.mTextView3.setText(item.getID());
    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }

}

