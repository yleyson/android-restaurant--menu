package com.example.myrestuarent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements EnterDialog.MenuPageListenerFinal, EnterDialog.FillterPageListner,
        CartPage.CartPageListener, FilterFragment.FillterPageListnerGoBack, ReceiptPage.ReceiptPageListener {


    //חיבור לדאטה בייס
    DatabaseReference ref;
    //רשימת תפריטים ועגלה
    ArrayList<Dish> menu_dishes, cart = new ArrayList<Dish>();

    //nav bar
    MaterialToolbar materialToolbar;
    //מילון שמחזיק את כל רשימות התפריטים
    // Map<String, ArrayList<Dish>> dictionary = new HashMap<String, ArrayList<Dish>>();
    Map<String, Object> dictionary = new HashMap<String, Object>();
    //קוד מסעדה ושם מסעדה
    String res_code="", res_name, res_img,res_phone;
    //משתנה בדיקת תקינות קוד מסעדה, החלפת פרייגמנט ראשון, בדיקה שטעינת מסך מסעדה הסתיים
    Boolean check_code = false, first_enter = false, load_finish = false;
    //דיאלוג החלפת מסעדה
    MaterialAlertDialogBuilder dialog_res_change, dialog_cart_empty, dialog_finish;

    //בדיקה האם קיימת מנה אלרגית שסומנה
    Boolean isElergic = false;

    //רשימה של אלרגיות שסומנו
    //רשימת קבלה
    ArrayList<String> arrayList = new ArrayList<String>(),receipt=new ArrayList<>();

    Handler handler = new Handler();

    @Override
    public void onBackPressed() {
        dialog_res_change.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        replaceFragmnet(new EnterDialog(""));
        intVarbs();
        intAlertDialogChangeRes();
        intAlertDialogCartEmpty();
        intAlertDialogFinish();
        intViews();
        intButtons();

    }

    //יצירת דיאלוג סגירת אפלקצייה
    private void intAlertDialogFinish() {
        dialog_finish = new MaterialAlertDialogBuilder(this);
        dialog_finish.setTitle("תודה שהזמנת אצלנו ובתאבון");
        dialog_finish.setPositiveButton("סגור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog_finish.setCancelable(false);
        dialog_finish.create();
    }


    //יצירת דיאלוג החלפת מסעדה
    private void intAlertDialogChangeRes() {
        dialog_res_change = new MaterialAlertDialogBuilder(this);
        dialog_res_change.setTitle("שינוי מסעדה");
        dialog_res_change.setMessage("אתה בטוח שאתה רוצה להחליף מסעדה");
        dialog_res_change.setNeutralButton("לא", null);
        dialog_res_change.setPositiveButton("כן", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //איפוס משתנים
                load_finish = false;
                dictionary = new HashMap<String, Object>();
                cart = new ArrayList<Dish>();
                arrayList = new ArrayList<>();
                check_code = false;
                materialToolbar.setTitle("מסעדה כלשהי");

                replaceFragmnet(new EnterDialog(""));
            }
        });
        dialog_res_change.create();

    }

    //יצירת דיאלוג עגלה ריקה
    private void intAlertDialogCartEmpty() {
        dialog_cart_empty = new MaterialAlertDialogBuilder(this);
        dialog_cart_empty.setTitle("עגלת קניות ריקה");
        dialog_cart_empty.setMessage("לחץ על סגור כדי לחזור לתפריט");
        dialog_cart_empty.setPositiveButton("סגור", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                replaceFragmnet(new MenuPage(dictionary, cart));
            }
        });
        dialog_cart_empty.create();
    }

    //אתחול אפלקצייה
    public void applyText(String code) {
        res_code = code;
        getData();
    }

    private void intVarbs() {
        ref = FirebaseDatabase.getInstance().getReference().child("Restaurants");
    }

    private void intViews() {
        materialToolbar = findViewById(R.id.topAppBarHome);
    }

    //אתחול כפתורי תפריט ועגלה ב - navbar
    //אם הקוד שגוי לא ניתן להיכנס
    //לחיצה אפשרית רק אחרי טעינת דף ברוכים הבאים
    private void intButtons() {

        materialToolbar.setNavigationOnClickListener(v -> {
            if (!load_finish)
                return;
            replaceFragmnet(new MenuPage(dictionary, cart));
        });

        materialToolbar.setOnMenuItemClickListener(v -> {
            if (!load_finish)
                return false;
            if (cart.isEmpty()) {
                dialog_cart_empty.show();
                return false;
            }
            replaceFragmnet(new CartPage(cart,receipt));
            return true;
        });
    }


    //קבלת תפריט מהדאטה בייס אם קוד מסעדה תקין
    private void getData() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //לולאה שעוברת על כל המסעדות
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //של כל מסעדה - id
                    String id = snapshot.child("ID").getValue().toString();
                    //אם ה - id שווה לקוד מסעדה שהוכנס ייקלט התפריט למילון
                    if (id.equals(res_code)) {

                        //השמת שם מסעדה במשתנה
                        res_name = snapshot.getKey();
                        res_img = snapshot.child("IMG").getValue().toString();
                        res_phone = snapshot.child("Phone").getValue().toString();

                        //משתנה בדיקת תקינות קוד
                        check_code = true;

                        //לולאה שעוברת על כל התפריט של המסעדה שנבחרה
                        for (DataSnapshot snapshotChildMenu : snapshot.child("Menu").getChildren()) {
                            //רשימה שתחזיק את כל המנות של תפריט נוכחי הונכחי
                            menu_dishes = new ArrayList<Dish>();
                            String temp_button_name = "";
                            String temp_button_img = "";
                            //לולאה שעוברת על תפריט נוכחי
                            for (DataSnapshot MenuDishes : snapshotChildMenu.getChildren()) {



                                //שמירת שם התפריט ואייקון התפריט
                                if (MenuDishes.getKey().equals("button_name")) {
                                    temp_button_name = MenuDishes.getValue().toString();

                                    continue;

                                } else if (MenuDishes.getKey().equals("button_img")) {
                                    temp_button_img = MenuDishes.getValue().toString();
                                    continue;
                                }


                                Dish dish = new Dish();
                                //רשימה של מרכיבי מנה
                                ArrayList<String> ing = new ArrayList<>();
                                //רשימה של אלגריות מנה אם קיימים
                                ArrayList<String> sens = new ArrayList<>();
                                //השמת כל תכונות המנה
                                dish.setID(MenuDishes.child("ID").getValue().toString());
                                dish.setName(MenuDishes.child("Name").getValue().toString());
                                dish.setDesc(MenuDishes.child("Desc").getValue().toString());
                                dish.setPrice(Integer.parseInt(MenuDishes.child("Price").getValue().toString()));
                                //מחיר סופי שיתעדכן אם יוזמנו כמה מנות ממנה זו
                                dish.setTotal_price(dish.getPrice());
                                dish.setImg(MenuDishes.child("Img").getValue().toString());
                                //אתחול כמות הזמנות ממנה זו ב-1
                                dish.setAmount(1);
                                //לולאה שעוברת על מרכיבי המנה
                                for (DataSnapshot snapshotIngredients : MenuDishes.child("ingredients").getChildren()) {
                                    ing.add(snapshotIngredients.getValue().toString());
                                }
                                //לולאה שעוברת על רגישיות המנה
                                for (DataSnapshot snapshotSensetive : MenuDishes.child("sensitivity").getChildren()) {
                                    if (arrayList.contains(snapshotSensetive.getValue().toString())) {
                                        isElergic = true;
                                        break;
                                    }
                                    sens.add(snapshotSensetive.getValue().toString());

                                }
                                //אם קיימת רגישות שסומנה במנה הלולאה ממשיכה הלאה
                                if (isElergic == true) {
                                    isElergic = false;
                                    continue;
                                }
                                //הוספת מרכיבים ורגישויות למנה
                                dish.setIngredients(ing);
                                dish.setSensitivity(sens);
                                menu_dishes.add(dish);
                            }
                            //השמת רשימת המנות במילון לפי מפתח שם תפריט
                            dictionary.put(snapshotChildMenu.getKey(), menu_dishes);
                            //מפתח לפי שם תפריט כדי לשייך אליו שם כפתור ואייקון
                            dictionary.put(snapshotChildMenu.getKey()+"btn_name", temp_button_name);
                            dictionary.put(snapshotChildMenu.getKey()+"btn_img", temp_button_img);

                        }


                    }

                }

                if (check_code) {
                    //השמת שם מסעדה
                    materialToolbar.setTitle(res_name);

                    //דליי לעינת דף תפריט
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //העברה לדף תפריט
                            replaceFragmnet(new MenuPage(dictionary, cart));
                            //אפשר לחיצה על הכפתורים
                            load_finish = true;
                        }
                    }, 5000);
                    first_enter = true;
                    replaceFragmnet(new WelcomePage(res_name, res_img));

                } else {
                    Toast.makeText(MainActivity.this, res_code + " קוד מסעדה שגוי", Toast.LENGTH_SHORT).show();
                    //חזרה לדף הכנסת קוד מסעדה
                    replaceFragmnet(new EnterDialog(res_code));

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    //פונקציית החלפת דפים
    private void replaceFragmnet(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        //הפעלה שך הדף מסעדה עם אפקט פייד אין ופייד אווט
        if (first_enter == true) {
            first_enter = false;
            fragmentManager.beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(
                    R.id.frameLayout, fragment
            ).commit();
            return;
        }

        fragmentManager.beginTransaction().replace(
                R.id.frameLayout, fragment
        ).commit();

    }

    //מעבר לפונקציית אתחול עם הקוד שהוכנס
    @Override
    public void GoMenuFinal(String res_code) {

        applyText(res_code);

    }

    //מעבר לדף קבלה
    @Override
    public void GoReceiptPage(double total_order_pay) {
        //check permission
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED)
        {
            //if permission is granted
            sendTextMessage();
            replaceFragmnet(new ReceiptPage(cart, total_order_pay));
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
        }
    }

//שליחת סמס עם הקבלה
    private void sendTextMessage() {
        SmsManager smsManager=SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(res_phone,null,receipt,null,null);
    }

    //הצגת דיאלוג עגלה ריקה
    @Override
    public void GoToMenu() {
        dialog_cart_empty.show();
    }


    //מעבר לדף סינון רגישויות
    @Override
    public void GoToFillter(String res_code) {
        replaceFragmnet(new FilterFragment(arrayList,res_code));

    }

    //מעבר לדף בחירת תפריט
    @Override
    public void GoBack(String res_code) {
        replaceFragmnet(new EnterDialog(res_code));
    }

    //הצגת דיאלוג סיום
    @Override
    public void payAndFinish() {
        dialog_finish.show();

    }
}