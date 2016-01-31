package com.laptop.dawanda.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.laptop.dawanda.R;
import com.laptop.dawanda.main.ProductsFragment.OnListFragmentInteractionListener;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Products} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private final List<Products> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ProductRecyclerViewAdapter(List<Products> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                //may need to modify this when attaching view to "true" so that it
                //uses the parent view and adds the new view the to parent container
                .inflate(R.layout.fragment_products, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mProduct = mValues.get(position);
        holder.mprodname.setText(mValues.get(position).getProdName());
        holder.mprodprice.setText(mValues.get(position).getProdPrice());
        holder.mprodimage.setImageBitmap(mValues.get(position).getProdImage());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mprodname;
        public final TextView mprodprice;
        public final ImageView mprodimage;
        public Products mProduct;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mprodname = (TextView) view.findViewById(R.id.prodtitle);
            mprodprice = (TextView) view.findViewById(R.id.prodprice);
            mprodimage = (ImageView) view.findViewById(R.id.prodimage);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mprodname.getText() + " ' " + mprodprice.getText() + "'";
        }
    }
}
