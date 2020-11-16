package com.minevera.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backend.groupHomeApi.model.Product;
import com.backend.groupHomeApi.model.ProductBuy;
import com.backend.groupHomeApi.model.ProductHome;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.activities.InsertShoppingTextActivity;
import com.minevera.asynctasks.GroupHomeActionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ShoppingListFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{

    private static String TAG = ShoppingListFragment.class.getName();

    private List<ProductBuy> purchaseList;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout swipeContainer;
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShoppingListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Cargamos e inicializamos los elementos de la vista
        View view = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        swipeContainer = (SwipeRefreshLayout) view;
        FloatingActionButton mButton = (FloatingActionButton) view.findViewById(R.id.fsl_add);
        mButton.setOnClickListener(this);


        purchaseList = new ArrayList<ProductBuy>();
        initRecyclerView(view, view.getContext());
        return view;
    }

    private void initRecyclerView(View view, Context context) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.fsl_listRecyclerView);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        mRecyclerView.setAdapter(new ShoppingListRecyclerViewAdapter(purchaseList, mListener, getContext()));
        initItemTouchHelper();
    }

    private void initItemTouchHelper() {
        ItemTouchHelper mIth = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                ((ShoppingListRecyclerViewAdapter) mRecyclerView.getAdapter()).remove(viewHolder.getAdapterPosition());
            }
        });
        mIth.attachToRecyclerView(mRecyclerView);
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
    public void onClick(View v) {
        startActivity(new Intent(getContext(), InsertShoppingTextActivity.class));
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

    @Override
    public void onRefresh() {
        Log.i(TAG, "onRefresh");
        /*Refrescamos manualmente*/
        updateDates(GroupHomeActionTask.LOAD_GH);
        swipeContainer.setRefreshing(false);
    }

    private void calcularStocks() {
        try{
        /*Obtenemos la lista de productos habituales*/
        Product product2=null;
        for (Product product : Controller.gh.getProductsList()){
            product2 = product;
            if(product.getHabitual() && existInListHomeEnough(product)==false){
            /*Si es habitual y el numero de unidades es <= al minimo --> add a shopping list*/
                ProductBuy pb = new ProductBuy();
                pb.setNameProduct(product.getNameProduct());
                pb.setUnits(product.getStockQuantity());
                pb.setKindQuantity(product.getKindQuantity());
                try{pb.setImage(product.getImage());}catch (NullPointerException e){}
                Controller.addShop(pb);
            }
        }

            ProductHome  productHome = productHomeSearch(product2);
            for (int i = 0; i < Controller.gh.getShoppingList().size(); i++){
                if(product2.getHabitual() && Controller.gh.getShoppingList().get(i).getNameProduct().equals(product2.getNameProduct()) && productHome != null && productHome.getUnits() > product2.getStockQuantity()){
                    Controller.gh.getShoppingList().remove(i);
                    Controller.loadActionTask(GroupHomeActionTask.UPDATE_GH);
                }
            }
        }catch (NullPointerException e){Controller.gh.setShoppingList(new ArrayList<ProductBuy>());}
    }

    private boolean existInListHomeEnough(Product product) {
        boolean exist = true;
        if(!existInListShopping(product)){
            exist = false;
            for (ProductHome productH : Controller.gh.getHomeList()){
                if(productH.getNameProduct().equals(product.getNameProduct())){
                    exist = true;
                    if(productH.getUnits() <= product.getStockQuantity()){
                        return false;
                    }
                }
            }
        }else {
            return true;
        }
        return exist;
    }

    private boolean existInListShopping(Product product) {
        for (int i = 0; i < Controller.gh.getShoppingList().size(); i++){
            if(Controller.gh.getShoppingList().get(i).getNameProduct().equals(product.getNameProduct())){
               return true;
            }
        }
        return  false;
    }

    private ProductHome productHomeSearch(Product product) {
        for (int i = 0; i < Controller.gh.getHomeList().size(); i++){
            if(Controller.gh.getHomeList().get(i).getNameProduct().equals(product.getNameProduct())){
                return Controller.gh.getHomeList().get(i);
            }
        }
        return  null;
    }


    private void updateDates(GroupHomeActionTask action) {
            calcularStocks();
        /*Realizamos la accion necesaria en la BBDD obteniendo en Controller.gh los datos actualizados*/
        Controller.loadActionTask(action);
        /*Refrescamos la lista*/
        refreshList();
    }

    private void refreshList() {
        purchaseList.clear();
        mRecyclerView.getAdapter().notifyDataSetChanged();

        if(Controller.gh.getShoppingList() != null) {
            purchaseList.addAll(Controller.gh.getShoppingList());
        }else{
            Controller.gh.setShoppingList(new ArrayList<ProductBuy>());
        }

        mRecyclerView.getAdapter().notifyDataSetChanged();
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
        void onListFragmentInteraction();
    }



}
