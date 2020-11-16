package com.minevera.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.backend.groupHomeApi.model.ProductHome;
import com.google.zxing.integration.android.IntentIntegrator;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.activities.CaptureActivityAnyOrientation;
import com.minevera.activities.InsertProductHome1Activity;
import com.minevera.asynctasks.GroupHomeActionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HomeListFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener, SearchView.OnQueryTextListener {

    private final String TAG = "HomeListFragment";
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    private List<ProductHome> homeList;
    private RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;
    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;

    public HomeListFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_homelist, menu);

        searchView = (SearchView) MenuItemCompat.getActionView( menu.findItem(R.id.mhl_search));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        List<ProductHome> filteredList = filter(Controller.gh.getHomeList(), newText);
        ((HomeListRecyclerViewAdapter) recyclerView.getAdapter()).setFilter(filteredList);
        return false;
    }

    private List<ProductHome> filter(List<ProductHome> models, String query) {
        query = query.toLowerCase();
        final List<ProductHome> filteredModelList = new ArrayList<>();
        for (ProductHome model : models) {
            final String text = model.getNameProduct().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mhl_text) {
            intentText();
            return true;
        }
        else if (id == R.id.mhl_scan) {
            intentCamara();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homelist, container, false);
        swipeContainer = (SwipeRefreshLayout) view;
        homeList = new ArrayList<ProductHome>();
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.fhl_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new HomeListRecyclerViewAdapter(homeList, mListener,  getContext()));
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
                ((HomeListRecyclerViewAdapter) recyclerView.getAdapter()).remove(viewHolder.getAdapterPosition());
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


    private void intentText() {
        Intent iInsertProductText = new Intent(this.getContext(), InsertProductHome1Activity.class);
        startActivity(iInsertProductText);
    }

    private void intentCamara() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Escanear codigo de barras");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);

        integrator.setCaptureActivity(CaptureActivityAnyOrientation.class);
        integrator.setOrientationLocked(false);

        integrator.initiateScan();
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
        homeList.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        if(Controller.gh.getHomeList() != null) {
            homeList.addAll(Controller.gh.getHomeList());
        }else{
            Controller.gh.setHomeList(new ArrayList<ProductHome>());
        }
//        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.setAdapter(new HomeListRecyclerViewAdapter(homeList, mListener,  getActivity()));
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
