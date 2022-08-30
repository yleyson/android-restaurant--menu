package com.example.myrestuarent;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.ViewTarget;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DishDialog extends AppCompatDialogFragment implements View.OnClickListener {

    Dish dish;
    LinearLayout pop_up_image_layout;
    ImageView imageView;
    TextView title, desc, amount_view, price_view, ingredients_textView;
    Button btn_add_to_cart, btn_plus, btn_minus, btn_sensitivity;
    View view;
    DishDialogListener listener;
    int amount_num;
    double price_num;
    SensitivityFragment sensitivityFragment;
    ArrayList<String> ingredeints_list;


    public DishDialog(Dish dish, DishDialogListener listener) {
        this.dish = dish;
        this.listener = listener;
        sensitivityFragment = new SensitivityFragment(dish.getSensitivity());
        ingredeints_list = dish.getIngredients();
    }

    public Dish getDish() {
        return dish;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.pop_up, null);
        builder.setView(view);

        intViews();

        buttonInt();
        // Now create object of AlertDialog from the Builder.
        final AlertDialog dialog = builder.create();

// Let's start with animation work. We just need to create a style and use it here as follows.
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void intViews() {

        pop_up_image_layout = view.findViewById(R.id.pop_up_image_layout);
        imageView = view.findViewById(R.id.img_to_show);
        Glide.with(getContext()).load(dish.getImg()).into(imageView);

        title = view.findViewById(R.id.pop_up_title);
        title.setText(dish.getName());
        desc = view.findViewById(R.id.pop_up_desc);
        desc.setText(dish.getDesc());
        amount_view = view.findViewById(R.id.pop_up_amount);
        amount_num = dish.getAmount();
        amount_view.setText("" + amount_num);
        price_view = view.findViewById(R.id.pop_up_price);
        price_num = dish.getPrice();
        //אם המנה כבר קיימת בעגלה הכמות תיהיה כמות המנה
        if (amount_num == 0)
            price_view.setText("0 ש\"ח");
        else
            price_view.setText("" + dish.getTotal_price() + " " + "ש\"ח");

        btn_sensitivity = view.findViewById(R.id.btn_sensitivity);
        //אם אין רישויות במנה גפתור רגישויות לא יופיע
        if (sensitivityFragment.sensitivity.toString().equals("[]"))
            btn_sensitivity.setVisibility(View.GONE);
        ingredients_textView = view.findViewById(R.id.ingredients_textView);
        ingredients_textView.setText("מרכיבים: " + (ingredeints_list.toString()).substring(1, ingredeints_list.toString().length() - 1));

        btn_add_to_cart = view.findViewById(R.id.btn_add_to_cart);
        btn_plus = view.findViewById(R.id.btn_pop_up_plus);
        btn_minus = view.findViewById(R.id.btn_pop_up_minus);

    }


    private void buttonInt() {
        btn_add_to_cart.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        btn_minus.setOnClickListener(this);
        btn_sensitivity.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_to_cart:
                if (amount_num == 0)
                    return;
                dish.setAmount(amount_num);
                dish.setTotal_price(amount_num * price_num);
                listener.AddDish(dish);
                break;
            case R.id.btn_pop_up_plus:
                amount_num++;
                amount_view.setText("" + amount_num);
                price_view.setText("" + (price_num * amount_num) + " " + "ש\"ח");
                break;
            case R.id.btn_pop_up_minus:
                if (amount_num == 1)
                    return;
                amount_num--;
                amount_view.setText("" + amount_num);
                price_view.setText("" + (price_num * amount_num) + " " + "ש\"ח");
                break;
            case R.id.btn_sensitivity:
                sensitivityFragment.show(getChildFragmentManager(), "Ingredients");
                break;

        }
    }

    //ממשק להוספת מנה לעגלה
    public interface DishDialogListener {
        void AddDish(Dish dish);
    }

}
