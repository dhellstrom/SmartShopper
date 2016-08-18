package com.summer.daniel.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 2016-08-10.
 */
public abstract class AbstractListFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private FloatingActionButton mNewListFab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_main_list, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.main_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mNewListFab = (FloatingActionButton) v.findViewById(R.id.fab_new_list);
        mNewListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = newObjectIntent();
                startActivity(intent);
            }
        });

        updateUI(mRecyclerView);
        return v;
    }

    @Override
    public void onResume(){
        super.onResume();

        updateUI(mRecyclerView);
    }

    protected abstract void updateUI(RecyclerView recyclerView);

    protected abstract Intent newObjectIntent();

}
