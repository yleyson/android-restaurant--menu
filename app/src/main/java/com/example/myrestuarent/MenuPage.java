package com.example.myrestuarent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.sun.mail.imap.Rights;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


public class MenuPage extends Fragment implements View.OnClickListener, DishDialog.DishDialogListener, MenuCardAdapter.ShowDishDialogListner {


    View view;
    //רשימה של כפתורי התפריט
    ArrayList<MaterialButton> button_list = new ArrayList<>();
    RecyclerView recyclerView;
    MenuCardAdapter menuCardAdapter;
    //כפתור שנבחר בתפריט
    MaterialButton select_button = null;
    HorizontalScrollView horizontalScrollView;
    Map<String, Object> dictionary = new HashMap<String, Object>();
    ArrayList<Dish> cart = new ArrayList<Dish>();
    //חלון קופץ של המנה
    DishDialog dishDialog;
    //LinearLayout של כפתורי התפריט
    LinearLayout button_lay_menu;

    public MenuPage(Map<String, Object> dictionary, ArrayList<Dish> cart) {
        this.dictionary = dictionary;
        this.cart = cart;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu_page, container, false);
        intViews();
        intButtons();
        onClick(button_list.get(0));

        return view;
    }


    private void intButtons() {

        for (MaterialButton button : button_list) {
            button.setOnClickListener(this);
        }
    }

    private void intViews() {

        recyclerView = view.findViewById(R.id.recycleViewMenu5);
        horizontalScrollView = view.findViewById(R.id.scrollview);
        //חילוץ כל המפתחות של המילון למערך
        String[] dict_list = dictionary.keySet().toArray(new String[0]);

        button_lay_menu = view.findViewById(R.id.button_lay_menu);
//לולאה שעוברת על ל מפתחות המילון ומייצרת כפתורים לפי שם התפריט ואייקן התפריט
        for (int i = dict_list.length - 1; i > 0; i--) {
            //אם המפתח לא מכיל תפריט מנות תמשיך הלאה
            if (dict_list[i].contains("btn_name") || dict_list[i].contains("btn_img"))
                continue;
            //יצירת כפתור חדש
            MaterialButton button = new MaterialButton(new ContextThemeWrapper(getContext(), R.style.MenuButton), null, R.style.MenuButton);
            //שם הכפתור לפי מפתח של התפריט
            String btn_name = (String) dictionary.get(dict_list[i] + "btn_name");
            //אייקון הכפתור לפי מפתח של התפריט
            String btn_img = (String) dictionary.get(dict_list[i] + "btn_img");

            button.setText(btn_name);
            //השמת אייקון בעזרת ספריית glide
            Glide.with(getContext()).load(btn_img).into(new CustomTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    resource.setBounds(0, 0, 80, 80);
                    button.setCompoundDrawables(resource, null, null, null);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });

            //id
            button.setTag(dict_list[i]);
            button.setCornerRadius(50);
            button_list.add(button);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(60, 0, 0, 0);
        for (MaterialButton button : button_list) {
            button_lay_menu.addView(button, params);
        }
        //גלילה לצד ימין
        horizontalScrollView.postDelayed(new Runnable() {
            public void run() {
                horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);    }

    private void intAdapter(ArrayList<Dish> list) {

        menuCardAdapter = new MenuCardAdapter(getContext(), list, this);
        recyclerView.setAdapter((menuCardAdapter));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onClick(View v) {

        //אם זה לא הלחיצה ראשונה הכפתור הקודם שנלחץ מקבל חזרה צבע רקע לבן
        if (select_button != null) {
            select_button.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            select_button.setTextColor(Color.parseColor("#808080"));
        }
        //השמת הכפתור הנלחץ
        select_button = getButton(v);
        //צביעת רקע בכחול הכתפור המלחץ
        select_button.setBackgroundColor(Color.parseColor("#3B5BBDED"));
        select_button.setTextColor(Color.parseColor("#1684E4"));

        intAdapter((ArrayList<Dish>) dictionary.get(v.getTag()));

    }

    //פונקציה שמחזירה את הגפתור הנלחץ
    private MaterialButton getButton(View v) {
        MaterialButton button_to_paint = null;
        for (MaterialButton button : button_list) {
            if (button.equals(v))
                button_to_paint = button;
        }

        return button_to_paint;
    }

    //הוספת מנה לעגלה
    private void addToCart(Dish dish) {


        //אם המנה כבר קיימת בעגלה תעלה רק את הכמות שלה
        if (cart.contains(dish)) {
            (cart.get(cart.indexOf(dish))).setAmount(dish.getAmount());
            return;
        }

        cart.add(dish);
    }


    //הצגת המנה בחון קופץ
    public void popUpWindow(Dish dish) {

        //לולאה שעוברת על העגלה לבדוק האם המנה קיימת כבר בעגלה
        for (int i = 0; i < cart.size(); i++) {
            if (dish.getID().equals(cart.get(i).getID())) {
                dish = cart.get(i);
                break;
            }
        }


        dishDialog = new DishDialog(dish, this);
        dishDialog.show(getChildFragmentManager(), "dish dialog");

    }


    //קריאה לפנוקציה הוספת עגלה וסגירת החון קופץ
    @Override
    public void AddDish(Dish dish) {
        addToCart(dish);
        dishDialog.dismiss();
    }

    //שליחת המנה לפונקצית הצגה
    @Override
    public void showPopUpWindow(Dish dish) {
        popUpWindow(dish);
    }
}