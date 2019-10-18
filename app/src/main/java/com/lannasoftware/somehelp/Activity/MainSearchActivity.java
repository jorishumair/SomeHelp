package com.lannasoftware.somehelp.Activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lannasoftware.somehelp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainSearchActivity extends ListActivity {

    private FirebaseFirestore db;
    private Context mContext;
    private ProgressDialog pDialog;

    ArrayList<HashMap<String, String>> cElementsCategories;

    String sCollectionId = "categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_search);

        mContext = this;

        cElementsCategories = new ArrayList<HashMap<String, String>>();

        db = FirebaseFirestore.getInstance();

        ListView lv = getListView();

        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                // on selecting a single album
                // TrackListActivity will be launched to show tracks inside the album
                Intent i = new Intent(getApplicationContext(), SearchLevel1Activity.class);

                // send album id to tracklist activity to get list of songs under that album
                String sDocumentId = ((TextView) view.findViewById(R.id.album_id)).getText().toString();
                i.putExtra("documentId", sDocumentId);

                startActivity(i);
            }
        });

        db.collection(sCollectionId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

                            cElementsCategories.add(map);
                        }
                    }

                    ListAdapter adapter = new SimpleAdapter(
                            mContext, cElementsCategories,
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
