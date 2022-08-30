package com.example.myrestuarent;

import android.content.Context;
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

import java.util.ArrayList;


public class ReceiptPage extends Fragment {


    View view;
    RecyclerView recyclerView;
    Button btn_payment;
    ReceiptCardAdapter cardAdapter;
    ArrayList<Dish> receipt_list = new ArrayList<Dish>();
    double total_order_pay;
    TextView total_payment;
    ReceiptPageListener receiptPageListener;

    public ReceiptPage(ArrayList<Dish> receipt_list,double total_order_pay) {
        this.receipt_list = receipt_list;
        this.total_order_pay = total_order_pay;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_receipt_page, container, false);
        intViews();
        intAdapter();
        btn_payment.setOnClickListener(v->{
            receiptPageListener.payAndFinish();
        });
        return view;

    }
    private void intViews() {
        recyclerView = view.findViewById(R.id.recycleViewReceipt);
        total_payment = view.findViewById(R.id.total_payment);
        total_payment.setText("סכום כולל:" + " " + total_order_pay + " " + "ש\"ח");
        btn_payment = view.findViewById(R.id.btn_payment);
    }







    private void intAdapter() {
        cardAdapter = new ReceiptCardAdapter(getContext(), receipt_list);
        recyclerView.setAdapter((cardAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        receiptPageListener = (ReceiptPage.ReceiptPageListener)context;
    }

    public interface ReceiptPageListener{
        void payAndFinish();
}



}