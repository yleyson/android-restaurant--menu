package com.example.myrestuarent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReceiptCardAdapter extends RecyclerView.Adapter<ReceiptCardAdapter.MyViewHolder> {

    Context context;
    ArrayList<Dish> receipt_list;
    public ReceiptCardAdapter(Context context, ArrayList<Dish> receipt_list) {
        this.context =context;
        this.receipt_list = receipt_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.receipt_card_view, parent, false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(receipt_list.get(position).getName());
        holder.amount.setText("" + receipt_list.get(position).getAmount());
        holder.price.setText("" + receipt_list.get(position).getTotal_price());

    }

    @Override
    public int getItemCount() {
        return receipt_list.size();
    }

    public class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView name, price, amount;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.receiptName);
            amount = itemView.findViewById(R.id.receiptAmount);
            price = itemView.findViewById(R.id.receiptPrice);

        }
    }
}
