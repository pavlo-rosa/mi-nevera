package com.minevera.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.asynctasks.GroupHomeActionTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NumberPickerItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NumberPickerItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NumberPickerItemFragment extends DialogFragment implements View.OnClickListener {

    int mPosition;
    private ProductHome productHome;
    private NumberPicker mPicker;
    private OnFragmentInteractionListener mListener;
    private final String TAG = "NumberPickerFragment";

    public NumberPickerItemFragment() {}

    public static NumberPickerItemFragment newInstance(int num) {
        NumberPickerItemFragment fragment = new NumberPickerItemFragment();
        Bundle args = new Bundle();
        args.putInt("num", num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosition = getArguments().getInt("positionItem");
        productHome = Controller.gh.getHomeList().get(mPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment fnpi_numberPicker
        Log.i(TAG,"init");
        View view= inflater.inflate(R.layout.fragment_number_picker_item, container, false);
        mPicker = (NumberPicker) view.findViewById(R.id.fnpi_numberPicker);
        mPicker.setMinValue(1);
        mPicker.setMaxValue(productHome.getUnits());
        getDialog().setTitle("Unidades a reducir");
        // Watch for button clicks.
        view.findViewById(R.id.fnpi_buttonOK).setOnClickListener(this);
        view.findViewById(R.id.fnpi_buttonCancel).setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void returnDialog(int newUnits) {
        if (mListener != null) {
            mListener.onFragmentInteraction(newUnits);
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
            case R.id.fnpi_buttonOK: uptadeProductHome();
                break;
            case R.id.fnpi_buttonCancel:  getDialog().dismiss();
                break;
        }
    }

    private void uptadeProductHome() {
        for(int i = 0 ; i < mPicker.getValue() && !productHome.getInfoUnits().isEmpty(); i++){
            int rest = productHome.getInfoUnits().get(0).getNumber() - 1;
            productHome.getInfoUnits().get(0).setNumber(rest);
            if(rest == 0){
                productHome.getInfoUnits().remove(0);
            }
        }
        if (productHome.getInfoUnits().isEmpty()){
            Controller.gh.getHomeList().remove(mPosition);
        }else{
            Controller.gh.getHomeList().set(mPosition,productHome);
        }
        Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);
        getDialog().dismiss();
        goToHome();
    }

    private void goToHome() {
        Fragment fragment = new HomeListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_navegation_drawerID, fragment).commit();
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
        void onFragmentInteraction(int newUnits);
    }
}
