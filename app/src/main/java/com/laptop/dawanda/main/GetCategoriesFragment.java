package com.laptop.dawanda.main;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.laptop.dawanda.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A placeholder fragment containing a simple view.
 */
public class GetCategoriesFragment extends Fragment {


    public GetCategoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //get the categories data on create view
        FetchCategoriesTask categoriesTask = new FetchCategoriesTask();
        categoriesTask.execute();

        View catView = inflater.inflate(R.layout.fragment_Get_Categories, container, false);

        return catView;
    }

    //was public class FetchCategorieTask extends AsyncTask<String, Void, String[]>
    public class FetchCategoriesTask extends AsyncTask<ArrayList, Void, ArrayList> {

        private final String LOG_TAG = FetchCategoriesTask.class.getSimpleName();

        /**
         * Take the String representing the Categories in JSON and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * The constructor takes the JSON string and converts it
         * to an object
         */
        private ArrayList getCategoriesDataFromJson(String categoriesJSON)
                throws JSONException {

            //The names of the JSON objects that need to be extracted.
            final String CAT_LIST = "data";
            final String CAT_NAME = "name";
            final String CAT_IMGURL = "image_url";

            ArrayList<JSONObject> catArrayList = new ArrayList<>();

            JSONObject categoriesJson = new JSONObject(categoriesJSON);
            JSONArray categoriesArray = categoriesJson.getJSONArray(CAT_LIST);

            String[] resultStrs = new String[categoriesArray.length()];
            for(int i = 0; i < categoriesArray.length(); i++) {
                // Get the JSON object representing a single category in the list
                JSONObject singleCategory = categoriesArray.getJSONObject(i);
                //add the name and image_url component to the arraylist
                catArrayList.add(singleCategory);
            }

            return catArrayList;

        }
        @Override
        protected ArrayList doInBackground(ArrayList... params) {

            // declare outside the try/catch so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String categoriesJsonStr = null;

            try {
                URL url = new URL("http://public.dawanda.in/category.json");
                Log.v(LOG_TAG, "The json categories URL: " + url.toString());
                // Create the request to DaWanda, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET"); //http get
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null; // Nothing to do.
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // add a new line for debugging purposes
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // The stream is empty so there is no reason to parse.
                    return null;
                }
                categoriesJsonStr = buffer.toString();

                Log.v(LOG_TAG, "JSON string: " + categoriesJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the data, there's no point in attempting to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try { reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            //***finally attempt to parse the data***
            try {
                return getCategoriesDataFromJson(categoriesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the JSON.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList arraylist) {
            super.onPostExecute(arraylist);
          categoriesAdapter adapter = new categoriesAdapter(getActivity().getApplicationContext(), R.id.catList, arraylist);
        ListView listView = (ListView) getActivity().findViewById(R.id.catList);
        listView.setAdapter(adapter);

        }
    }

}
