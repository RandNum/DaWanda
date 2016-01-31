package com.laptop.dawanda.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductsFragment newInstance(int columnCount) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FetchProductsTask fetchProductsTask = new FetchProductsTask();
        fetchProductsTask.execute();

        //inflater.inflate(R.layout.fragment_products or .fragment_products_list and true or false
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);

        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//         }



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Products item);
    }



    public class FetchProductsTask extends AsyncTask<ArrayList, Void, ArrayList> {


        private final String LOG_TAG = FetchProductsTask.class.getSimpleName();

        /**
         * Take the String representing the Categories in JSON and
         * pull out the data we need to construct the Strings needed for the wireframes.
         * The constructor takes the JSON string and converts it
         * to an object
         */
        private ArrayList getCategoriesDataFromJson(String categoriesJSON)
                throws JSONException {

            //The names of the JSON objects that need to be extracted.
            final String PROD_LIST = "data";
            final String PROD_NAME = "title";
            final String PROD_PRICE = "price";
            final String CAT_IMGURL = "default_image";

            ArrayList<Products> catArrayList = new ArrayList<>();

            JSONObject categoriesJson = new JSONObject(categoriesJSON);
            JSONObject justCatData = categoriesJson.getJSONObject("data");
            JSONObject justProducts = justCatData.getJSONObject("data");
            JSONArray categoriesArray = justProducts.getJSONArray("products");


            //String[] resultStrs = new String[categoriesArray.length()];
            int p = categoriesArray.length();
            for (int i = 0; i < categoriesArray.length(); i++) {
                // Get the JSON object representing a single category in the list
                JSONObject singleProduct = categoriesArray.getJSONObject(i);
                //add the name and image_url component to the arraylist

                Bitmap catImageBitmap = null;
                try {
                    JSONObject imageObjects = singleProduct.getJSONObject("default_image");
                    String listviewObject = imageObjects.getString("listview");
                    System.out.println(listviewObject);
                    InputStream streamIn = new java.net.URL("http:" + listviewObject).openStream();
                    System.out.println(streamIn);
                    catImageBitmap = BitmapFactory.decodeStream(streamIn);
                } catch (Exception e) {
                    // Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }

                catArrayList.add(new Products(singleProduct.getString(PROD_NAME), singleProduct.getString(PROD_PRICE), catImageBitmap));
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
                URL url = new URL("http://public.dawanda.in/products.json");
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
                    try {
                        reader.close();
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
//            categoriesAdapter adapter = new categoriesAdapter(getActivity().getApplicationContext(), R.id.catList, arraylist);
//            ListView listView = (ListView) getActivity().findViewById(R.id.catList);
//            listView.setAdapter(adapter);

            RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.productsrecyclerviewlist);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
            recyclerView.setAdapter(new ProductRecyclerViewAdapter(arraylist, mListener));


            //Now that adapter is set, add the onItemClick Listeners

        }


    }

}
