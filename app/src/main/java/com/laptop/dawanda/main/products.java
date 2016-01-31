package com.laptop.dawanda.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;



public class Products {
    private String prodName;
    private String prodPrice;
    private Bitmap prodImage;


    public Products(String name, String price, Bitmap image) {
        prodName = name;
        prodImage = image;
        prodPrice = price;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public Bitmap getProdImage() {
        return prodImage;
    }


}
