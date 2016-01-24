package com.laptop.dawanda.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.laptop.dawanda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by laptop on 23-01-16.
 */
public class categoriesAdapter extends ArrayAdapter<JSONObject>{

    List<JSONObject> myJSONAdapterObjects;
    JSONObject singleCatObject;

    private static class ViewHolder {
        TextView name;
        TextView imgURL;
    }


    public categoriesAdapter(Context context, int resource, List<JSONObject> objects) {
        super(context, resource, objects);
        myJSONAdapterObjects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        // Get the data item for this position
        singleCatObject  = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.category_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.nameField);
            viewHolder.imgURL = (TextView) convertView.findViewById(R.id.imageURL);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        try {
            viewHolder.name.setText(singleCatObject.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            viewHolder.imgURL.setText(singleCatObject.getString("image_url"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return convertView;




    }
}
