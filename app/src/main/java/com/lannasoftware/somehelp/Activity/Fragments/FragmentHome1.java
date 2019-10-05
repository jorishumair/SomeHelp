package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lannasoftware.somehelp.Activity.MainSearchActivity;
import com.lannasoftware.somehelp.Adapter.RecycledAdapterFragment1;
import com.lannasoftware.somehelp.Entity.Advertisement;
import com.lannasoftware.somehelp.Helper.OnLoadMoreListener;
import com.lannasoftware.somehelp.R;

import java.util.ArrayList;

public class FragmentHome1 extends Fragment implements View.OnClickListener{

    Context mContext;

    FirebaseFirestore dbFirestore;
    Query mQuery;
    String TAG = "firebase_FragmentHome1";

    RelativeLayout rl_search;

    RecyclerView recycler_liste;
    private SwipeRefreshLayout swipeContainer;
    RecycledAdapterFragment1 adapter;
    ArrayList<Object> dataList;

    public FragmentHome1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        dbFirestore = FirebaseFirestore.getInstance();

        dataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_1, container, false);

        rl_search = v.findViewById(R.id.rl_search);
        recycler_liste = v.findViewById(R.id.recycler_liste);
        swipeContainer = v.findViewById(R.id.swipeContainer);

        rl_search.setOnClickListener(this);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshPublications();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recycler_liste.setHasFixedSize(true);

        adapter = new RecycledAdapterFragment1(mContext, dataList, recycler_liste);

        adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recycler_liste.post(new Runnable() {
                    @Override
                    public void run() {
                        LoadMorePublications();
                    }
                });
            }
        });

        recycler_liste.addOnItemTouchListener(new RecycledAdapterFragment1.RecyclerTouchListener(mContext, recycler_liste, new RecycledAdapterFragment1.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if(position == dataList.size()+1){
                    //Intent pickIntent = new Intent(getActivity(), SettingActivity.class);
                    //startActivityForResult(pickIntent, SETTINGS_REQUEST);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recycler_liste.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        recycler_liste.setAdapter(adapter);

        GetPublications();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_search :
                AccessMainSearchActivity();
                break;
        }
    }

    private void AccessMainSearchActivity()
    {
        Intent IAccessMainSearchActivity = new Intent(mContext, MainSearchActivity.class);
        startActivity(IAccessMainSearchActivity);
    }

    private void RefreshPublications()
    {
        if(swipeContainer.isRefreshing()){
            swipeContainer.setRefreshing(false);
        }
    }

    private void LoadMorePublications()
    {
        /*
        loadmore = true;

        if(lastKeyEvaluatedVille == null && lastKeyEvaluatedRegion ==  null && lastKeyEvaluatedPays == null){
            adapter.setMoreDataAvailable(false);
        }else{

            LoadListeVilleTsk tskVille = new LoadListeVilleTsk();
            tskVille.execute();
        }
        */
    }

    private void GetPublications()
    {
        dbFirestore = FirebaseFirestore.getInstance();

        mQuery = dbFirestore.collection("advertisementsall")
                .orderBy("advertisement_date", Query.Direction.DESCENDING)
                .limit(50);
/*
        FirestoreRecyclerOptions<Advertisement> response = new FirestoreRecyclerOptions.Builder<Advertisement>()
                .setQuery(mQuery, FriendsResponse.class)
                .build();
*/
/*
        dbFirestore.collection("advertisementsall")
                .orderBy("advertisement_date", Query.Direction.DESCENDING)
                .limit(50)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String sAdvertisementIdFirestore = "";
                                String sAdvertisementCategorie = "";
                                String sAdvertisementDate = "";
                                String sAdvertisementDescription = "";
                                String sAdvertisementLocation = "";
                                String sAdvertisementPrice = "";
                                String sAdvertisementTitle = "";
                                String sAdvertisementType = "";
                                String sAdvertisementUserIdFirestore = "";
                                String sAdvertisementUserLocation = "";
                                String sAdvertisementUserName = "";

                                sAdvertisementIdFirestore = document.getId();

                                if(document.get("advertisement_categorie") != null)
                                    sAdvertisementCategorie = document.get("advertisement_categorie").toString();

                                if(document.get("advertisement_date") != null)
                                    sAdvertisementDate = document.get("advertisement_date").toString();

                                if(document.get("advertisement_description") != null)
                                    sAdvertisementDescription = document.get("advertisement_description").toString();

                                if(document.get("advertisement_location") != null)
                                    sAdvertisementLocation = document.get("advertisement_location").toString();

                                if(document.get("advertisement_price") != null)
                                    sAdvertisementPrice = document.get("advertisement_price").toString();

                                if(document.get("advertisement_title") != null)
                                    sAdvertisementTitle = document.get("advertisement_title").toString();

                                if(document.get("type") != null)
                                    sAdvertisementType = document.get("type").toString();

                                if(document.get("user_id_firestore") != null)
                                    sAdvertisementUserIdFirestore = document.get("user_id_firestore").toString();

                                if(document.get("user_location") != null)
                                    sAdvertisementUserLocation = document.get("user_location").toString();

                                if(document.get("user_name") != null)
                                    sAdvertisementUserName = document.get("user_name").toString();


                                dataList.add(new Advertisement(sAdvertisementIdFirestore, sAdvertisementCategorie,
                                        sAdvertisementDate, sAdvertisementDescription, sAdvertisementLocation, sAdvertisementPrice,
                                        sAdvertisementTitle, sAdvertisementType, sAdvertisementUserIdFirestore,
                                        sAdvertisementUserLocation, sAdvertisementUserName));
                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
*/
        swipeContainer.setRefreshing(false);

        adapter.notifyDataSetChanged();
        adapter.setLoaded();
    }
}
