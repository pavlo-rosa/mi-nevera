package com.minevera.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.minevera.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DateFragmentText.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DateFragmentText#} factory method to
 * create an instance of this fragment.
 */
public class DateFragmentText extends DialogFragment implements  View.OnClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private DatePicker datePicker;

    private OnFragmentInteractionListener mListener;

    public DateFragmentText() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_date_text, container, false);
        // Inflate the layout for this fragment
        datePicker = (DatePicker) view.findViewById(R.id.fdt_datePicker);
        view.findViewById(R.id.fdt_buttonCancel).setOnClickListener(this);
        view.findViewById(R.id.fdt_buttonAccept).setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void returnDialog(String result) {
        if (mListener != null) {
            mListener.onFragmentInteraction(result);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fdt_buttonAccept:
                returnDialog(parseDate());
                getDialog().dismiss();
                break;
            case R.id.fdt_buttonCancel:
                returnDialog("none");
                getDialog().dismiss();
                break;
        }
    }

    @NonNull
    private String parseDate() {
        int month= datePicker.getMonth()+1;
        String mydate  = datePicker.getDayOfMonth() + "/" + month + "/" + datePicker.getYear();
        SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yyyy");
        String dateExpired = "";
        try {
            Date aux = ft.parse(mydate);
            dateExpired = ft.format(aux);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateExpired;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String result);
    }
}
