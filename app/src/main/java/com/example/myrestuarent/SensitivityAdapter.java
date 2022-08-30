package com.example.myrestuarent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SensitivityAdapter extends RecyclerView.Adapter<SensitivityAdapter.MyViewHolder> {

    ArrayList<String> ingredients_list;

    Context context;


    public SensitivityAdapter(Context context, ArrayList<String> ingredients_list) {
        this.ingredients_list = ingredients_list;
        this.context = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.sensitivity_view, parent, false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.textView.setText(ingredients_list.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.view_text);

        }
    }
}