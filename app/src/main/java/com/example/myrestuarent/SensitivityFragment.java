package com.example.myrestuarent;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SensitivityFragment extends DialogFragment {

    ArrayList<String> sensitivity = new ArrayList<String>();
    RecyclerView recyclerView;
    SensitivityAdapter sensitivityAdapter;
    View view;

    public SensitivityFragment(ArrayList<String> sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = 600;
        window.setAttributes(params);
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_sensitivity, null);
        builder.setView(view);

        recyclerView = view.findViewById(R.id.recycleViewIngredients);
        recyclerView.setHasFixedSize(true);
        sensitivityAdapter = new SensitivityAdapter(view.getContext(), sensitivity);
        recyclerView.setAdapter((sensitivityAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return builder.create();
    }


}
