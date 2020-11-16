package com.minevera.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backend.groupHomeApi.model.ProductDate;
import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.asynctasks.GroupHomeActionTask;
import com.minevera.fragments.HomeListFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HomeListRecyclerViewAdapter extends RecyclerView.Adapter<HomeListRecyclerViewAdapter.ViewHolder> {

    private final String TAG = "HomeListViewAdapter";
    private List<ProductHome> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;
    private HomeListRecyclerViewAdapter adapter;
    public HomeListRecyclerViewAdapter(List<ProductHome> items, OnListFragmentInteractionListener listener,  Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
        adapter = this;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_homelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
       // printExtraMargin(holder, position);
        holder.mName.setText(mValues.get(position).getNameProduct());
        holder.mQuantity.setText(String.valueOf(mValues.get(position).getUnits()));
        holder.mQuantityKind.setText(mValues.get(position).getKindQuantity());
        printDateExpired(holder, position);
        printImage(holder, position);
        printDateExpired(holder, position);
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //removeOneItemClick(position, holder);
                reduceUnits(position);
                notifyItemChanged(position);

            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private void reduceUnits(int position) {
        int positionElementInHomeList = Controller.findByIDHOME(mValues.get(position).getNameProduct(), Controller.gh.getHomeList());
        Bundle bundle = new Bundle();
//        int maxN = mValues.get(position).getUnits();
        bundle.putInt("positionItem", positionElementInHomeList );
       // bundle.putInt("maxN", maxN );

        FragmentActivity activity = (FragmentActivity)(mContext);
        FragmentManager fm = activity.getSupportFragmentManager();
        NumberPickerItemFragment dateFragment = new NumberPickerItemFragment();
        dateFragment.setArguments(bundle);
        dateFragment.show(fm, "fragment_number_picker_item");
    }

    private void removeOneItemClick(int position, ViewHolder holder) {
        int positionElementInHomeList = Controller.findByIDHOME(mValues.get(position).getNameProduct(), Controller.gh.getHomeList());
        int contTotalUnitsProduct = countUnitsLocal(Controller.gh.getHomeList().get(positionElementInHomeList))-1;
        int cont = Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().get(0).getNumber()-1;
                /*Nuevo numero -1*/
        Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().get(0).setNumber(cont);
        //mValues.get(position).getInfoUnits().get(0).setNumber(cont);
        if(cont <= 0) {
            /*Si nos quedamos a cero de ese InfoUnits concreto, lo eliminamos y a la siguiente se pondra el siguiente infounits*/
            Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().remove(0);
            printDateExpired(holder, position);
        }
                /*Sino, reducimos el numero y actualizamos*/
        holder.mQuantity.setText(String.valueOf(contTotalUnitsProduct));
        if (contTotalUnitsProduct <= 0) {
                /*Sino quedan mas unidades  se elimina de productHome*/
            remove(position);
        }
        Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);
    }

    private int countUnitsLocal(ProductHome product){
        int count = 0;
        for (int i = 0; i < product.getInfoUnits().size(); i++){
            count = count + product.getInfoUnits().get(i).getNumber();
        }
        return count;
    }
    private void printExtraMargin(ViewHolder holder, int position) {
        if(mValues.size() == (position+1)){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            params.setMargins(0, 0, 0, 170); //substitute parameters for left, top, right, bottom
            holder.itemView.setLayoutParams(params);
        }
    }

    private void printDateExpired(ViewHolder holder, int position) {
        int positionElementInHomeList = Controller.findByIDHOME(mValues.get(position).getNameProduct(), Controller.gh.getHomeList());
        boolean allNoExpired = true;
        for (ProductDate date:Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits()) {
            if(!date.getDateExpired().equals("01/01/9999") && allNoExpired){
                allNoExpired = false;
            }
        }
        if(allNoExpired){
            holder.mDate.setText("Sin Caducidad");
        }else{
            if(Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().size() == 1){
                holder.mDate.setText(Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().get(0).getDateExpired());
            }else{
                holder.mDate.setText(Controller.gh.getHomeList().get(positionElementInHomeList).getInfoUnits().size()+" Fechas");
            }
        }

    }

    private void printImage(ViewHolder holder, int position) {
       if(mValues.get(position).getImage()!=null) {
           if (mValues.get(position).getImage().contains("NeedStringToBitMap#")) {
               String image = mValues.get(position).getImage().replace("NeedStringToBitMap#", "");
               Bitmap bitmap = Controller.StringToBitMap(image);
               holder.mImage.setImageBitmap(bitmap);
           } else {
               Picasso.with(mContext).load(mValues.get(position).getImage()).into(holder.mImage);
           }
       }else{
           holder.mImage.setImageDrawable(null);
       }
    }

    public void remove (int position){
        int removePosition = Controller.findByIDHOME(mValues.get(position).getNameProduct(), Controller.gh.getHomeList());
        Toast.makeText(mContext, Controller.gh.getHomeList().get(removePosition).getNameProduct() + " eliminado", Toast.LENGTH_SHORT).show();
        Controller.gh.getHomeList().remove(removePosition);
        Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);

        mValues.remove(position);
        notifyItemRemoved(position);
    }

    public void setFilter(List<ProductHome> list) {
        mValues = new ArrayList<ProductHome>();
        mValues.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mDate;
        public final TextView mQuantity;
        public final TextView mQuantityKind;
        public final ImageView mImage;
        public final ImageButton mButton;
        public ProductHome mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.fhli_name);
            mDate = (TextView) view.findViewById(R.id.fhli_dateExpired);
            mQuantity = (TextView) view.findViewById(R.id.fhli_units);
            mQuantityKind = (TextView) view.findViewById(R.id.fhli_quantityKind);
            mImage = (ImageView) view.findViewById(R.id.fhli_image);
            mButton = (ImageButton) view.findViewById(R.id.fhli_imageButtonQuantity);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }
}
