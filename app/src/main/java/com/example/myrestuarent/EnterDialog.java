package com.example.myrestuarent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


public class EnterDialog extends Fragment implements View.OnClickListener {

    EditText res_code;
    Button enter_app, checkElergic;
    String res_code_str;
    View view;
    MenuPageListenerFinal menuPageListenerFinal;
    FillterPageListner fillterPageListner;


    public EnterDialog(String res_code) {
        res_code_str=res_code;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_enter_dialog, container, false);
        intViews();
        intButtons();


        return view;
    }

    private void intButtons() {

        enter_app.setOnClickListener(this);
        checkElergic.setOnClickListener(this);
    }

    private void intViews() {
        res_code = view.findViewById(R.id.res_code);
        res_code.setText(res_code_str);
        enter_app = view.findViewById(R.id.enter_app);
        checkElergic = view.findViewById(R.id.sensetive);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.enter_app:
                if (res_code.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "חובה להכניס קוד מסעדה", Toast.LENGTH_SHORT).show();
                    return;
                }

                menuPageListenerFinal.GoMenuFinal(res_code.getText().toString());
                break;
            case R.id.sensetive:
                 fillterPageListner.GoToFillter(res_code.getText().toString());
                break;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        menuPageListenerFinal = (EnterDialog.MenuPageListenerFinal) context;
         fillterPageListner = (EnterDialog.FillterPageListner) context;

    }

    //ממשק מעבר לתפריט
    public interface MenuPageListenerFinal {
        void GoMenuFinal(String res_code);
    }

//ממשק מעבר לדף סינון רגישויות
    public interface FillterPageListner{
        void GoToFillter(String res_code);
    }


}