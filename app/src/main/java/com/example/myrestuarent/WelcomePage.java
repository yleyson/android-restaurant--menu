package com.example.myrestuarent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class WelcomePage extends Fragment {

    View view;
    TextView welcome_text;
    ImageView welcome_img;
    String text,img;

    public WelcomePage(String text,String img) {
        this.text = text;
        this.img = img;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_welcome_page, container, false);
        intViews();

        return  view;
    }

    private void intViews() {
        welcome_text = view.findViewById(R.id.welcome_text);
        welcome_img = view.findViewById(R.id.welcome_img);

        welcome_text.setText(text + " "+ "ברוכים הבאים למסעדת");
        Glide.with(getContext()).load(img).into(welcome_img);

    }
}