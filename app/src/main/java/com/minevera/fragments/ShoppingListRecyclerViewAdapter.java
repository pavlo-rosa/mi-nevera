package com.minevera.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backend.groupHomeApi.model.ProductBuy;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.fragments.ShoppingListFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ShoppingListRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = ShoppingListRecyclerViewAdapter.class.getName();
    private List<ProductBuy> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public ShoppingListRecyclerViewAdapter(List<ProductBuy> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_shoppinglist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getNameProduct());
        holder.mUnits.setText(mValues.get(position).getUnits()+" "+mValues.get(position).getKindQuantity());
        printImage(holder, position);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction();
                }
            }
        });
    }

    private void printImage(ViewHolder holder, int position) {
        try {
            if(mValues.get(position).getImage().contains("NeedStringToBitMap#")){
                String image = mValues.get(position).getImage().replace("NeedStringToBitMap#","");
                Bitmap bitmap = Controller.StringToBitMap(image);
                holder.mImage.setImageBitmap(bitmap);
            }else {
                Picasso.with(mContext).load(mValues.get(position).getImage()).into(holder.mImage);
            }
        }catch (NullPointerException e){
            holder.mImage.setImageDrawable(null);
        }
    }


    @Override
    public int getItemCount() {
       return mValues.size();
    }

    public void remove(int position) {
//        int removePosition = Controller.findByIDBUY(mValues.get(position).getNameProduct(),  Controller.gh.getShoppingList());
        Controller.removeShop(position);

        mValues.remove(position);
        notifyItemRemoved(position);
    }





    /******************************************    ViewHolder   ***********************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ProductBuy mItem;
        public final TextView mName,mUnits;
        public final ImageView mImage;

        public ViewHolder(final View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.fsli_name);
            mImage = (ImageView) view.findViewById(R.id.fsli_image);
            mUnits = (TextView) view.findViewById(R.id.fsli_units);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
