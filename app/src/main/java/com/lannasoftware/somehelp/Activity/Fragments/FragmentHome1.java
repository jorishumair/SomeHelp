package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.lannasoftware.somehelp.Activity.MainSearchActivity;
import com.lannasoftware.somehelp.Adapter.AdvertisementViewHolder;
import com.lannasoftware.somehelp.Entity.Advertisement;
import com.lannasoftware.somehelp.R;

public class FragmentHome1 extends Fragment implements View.OnClickListener{

    private Context mContext;

    private RelativeLayout rl_search;

    FirestorePagingAdapter<Advertisement, AdvertisementViewHolder> mAdapter;
    PagedList.Config config;
    FirestorePagingOptions<Advertisement> options;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private FirebaseFirestore mFirestore;
    private CollectionReference mAdvertisementsCollection;
    private Query mQuery;

    public FragmentHome1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        mFirestore = FirebaseFirestore.getInstance();
        mAdvertisementsCollection = mFirestore.collection("advertisementsall");
        mQuery = mAdvertisementsCollection.orderBy("advertisement_date", Query.Direction.DESCENDING);

        // This configuration comes from the Paging Support Library
        // https://developer.android.com/reference/android/arch/paging/PagedList.Config.html
         config = new PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build();

        // The options for the adapter combine the paging configuration with query information
        // and application-specific options for lifecycle, etc.
        options = new FirestorePagingOptions.Builder<Advertisement>()
                .setLifecycleOwner(this)
                .setQuery(mQuery, config, Advertisement.class)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_1, container, false);

        rl_search = v.findViewById(R.id.rl_search);
        mRecyclerView = v.findViewById(R.id.recycler_liste);
        mSwipeRefreshLayout = v.findViewById(R.id.swipeContainer);

        rl_search.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        getAdvertisements();

        // Refresh Action on Swipe Refresh Layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //mAdapter.refresh();
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

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

    private void getAdvertisements()
    {
        mAdapter =
                new FirestorePagingAdapter<Advertisement, AdvertisementViewHolder>(options) {
                    @NonNull
                    @Override
                    public AdvertisementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_advertisement_in_list, parent, false);
                        return new AdvertisementViewHolder(view, mContext);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull AdvertisementViewHolder holder,
                                                    int position,
                                                    @NonNull Advertisement model) {

                        if(model.getAdvertisement_images() != null && model.getAdvertisement_images().size() > 0)
                            holder.setAdvertisementImages(model.getAdvertisement_images());

                        holder.setAdvertisementTitle(model.getAdvertisement_title());
                        holder.setAdvertisementPrice(model.getAdvertisement_price());
                        holder.setAdvertisementDescription(model.getAdvertisement_description());
                        holder.setAdvertisementLocation(model.getAdvertisement_location());
                        holder.setAdvertisementUserName(model.getUser_name());
                    }

                    @Override
                    protected void onLoadingStateChanged(@NonNull LoadingState state) {
                        switch (state) {
                            case LOADING_INITIAL:
                            case LOADING_MORE:
                                mSwipeRefreshLayout.setRefreshing(true);
                                break;

                            case LOADED:
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case ERROR:
                                Toast.makeText(
                                        getActivity(),
                                        "Error Occurred!",
                                        Toast.LENGTH_SHORT
                                ).show();

                                mSwipeRefreshLayout.setRefreshing(false);
                                break;

                            case FINISHED:
                                mSwipeRefreshLayout.setRefreshing(false);
                                break;
                        }
                    }
                };
        mRecyclerView.setAdapter(mAdapter);
    }

    private void LoadMorePublications()
    {
        /*
        loadmore = true;

        if(lastKeyEvaluatedVille == null && lastKeyEvaluatedRegion ==  null && lastKeyEvaluatedPays == null){
            mAdapter.setMoreDataAvailable(false);
        }else{

            LoadListeVilleTsk tskVille = new LoadListeVilleTsk();
            tskVille.execute();
        }
        */
    }

    /*
    private void GetPublications()
    {
        dbFirestore = FirebaseFirestore.getInstance();

        mQuery = dbFirestore.collection("advertisementsall")
                .orderBy("advertisement_date", Query.Direction.DESCENDING)
                .limit(50);






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

        swipeContainer.setRefreshing(false);

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
    }

    */
}
