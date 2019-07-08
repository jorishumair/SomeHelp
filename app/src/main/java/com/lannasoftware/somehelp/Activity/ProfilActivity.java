package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lannasoftware.somehelp.Activity.Edition.ProfilEditionActivity;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.Calendar;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    DAOUser cDaoUser;
    User currentUserSQLite;

    FirebaseStorage storage;

    TextView txt_firstname_lastname;
    TextView txt_member;
    TextView txt_a_propos_title;
    TextView txt_vit_a;
    TextView txt_parle;
    TextView txt_a_fournis;
    TextView txt_parking_propose;
    TextView txt_commentaires;
    TextView txt_header;

    ImageView img_user_image;
    ImageView img_back;
    ImageView img_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mContext = this;

        txt_firstname_lastname = findViewById(R.id.txt_firstname_lastname);
        txt_member = findViewById(R.id.txt_member);
        txt_a_propos_title = findViewById(R.id.txt_a_propos_title);
        txt_vit_a = findViewById(R.id.txt_vit_a);
        txt_a_fournis = findViewById(R.id.txt_a_fournis);
        txt_parle = findViewById(R.id.txt_parle);
        txt_parking_propose = findViewById(R.id.txt_parking_propose);
        txt_commentaires = findViewById(R.id.txt_commentaires);
        img_user_image = findViewById(R.id.img_user_image);
        img_back = findViewById(R.id.img_back);
        img_edit = findViewById(R.id.img_edit);
        txt_header = findViewById(R.id.txt_header);

        HelperApp.SetFont(mContext, txt_header,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_firstname_lastname,"Roboto-Bold.ttf");
        HelperApp.SetFont(mContext, txt_a_propos_title,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_a_fournis,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_parking_propose,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_commentaires,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_member,"Roboto-Light.ttf");
        HelperApp.SetFont(mContext, txt_vit_a,"Roboto-Light.ttf");
        HelperApp.SetFont(mContext, txt_parle,"Roboto-Light.ttf");

        img_back.setOnClickListener(this);
        img_edit.setOnClickListener(this);

        FillInformationsAboutUser();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.img_edit :
                AccessProfilEdition();
                break;

            case R.id.img_back:
                finish();
                break;
        }
    }

    private void AccessProfilEdition()
    {
        Intent IAccessProfilEdition = new Intent(mContext, ProfilEditionActivity.class);
        startActivity(IAccessProfilEdition);
    }

    private void FillInformationsAboutUser()
    {
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

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentUserSQLite = cDaoUser.GetUserById(1);

        long timestamp = Long.parseLong(currentUserSQLite.getsMemberSince());
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp);
        String sYear = String.valueOf(cal.get(Calendar.YEAR));
        int iMonth = cal.get(Calendar.MONTH);

        String sName = currentUserSQLite.getsFirstname() + " " + currentUserSQLite.getsLastname();
        String sMemberSince = getResources().getString(R.string.membre_depuis) + " " + HelperApp.GetMonthForInt(iMonth) + ", " + sYear;

        txt_firstname_lastname.setText(sName);
        txt_member.setText(sMemberSince);

        cDaoUser.Close();
    }
}
