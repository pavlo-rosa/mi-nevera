package com.minevera.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.asynctasks.GroupHomeActionTask;
import com.minevera.fragments.ExpiredListFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ExpiredListRecyclerViewAdapter extends RecyclerView.Adapter<ExpiredListRecyclerViewAdapter.ViewHolder> {

    private final List<ProductHome> mValues;
    private final OnListFragmentInteractionListener mListener;
    private final Context mContext;

    public ExpiredListRecyclerViewAdapter(List<ProductHome> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_expiredlist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mName.setText(mValues.get(position).getNameProduct());
        printDateExpired(holder,position);
        printImage(holder,position);

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
    private void printDateExpired(ViewHolder holder, int position) {
        String dates = "";
        String datesN = "";
        String datesQ = "";
        String kind = "";
        String oldS="01/01/9999";
        String newS="Sin Caducidad";
        /*Obtenemos la fecha más anterior para saber si existen o no pasado productos pasados*/
        if(mValues.get(position).getKindQuantity().equals("Unidades")){
            kind="U";
        }else if(mValues.get(position).getKindQuantity().equals("Kilogramos")){
            kind="Kg";
        }
        else if(mValues.get(position).getKindQuantity().equals("Gramos")){
            kind="G";
        }else if(mValues.get(position).getKindQuantity().equals("Litros")){
            kind="L";
        }

        for (int i = 0; i < mValues.get(position).getInfoUnits().size(); i++){
            printAlert(holder, position, i, mValues.get(position).getInfoUnits().get(i).getDateExpired());
            datesN = datesN + mValues.get(position).getInfoUnits().get(i).getNumber()+"\n";
            datesQ = datesQ + kind+"\n";
            dates = dates +mValues.get(position).getInfoUnits().get(i).getDateExpired().replace(oldS,newS)+"\n";

        }
        holder.mDateN.setText(datesN);
        holder.mDate.setText(dates);
        holder.mDateQ.setText(datesQ);
    }

    private void printAlert(ViewHolder holder, int position, int i, String date) {
        if(mValues.get(position).getInfoUnits().get(i).getDateExpired().equals(date) && isProductExpiredToday(mValues.get(position).getInfoUnits().get(i).getDateExpired())){
            /*Si expira hoy-->alert amarillo ic_warning_yellow_24dp*/
            int imageResource2 = mContext.getResources().getIdentifier("@drawable/ic_warning_48dp", null, mContext.getPackageName());
            Drawable resW =  mContext.getResources().getDrawable(imageResource2);
            /*Si en mAlert ya esta dibujado la alerta de caducado prevalece*/
            holder.mAlert.setImageDrawable(resW);
        }else if(mValues.get(position).getInfoUnits().get(i).getDateExpired().equals(date) && isProductExpired(mValues.get(position).getInfoUnits().get(i).getDateExpired())){
            /*Existen productos ya caducados pasados de fecha-->alert rojo ic_error_red_24dp*/
            int imageResource = mContext.getResources().getIdentifier("@drawable/ic_error_48dp", null, mContext.getPackageName());
            Drawable resE =  mContext.getResources().getDrawable(imageResource);
            holder.mAlert.setImageDrawable(resE);
        }
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
            holder.mImage.setImageBitmap(null);
        }
    }



    public void remove (int position){
        int removePosition = Controller.findByIDHOME(mValues.get(position).getNameProduct(), Controller.gh.getHomeList());
        Controller.gh.getHomeList().remove(removePosition);
        Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);
        Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);
        mValues.remove(position);
        notifyItemRemoved(position);
    }


    private boolean isProductExpiredToday(String expiredDate) {

        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            Date currentDate = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String systemDate=formateador.format(currentDate);
            Date fechaDate1 = formateador.parse(systemDate);
            Date fechaDate2 = formateador.parse(expiredDate);;
            if ( fechaDate1.before(fechaDate2) ){
                //"La Fecha 1 es menor ";
                return false;
            }else{
                if ( fechaDate2.before(fechaDate1) ){
                //"La Fecha 1 es Mayor ";
                    return false;
                }else{
                //"Las Fechas Son iguales ";
                    return true;
                }
            }
        } catch (ParseException e) {
        }
        return false;
    }
    private boolean isProductExpired(String expiredDate) {

        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            Date currentDate = new Date();
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            String systemDate=formateador.format(currentDate);
            Date fechaDate1 = formateador.parse(systemDate);
            Date fechaDate2 = formateador.parse(expiredDate);;
            if ( fechaDate1.before(fechaDate2) ){
                //"La Fecha 1 es menor ";
                return false;
            }else{
                if ( fechaDate2.before(fechaDate1) ){
                    //"La Fecha 1 es Mayor ";
                    return true;
                }else{
                    //"Las Fechas Son iguales ";
                    return false;
                }
            }
        } catch (ParseException e) {
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mDate;
        public final TextView mDateQ;
        public final TextView mDateN;
        public final ImageView mImage;
        public final ImageView mAlert;
        public ProductHome mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.feli_name);
            mDate = (TextView) view.findViewById(R.id.feli_dateExpired);
            mDateN = (TextView) view.findViewById(R.id.feli_dateN);
            mDateQ = (TextView) view.findViewById(R.id.feli_dateQ);
            mImage = (ImageView) view.findViewById(R.id.feli_image);
            mAlert = (ImageView) view.findViewById(R.id.feli_imageAlert);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }

    }
}
