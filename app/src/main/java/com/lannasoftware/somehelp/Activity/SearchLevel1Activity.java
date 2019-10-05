package com.lannasoftware.somehelp.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lannasoftware.somehelp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchLevel1Activity extends ListActivity {

    private FirebaseFirestore db;
    private Context mContext;
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> cElementsCategoriesLevel1;

    String sCollectionId = "categories";
    String sCollectionLevel1Id = "categorielevel1";
    String sDocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_level1);

        Intent i = getIntent();
        sDocumentId = i.getStringExtra("documentId");

        if(sDocumentId == null)
            finish();

        mContext = this;

        cElementsCategoriesLevel1 = new ArrayList<HashMap<String, String>>();

        db = FirebaseFirestore.getInstance();

        ListView lv = getListView();

        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                //Intent i = new Intent(getApplicationContext(), SearchLevel1Activity.class);

                // send album id to tracklist activity to get list of songs under that album
                //String album_id = ((TextView) view.findViewById(R.id.album_id)).getText().toString();
                //i.putExtra("album_id", album_id);

                //startActivity(i);
            }
        });

        db.collection(sCollectionId).document(sDocumentId).collection(sCollectionLevel1Id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        String sId = document.getId();
                        String sName = null;

                        if(document.get("nameFR") != null && document.get("nameFR").getClass().equals(String.class))
                            sName = (String) document.get("nameFR");

                        if(sName != null)
                        {
                            HashMap<String, String> map = new HashMap<String, String>();

                            map.put("id", sId);
                            map.put("name", sName);

                            cElementsCategoriesLevel1.add(map);
                        }
                    }

                    ListAdapter adapter = new SimpleAdapter(
                            mContext, cElementsCategoriesLevel1,
                            R.layout.list_items_categories, new String[] { "id",
                            "name" }, new int[] {
                            R.id.album_id, R.id.album_name});

                    // updating listview
                    setListAdapter(adapter);
                } else {
                    Toast.makeText(mContext, "Error getting categories: " + task.getException(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}