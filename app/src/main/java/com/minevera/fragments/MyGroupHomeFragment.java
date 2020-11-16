package com.minevera.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backend.groupHomeApi.model.User;
import com.minevera.Controller;
import com.minevera.R;
import com.minevera.activities.SendInvitationActivity;
import com.minevera.asynctasks.GroupHomeActionTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MyGroupHomeFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private FloatingActionButton myFab;
    private RecyclerView recyclerView;
    private List<User> userList;
    private SwipeRefreshLayout swipeContainer;
    private TextView id,password;
    private int request_code = 1;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MyGroupHomeFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MyGroupHomeFragment newInstance(int columnCount) {
        MyGroupHomeFragment fragment = new MyGroupHomeFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,    Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygrouphome, container, false);
        // Inicializamos los elementos
        swipeContainer = (SwipeRefreshLayout) view;
        putViewIDandPassword(view);

        myFab = (FloatingActionButton)  view.findViewById(R.id.addPersonIc);
        myFab.setOnClickListener(this);

        userList = new ArrayList<>();
        userList.addAll(Controller.gh.getUsersList());

        initRecyclerView(view, view.getContext());
        return view;
    }

    private void initRecyclerView(View view, Context context) {
        recyclerView = (RecyclerView) view.findViewById(R.id.mghList);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new MyGroupHomeRecyclerViewAdapter(userList, mListener));
    }

    private void putViewIDandPassword(View view) {
        id = (TextView) view.findViewById(R.id.MyGH_IDGHtextView);
        password = (TextView) view.findViewById(R.id.MyGH_PasswodtextView);

        id.setText(id.getText()+ Controller.gh.getId().toString());
        password.setText(password.getText()+Controller.gh.getPassword());
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
    public void onRefresh() {
        Controller.loadActionTask(GroupHomeActionTask.LOAD_GH);
        refreshList();
        swipeContainer.setRefreshing(false);
    }

    private void refreshList() {
        userList.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
        userList.addAll(Controller.gh.getUsersList());
        recyclerView.getAdapter().notifyDataSetChanged();
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
        void onListFragmentInteraction(User item);
    }
    @Override
    public void onClick(View v) {
        startActivityForResult(new Intent(getActivity(), SendInvitationActivity.class),request_code);
        /*
        Intent intent = new Intent(getActivity(), SendInvitationActivity.class);
        startActivity(intent);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == request_code){
            if(resultCode == 0){
                Snackbar.make(swipeContainer, getString(R.string.send_failed), Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }
}
