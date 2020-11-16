package com.minevera.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.asynctasks.GroupHomeActionTask;
import com.minevera.auxiliars.ComparadorDatesProductHome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ExpiredListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private RecyclerView recyclerView;
    private List<ProductHome> listExpired;
    private SwipeRefreshLayout swipeContainer;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ExpiredListFragment() {
    }

    public static ExpiredListFragment newInstance(int columnCount) {
        ExpiredListFragment fragment = new ExpiredListFragment();
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
        View view = inflater.inflate(R.layout.fragment_expiredlist, container, false);
        swipeContainer = (SwipeRefreshLayout) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.fel_recyclerlist);
        listExpired = new ArrayList<ProductHome>();
        // Set the adapter
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new ExpiredListRecyclerViewAdapter(listExpired, mListener, this.getContext()));
        initItemTouchHelper();
        return view;
    }

    private void initItemTouchHelper() {
        ItemTouchHelper mIth = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar.make(swipeContainer, "Elemento seleccionado eliminado", Snackbar.LENGTH_SHORT).show();
            ((ExpiredListRecyclerViewAdapter) recyclerView.getAdapter()).remove(viewHolder.getAdapterPosition());
        }
    });
        mIth.attachToRecyclerView(recyclerView);
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

    @Override
    public void onStart() {
        super.onStart();
        updateDates(GroupHomeActionTask.LOAD_GH);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void updateDates(GroupHomeActionTask loadGh) {
    /*Realizamos la accion necesaria en la BBDD obteniendo en Controller.gh los datos actualizados*/
        Controller.loadActionTask(loadGh);
        /*Refrescamos la lista*/
        refreshList();
    }
    @Override
    public void onRefresh() {
        updateDates(GroupHomeActionTask.LOAD_GH);
        swipeContainer.setRefreshing(false);
    }

    private void refreshList() {
        listExpired.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        if(Controller.gh.getHomeList() != null) {
            for (int i = 0; i < Controller.gh.getHomeList().size(); i++) {
                for (int j = 0; j < Controller.gh.getHomeList().get(i).getInfoUnits().size(); j++) {
                    if(isProductExpired(Controller.gh.getHomeList().get(i).getInfoUnits().get(j).getDateExpired())){
                         /*Producto: No consumido y Si expirado --> add result*/
                        listExpired.add(Controller.gh.getHomeList().get(i));
                        break;
                    }
                }
            }
        }else{
            Controller.gh.setHomeList(new ArrayList<ProductHome>());
        }
        Collections.sort(listExpired,new ComparadorDatesProductHome());
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private boolean isProductExpired(String expiredDate) {
        try {
            /**Obtenemos las fechas enviadas en el formato a comparar*/
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

            Date fechaDate1 = getDateLimit(formateador);
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
                    return true;
                }
            }
        } catch (ParseException e) {
        }
        return false;
    }
    private Date getDateLimit(SimpleDateFormat formateador) throws ParseException {
        Date currentDate = new Date();
        String systemDate=formateador.format(currentDate);
        Date date =formateador.parse(systemDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // Configuramos la fecha que se recibe
        calendar.add(Calendar.DAY_OF_YEAR, 5);  // numero de días a añadir, o restar en caso de días<0
        return calendar.getTime();
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
        void onListFragmentInteraction(ProductHome item);
    }
}
