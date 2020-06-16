package pl.achcinski.gtk.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.achcinski.gtk.Models.Match;
import pl.achcinski.gtk.R;


public class matchAdapter extends RecyclerView.Adapter<matchAdapter.matchViewHolder> {
    private ArrayList<Match> mMatchList;

    public static class matchViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;

        public matchViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.matchListImageView);
            mTextView1 = itemView.findViewById(R.id.matchListTextViewAge);
            mTextView2= itemView.findViewById(R.id.matchListTextViewName);
        }
    }

    public matchAdapter(ArrayList<Match> matchArrayList) {
        mMatchList = matchArrayList;
    }

    @NonNull
    @Override
    public matchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match_list_element, parent, false);
        matchViewHolder mvh = new matchViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull matchViewHolder holder, int position) {
        Match item = mMatchList.get(position);

        Picasso.get().load(item.getProfilePic()).into(holder.mImageView);
        holder.mTextView1.setText(item.getAge());
        holder.mTextView2.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mMatchList.size();
    }
}
