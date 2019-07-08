package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lannasoftware.somehelp.Activity.ProfilActivity;
import com.lannasoftware.somehelp.Activity.SplashScreenGoodbye;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

public class FragmentHome4 extends Fragment implements View.OnClickListener{

    Context mContext;

    DAOUser cDaoUser;
    User currentUserSQLite;

    TextView txt_user_name;

    ImageView img_user_image;

    RelativeLayout rl_profil_header;
    RelativeLayout rl_logout;

    public FragmentHome4() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_4, container, false);

        txt_user_name = v.findViewById(R.id.txt_user_name);
        img_user_image = v.findViewById(R.id.img_user_image);
        rl_profil_header = v.findViewById(R.id.rl_profil_header);
        rl_logout = v.findViewById(R.id.rl_logout);

        rl_profil_header.setOnClickListener(this);
        rl_logout.setOnClickListener(this);

        HelperApp.SetFont(mContext, txt_user_name,"Roboto-Black.ttf");

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentUserSQLite = cDaoUser.GetUserById(1);
        txt_user_name.setText(currentUserSQLite.getsFirstname());
        cDaoUser.Close();

        Glide.with(mContext).load(R.drawable.ic_user_gray)
                .thumbnail(0.5f)
                .into(img_user_image);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_profil_header :
                AccessProfil();
                break;

            case R.id.rl_logout :
                SignOut();
                break;
        }
    }

    private void AccessProfil()
    {
        Intent IAccessProfil = new Intent(mContext, ProfilActivity.class);
        startActivity(IAccessProfil);
    }

    private void SignOut()
    {
        cDaoUser.Open();
        cDaoUser.UpdateStayConnected(currentUserSQLite, "0");
        cDaoUser.Close();

        AuthUI.getInstance()
                .signOut(mContext)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {

                        Intent IClose = new Intent(mContext, SplashScreenGoodbye.class);
                        IClose.putExtra("user_name", currentUserSQLite.getsFirstname());
                        startActivity(IClose);
                        getActivity().finish();
                    }
                });
    }
}
