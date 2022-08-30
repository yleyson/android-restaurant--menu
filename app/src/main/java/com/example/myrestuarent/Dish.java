package com.example.myrestuarent;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Dish implements Parcelable {

    private String ID;
    private String Name;
    private String Desc;
    private double price;
    private double total_price;
    private String Img;
    private int Amount = 0;
    private ArrayList<String> ingredients;
    private ArrayList<String> sensitivity;

    public Dish(){}

    public Dish(Dish dish){
        this.ID = dish.ID;
        this.Name = dish.Name;
        this.Desc = dish.Desc;
        this.price = dish.price;
        this.total_price = dish.price;
        this.Img = dish.Img;
        this.Amount = dish.Amount;
        this.ingredients = dish.ingredients;
        this.sensitivity = dish.sensitivity;
    }



    public Dish(String ID, String name, String desc, double price, String img, ArrayList<String> ingredients,ArrayList<String> sensitivity) {
        this.ID = ID;
        Name = name;
        Desc = desc;
        this.price = price;
        this.total_price = price;
        Img = img;
        this.ingredients = ingredients;
        this.sensitivity=sensitivity;
    }

    protected Dish(Parcel in) {
        ID = in.readString();
        Name = in.readString();
        Desc = in.readString();
        price = in.readDouble();
        total_price = in.readDouble();
        Img = in.readString();
        Amount = in.readInt();
        ingredients = in.createStringArrayList();
        sensitivity=in.createStringArrayList();
    }

    public static final Creator<Dish> CREATOR = new Creator<Dish>() {
        @Override
        public Dish createFromParcel(Parcel in) {
            return new Dish(in);
        }

        @Override
        public Dish[] newArray(int size) {
            return new Dish[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(ArrayList<String> sensitivity) {
        this.sensitivity = sensitivity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Name);
        dest.writeString(Desc);
        dest.writeDouble(price);
        dest.writeDouble(total_price);
        dest.writeString(Img);
        dest.writeInt(Amount);
        dest.writeStringList(ingredients);
        dest.writeStringList(sensitivity);
    }


}
