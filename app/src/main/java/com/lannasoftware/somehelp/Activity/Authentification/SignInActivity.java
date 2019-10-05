package com.lannasoftware.somehelp.Activity.Authentification;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lannasoftware.somehelp.Activity.MainActivity;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    DAOUser cDaoUser;

    Button btn_continuer;

    EditText edit_prenom;
    EditText edit_nom;

    TextView tv_bienvenue;

    Context mContext;

    String sDateNow;
    String sContentEditPrenom;
    String sContentEditNom;

    TextInputLayout til_edit_nom;
    TextInputLayout til_edit_prenom;

    String TAG = "firebase_aparcar";

    FirebaseFirestore dbFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mContext = this;

        btn_continuer = (Button) findViewById(R.id.btn_continuer);
        edit_prenom = (EditText) findViewById(R.id.edit_prenom);
        edit_nom = (EditText) findViewById(R.id.edit_nom);
        tv_bienvenue = (TextView) findViewById(R.id.tv_bienvenue);
        til_edit_nom = (TextInputLayout) findViewById(R.id.til_edit_nom);
        til_edit_prenom = (TextInputLayout) findViewById(R.id.til_edit_prenom);

        HelperApp.SetFont(mContext, btn_continuer,"Roboto-Regular.ttf");
        HelperApp.SetFont(mContext, edit_prenom,"Roboto-Regular.ttf");
        HelperApp.SetFont(mContext, edit_nom,"Roboto-Regular.ttf");
        HelperApp.SetFont(mContext, tv_bienvenue,"Roboto-Light.ttf");

        btn_continuer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_continuer :

                sContentEditNom = edit_nom.getText().toString();
                sContentEditPrenom = edit_prenom.getText().toString();

                if(ControlFormulary())
                    CreateProfile();

                break;
        }
    }

    private Boolean ControlFormulary()
    {
        Boolean bFormularyOK = true;

        String sHint;

        String sEmptyEditPrenom = "Veuillez indiquer votre prénom";
        String sEmptyEditNom = "Veuillez indiquer votre prénom";

        if(HelperApp.IsNullOrEmpty(sContentEditNom))
        {
            sHint = til_edit_nom.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditNom, true, 3000, false, false);
            bFormularyOK = false;
        }
        else if(HelperApp.IsNullOrEmpty(sContentEditPrenom))
        {
            sHint = til_edit_prenom.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }

        return bFormularyOK;
    }

    private void CreateProfile()
    {
        sDateNow = String.valueOf((new Date().getTime()));

        dbFirestore = FirebaseFirestore.getInstance();

        // Create a new user with a first and last name
        Map<String, Object> mUser = new HashMap<>();
        mUser.put("firstname", sContentEditPrenom);
        mUser.put("lastname", sContentEditNom);
        mUser.put("membersince", sDateNow);

        // Add a new document with a generated ID
        dbFirestore.collection("users")
                .add(mUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        cDaoUser = new DAOUser(mContext);
                        cDaoUser.Open();

                        String sStayConnected = "1";
                        User user = new User (sContentEditNom, sContentEditPrenom, sStayConnected, sDateNow, "0", documentReference.getId());

                        cDaoUser.CreateSimpleUser(user);
                        cDaoUser.Close();

                        cDaoUser = new DAOUser(mContext);
                        cDaoUser.Open();
                        long iNbUsersInSQLite = cDaoUser.CountProfiles();
                        cDaoUser.Close();
                        if(iNbUsersInSQLite == 1)
                        {
                            Toast.makeText(mContext, R.string.compte_cree, Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                            Toast.makeText(mContext,  "Erreur", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
}
