package pl.achcinski.gtk.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;

import java.util.List;

import pl.achcinski.gtk.Card;
import pl.achcinski.gtk.R;

public class tinderAdapter extends android.widget.ArrayAdapter<Card> {

    public tinderAdapter(Context context, int resourceId, List<Card> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Card cardItem = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.nameItem);
        ImageView image = convertView.findViewById(R.id.imageView);

        name.setText(cardItem.getName());
        Log.i("pokaz imiona enemy",cardItem.getName());
        switch (cardItem.getProfilePic()){
            case "null":
                Glide.with(getContext()).load(R.drawable.test).into(image);
                break;
            default:
                Glide.with(getContext()).load(cardItem.getProfilePic()).into(image);
                break;
        }


        return convertView;

    }
}

// adapter czyli most między danymi a interfejsem widzianym przez uzytkownika (Konwertowanie tych danych do item.xml)
