package com.example.myrestuarent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MenuCardAdapter extends RecyclerView.Adapter<MenuCardAdapter.MyViewHolder> {


    ArrayList<Dish> dishList;
    ShowDishDialogListner showDishDialogListner;

    Context context;


    public MenuCardAdapter(Context context, ArrayList<Dish> dishList, ShowDishDialogListner showDishDialogListner) {
        this.dishList = dishList;
        this.context = context;
        this.showDishDialogListner = showDishDialogListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card_view, parent, false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(dishList.get(position).getName());
        holder.price.setText("" + (dishList.get(position).getPrice()) + " " + "ש\"ח");
        Glide.with(context).load(dishList.get(position).getImg()).into(holder.myImage);

    }


    @Override
    public int getItemCount() {
        return dishList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, price;
        ImageView myImage;
        MaterialCardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitleMenuCard);
            price = itemView.findViewById(R.id.textPriceMenuCard);
            card = itemView.findViewById(R.id.menuCard);
            myImage = itemView.findViewById(R.id.imageMenuCard);

            card.setOnClickListener(v -> {
                Dish dishToAdd = new Dish(dishList.get(getAdapterPosition()));
                showDishDialogListner.showPopUpWindow(dishToAdd);
            });
        }
    }


    //ממשק כדי להציג את המנה
    public interface ShowDishDialogListner {
        void showPopUpWindow(Dish dish);
    }
}
