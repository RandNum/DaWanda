package com.laptop.dawanda.main;

import android.graphics.Bitmap;

/**
 * Created by laptop on 24-01-16.
 */
public class CategoryObject {

    public CategoryObject(String name, Bitmap inBitmap){
        catNameObj = name;
        catBitmapObj = inBitmap;

    }

    protected String catNameObj;
    protected Bitmap catBitmapObj;

}
