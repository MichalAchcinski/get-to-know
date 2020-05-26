package pl.achcinski.gtk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class tinderAdapter extends android.widget.ArrayAdapter<Card> {

    public tinderAdapter(Context context, int resourceId, List<Card> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Card card_item = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.name);
        ImageView image = convertView.findViewById(R.id.imageView);

        name.setText(card_item.getName());
        image.setImageResource(R.drawable.test169photo);

        return convertView;

    }
}

// adapter czyli most między danymi a interfejsem widzianym przez uzytkownika (Konwertowanie tych danych do item.xml)
