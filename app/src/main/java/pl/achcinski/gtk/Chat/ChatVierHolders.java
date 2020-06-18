package pl.achcinski.gtk.Chat;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ChatVierHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public ChatVierHolders (View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
