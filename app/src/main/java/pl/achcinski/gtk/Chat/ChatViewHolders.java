package pl.achcinski.gtk.Chat;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import pl.achcinski.gtk.R;

public class ChatViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView mText;
    public LinearLayout mLayout;
    public ChatViewHolders(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);

        mText = itemView.findViewById(R.id.messageChat);
        mLayout = itemView.findViewById(R.id.chat_element_layout);
    }

    @Override
    public void onClick(View v) {

    }
}
