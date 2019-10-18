package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.lannasoftware.somehelp.Activity.NewAdvertisement;
import com.lannasoftware.somehelp.Helper.CEnum;
import com.lannasoftware.somehelp.R;

public class FragmentHome2 extends Fragment implements View.OnClickListener{

    Context mContext;

    FloatingActionButton cFloatingButtonItem1;
    FloatingActionButton cFloatingButtonItem2;
    FloatingActionButton cFloatingButtonItem3;

    public FragmentHome2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            /*
            s_likes = bundle.getString("s_likes");
            s_visites = bundle.getString("s_visites");
            */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_2, container, false);

        cFloatingButtonItem1 = v.findViewById(R.id.floatingActionButton_item1);
        cFloatingButtonItem2 = v.findViewById(R.id.floatingActionButton_item2);
        cFloatingButtonItem3 = v.findViewById(R.id.floatingActionButton_item3);

        cFloatingButtonItem1.setOnClickListener(this);
        cFloatingButtonItem2.setOnClickListener(this);
        cFloatingButtonItem3.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.floatingActionButton_item1 :
                Intent CreateAdvertisement1 = new Intent(mContext, NewAdvertisement.class);
                CreateAdvertisement1.putExtra("annonceType", CEnum.AnnonceType.DemanderService.name());
                CreateAdvertisement1.putExtra("annonceName", cFloatingButtonItem1.getLabelText());
                startActivity(CreateAdvertisement1);
                break;
            case R.id.floatingActionButton_item2 :
                Intent CreateAdvertisement2 = new Intent(mContext, NewAdvertisement.class);
                CreateAdvertisement2.putExtra("annonceType", CEnum.AnnonceType.ProposerService.name());
                CreateAdvertisement2.putExtra("annonceName", cFloatingButtonItem2.getLabelText());
                startActivity(CreateAdvertisement2);
                break;

            case R.id.floatingActionButton_item3 :
                Intent CreateAdvertisement3 = new Intent(mContext, NewAdvertisement.class);
                CreateAdvertisement3.putExtra("annonceType", CEnum.AnnonceType.LouerArticle.name());
                CreateAdvertisement3.putExtra("annonceName", cFloatingButtonItem3.getLabelText());
                startActivity(CreateAdvertisement3);

                break;

        }
    }
}
