package com.example.myrestuarent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;


public class CartPage extends Fragment implements CartCardAdapter.CartSetPayTextInterface {

    View view;
    RecyclerView recyclerView;
    CartCardAdapter cardAdapter;
    double total_order_pay = 0;
    TextView total_order_pay_view;
    Button btn_order;
    CartPageListener cartPageListener;
    ArrayList<String> receipt = new ArrayList<>();
    ArrayList<Dish> cart = new ArrayList<Dish>();

    public CartPage(ArrayList<Dish> cart, ArrayList<String> receipt) {
        this.cart = cart;
        this.receipt = receipt;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_page, container, false);

        intViews();
        intAdapter();
        totalPriceCalc();
        creatSendList();
        return view;
    }

    //פונקציה ליצירת קבלה
    private void creatSendList() {

        receipt.clear();

        for (Dish dish : cart) {
            String receipt_str = "";
            receipt_str += "מנה: " + dish.getName();
            receipt_str += "\n";
            receipt_str += "מחיר: " + dish.getTotal_price();
            receipt_str += "\n";
            receipt_str += "כמות: " + dish.getAmount();
            receipt_str += "\n";
            receipt.add(receipt_str);
        }
        receipt.add("סכום כולל: " + total_order_pay);
    }


    private void intViews() {

        recyclerView = view.findViewById(R.id.recycleViewCart);
        total_order_pay_view = view.findViewById(R.id.total_order_pay);
        btn_order = view.findViewById(R.id.btn_order);


        btn_order.setOnClickListener(v -> {
            creatSendList();
            cartPageListener.GoReceiptPage(total_order_pay);
        });

    }

    // פונקציה לעדכון סכום כולל בטעינה ראשונה
    private void totalPriceCalc() {

        for (int i = 0; i < cart.size(); i++) {
            total_order_pay += cart.get(i).getTotal_price();
        }
        total_order_pay_view.setText("סכום כולל:" + " " + total_order_pay + " " + "ש\"ח");

    }

    private void intAdapter() {

        cardAdapter = new CartCardAdapter(getContext(), cart, this);
        recyclerView.setAdapter((cardAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    //פונקציה לעדכון סכום כולל
    protected void SetPaytext(double price) {
        total_order_pay += price;
        total_order_pay_view.setText("סכום כולל:" + " " + total_order_pay + " " + "ש\"ח");

    }

    @Override
    public void setPayTextAdapter(double price) {
        SetPaytext(price);
    }

    //אם עגלה ריקה מעבר לתפריט
    @Override
    public void cartEmptyGoToMenu() {
        cartPageListener.GoToMenu();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        cartPageListener = (CartPage.CartPageListener) context;
    }

    public interface CartPageListener {
        void GoReceiptPage(double total_order_pay);
        void GoToMenu();
    }
}

