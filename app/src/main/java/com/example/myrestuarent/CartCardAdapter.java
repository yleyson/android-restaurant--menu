package com.example.myrestuarent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;


public class CartCardAdapter extends RecyclerView.Adapter<CartCardAdapter.MyViewHolder> {

    ArrayList<Dish> cart_list;
    Context context;
    CartSetPayTextInterface cartSetPayTextInterface;
    public CartCardAdapter(Context context, ArrayList<Dish> cart_list,CartSetPayTextInterface cartSetPayTextInterface) {
        this.cart_list = cart_list;
        this.context = context;
        this.cartSetPayTextInterface = cartSetPayTextInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_card_view, parent, false);
        return new MyViewHolder((view));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(cart_list.get(position).getName());
        holder.price.setText("" + cart_list.get(position).getTotal_price() + "ש\"ח");
        holder.amount.setText("" + cart_list.get(position).getAmount());

    }

    @Override
    public int getItemCount() {
        return cart_list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, price, amount;
        Button btn_plus, btn_minus, btn_delete;

        MaterialCardView card;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.textTitleCartCard);
            price = itemView.findViewById(R.id.textPriceCartCard);
            card = itemView.findViewById(R.id.cartCard);
            amount = itemView.findViewById(R.id.textAmountCartCard);
            btn_plus = itemView.findViewById(R.id.btnPlusCart);
            btn_minus = itemView.findViewById(R.id.btnMinusCart);
            btn_delete = itemView.findViewById(R.id.btnDelCart);

            intButton();

        }

        private void intButton() {
            btn_delete.setOnClickListener(this);
            btn_plus.setOnClickListener(this);
            btn_minus.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Dish dish = cart_list.get(getAdapterPosition());
            int total_amount = dish.getAmount();
            double total_price = dish.getTotal_price();
            double dish_price = cart_list.get(getAdapterPosition()).getPrice();

            switch (v.getId()) {
                case R.id.btnPlusCart:
                    dish.setAmount(++total_amount);
                    total_price += dish_price;
                    dish.setTotal_price(total_price);
                    cartSetPayTextInterface.setPayTextAdapter(dish_price);
                    break;
                case R.id.btnMinusCart:
                    if (total_amount == 1)
                        return;
                    dish.setAmount(--total_amount);
                    total_price -= dish_price;
                    dish.setTotal_price(total_price);
                    cartSetPayTextInterface.setPayTextAdapter(-dish_price);
                    break;
                case R.id.btnDelCart:
                    removeItem(getAdapterPosition());
                    break;
            }

            price.setText("" + total_price);
            amount.setText("" + total_amount);

        }

        //פונקציה למחיקת מנה מהעגלה
        private void removeItem(int position) {
            double total_price = cart_list.get(position).getTotal_price();
            cart_list.get(position).setAmount(0);
            cart_list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cart_list.size());
            cartSetPayTextInterface.setPayTextAdapter(-total_price);
            if(cart_list.isEmpty())
                cartSetPayTextInterface.cartEmptyGoToMenu();
        }
    }

    //ממשק למעבר לתפריט עם עגלה ריקה ועדכון סכום כולל
    public interface CartSetPayTextInterface {
        void setPayTextAdapter(double price);
        void cartEmptyGoToMenu();
    }
}
