package com.lannasoftware.somehelp.Activity.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lannasoftware.somehelp.Activity.ProfilActivity;
import com.lannasoftware.somehelp.Activity.SplashScreenGoodbye;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

public class FragmentHome5 extends Fragment implements View.OnClickListener{

    Context mContext;

    DAOUser cDaoUser;
    User currentUserSQLite;

    FirebaseStorage storage;

    TextView txt_user_name;
    TextView txt_change_mode;

    ImageView img_user_image;

    RelativeLayout rl_profil_header;
    RelativeLayout rl_logout;
    RelativeLayout rl_change_mode;

    String sHostMode = "0";

    public FragmentHome5() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home_5, container, false);

        txt_user_name = v.findViewById(R.id.txt_user_name);
        img_user_image = v.findViewById(R.id.img_user_image);
        rl_profil_header = v.findViewById(R.id.rl_profil_header);
        rl_logout = v.findViewById(R.id.rl_logout);
        rl_change_mode = v.findViewById(R.id.rl_change_mode);
        txt_change_mode = v.findViewById(R.id.txt_change_mode);

        rl_profil_header.setOnClickListener(this);
        rl_logout.setOnClickListener(this);
        rl_change_mode.setOnClickListener(this);

        HelperApp.SetFont(mContext, txt_user_name,"Roboto-Black.ttf");

        FillInformationsAboutUser();

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

            case R.id.rl_change_mode :
                ChangeMode();
                break;
        }
    }

    private void FillInformationsAboutUser() {

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentUserSQLite = cDaoUser.GetUserById(1);
        txt_user_name.setText(currentUserSQLite.getsFirstname());
        cDaoUser.Close();

        sHostMode = currentUserSQLite.getsModeHost();

        if(sHostMode.equals("0"))
            txt_change_mode.setText("Passer en mode hôte");
        else if(sHostMode.equals("1"))
            txt_change_mode.setText("Passer en mode voyageur");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String sUserUid = currentFirebaseUser.getUid();
        String sUserProfilImageLink = null;

        if(sUserUid != null && !sUserUid.isEmpty())
            sUserProfilImageLink = "profils/"+sUserUid+".jpg";

        storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference().child(sUserProfilImageLink);//.child("images/").child(user.getUid());
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful())
                {
                    Glide.with(mContext)
                            .load(task.getResult())
                            .apply(RequestOptions.circleCropTransform())
                            .into(img_user_image);
                }
                else {
                    Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Firebase id", "user.getUid()");
                }
            }
        });
    }

    private void AccessProfil()
    {
        Intent IAccessProfil = new Intent(mContext, ProfilActivity.class);
        startActivity(IAccessProfil);
    }

    private void ChangeMode()
    {
        if(sHostMode.equals("0"))
            sHostMode = "1";
        else if(sHostMode.equals("1"))
            sHostMode = "0";

        if(sHostMode.equals("0"))
            txt_change_mode.setText("Passer en mode hôte");
        else if(sHostMode.equals("1"))
            txt_change_mode.setText("Passer en mode voyageur");
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
