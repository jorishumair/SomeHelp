package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lannasoftware.somehelp.R;

public class FragmentHome2 extends Fragment implements View.OnClickListener{

    Context mContext;

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

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
