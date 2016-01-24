package com.laptop.dawanda.main;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laptop on 23-01-16.
 */
public class categoriesAdapter extends ArrayAdapter<JSONObject>{

    List<JSONObject> myJSONAdapterObjects;
    JSONObject singleCatObject;

    public categoriesAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        myJSONAdapterObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);






    }
}
