package com.example.myrestuarent;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class FilterFragment extends Fragment implements View.OnClickListener {

    CheckBox checkBoxOne, checkBoxTwo, checkBoxThree,checkBoxFour;
    Button toggleButton;
    View view;
    ArrayList<String> arrayList = new ArrayList<>();
    FillterPageListnerGoBack fillterPageListnerGoBack;
    String res_code_str;

    public FilterFragment(ArrayList<String> arrayList,String res_code) {
        this.arrayList = arrayList;
        this.res_code_str=res_code;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_filter, container, false);
//        Toast.makeText(getContext(), ""+arrayList, Toast.LENGTH_SHORT).show();
        initViews();
        initButton();
        return view;
    }


    private void initButton() {

        toggleButton.setOnClickListener(this);
        checkBoxOne.setOnClickListener(this);
        checkBoxTwo.setOnClickListener(this);
        checkBoxThree.setOnClickListener(this);
        checkBoxFour.setOnClickListener(this);
    }

    private void initViews() {
        checkBoxOne = view.findViewById(R.id.gluten);
        checkBoxTwo = view.findViewById(R.id.lectose);
        checkBoxThree = view.findViewById(R.id.nuts);
        checkBoxFour=view.findViewById(R.id.sesemi);
        toggleButton = view.findViewById(R.id.sendChoose);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendChoose: {
                if (toggleButton.isClickable()) {
                    arrayList.removeAll(arrayList);
                    if (checkBoxOne.isChecked()) {
                        arrayList.add(checkBoxOne.getText().toString());
                    } else
                        arrayList.remove(checkBoxOne.getText().toString());
                    if (checkBoxTwo.isChecked()) {

                        arrayList.add(checkBoxTwo.getText().toString());
                    } else
                        arrayList.remove(checkBoxTwo.getText().toString());
                    if (checkBoxThree.isChecked()) {

                        arrayList.add(checkBoxThree.getText().toString());
                    } else
                        arrayList.remove(checkBoxThree.getText().toString());
                    if (checkBoxFour.isChecked())
                        arrayList.add(checkBoxFour.getText().toString());
                    else
                        arrayList.remove(checkBoxFour.getText().toString());
                    Drawable drawabletwo = getResources().getDrawable(R.drawable.ic_baseline_check_box_outline);
                    toggleButton.setBackgroundDrawable(drawabletwo);
                    fillterPageListnerGoBack.GoBack(res_code_str);
                }

            }

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fillterPageListnerGoBack = (FilterFragment.FillterPageListnerGoBack) context;

    }

    public  interface FillterPageListnerGoBack{
        void GoBack(String res_code);
    }


}