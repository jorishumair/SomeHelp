package com.lannasoftware.somehelp.Activity.Edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.HashMap;
import java.util.Map;

public class NameEditionActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "firebase_aparcar";

    ImageView img_validation;
    TextInputLayout til_edit_firstname;
    TextInputLayout til_edit_lastname;
    TextView txt_header;
    EditText edit_firstname;
    EditText edit_lastname;

    Context mContext;

    FirebaseFirestore dbFirestore;
    DAOUser cDaoUser;
    User currentUserSQLite;
    String sUserSQLiteIdFirestore;

    String sContentFirstname;
    String sContentLastname;

    String sInitialFirstname;
    String sInitialLastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_edition);

        mContext = this;

        img_validation = findViewById(R.id.img_validation);
        txt_header = findViewById(R.id.txt_header);
        til_edit_firstname = findViewById(R.id.til_edit_firstname);
        til_edit_lastname = findViewById(R.id.til_edit_lastname);
        edit_firstname = findViewById(R.id.edit_firstname);
        edit_lastname = findViewById(R.id.edit_lastname);

        HelperApp.SetFont(mContext, txt_header,"Roboto-Medium.ttf");

        img_validation.setOnClickListener(this);

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentUserSQLite = cDaoUser.GetUserById(1);
        sUserSQLiteIdFirestore = currentUserSQLite.getsIdFirestore();
        cDaoUser.Close();

        FillInformationsAboutUser();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.img_validation :

                sContentFirstname = edit_firstname.getText().toString();
                sContentLastname = edit_lastname.getText().toString();

                if(CheckFormulary())
                {
                    SaveInformationsAboutUser();
                }
                break;
        }
    }

    private Boolean CheckFormulary()
    {
        Boolean bFormularyOK = true;

        String sHint;

        String sEmptyEditPrenom = getResources().getString(R.string.veuillez_indiquer_firstname);
        String sEmptyEditNom = getResources().getString(R.string.veuillez_indiquer_lastname);

        if(HelperApp.IsNullOrEmpty(sContentFirstname))
        {
            sHint = til_edit_lastname.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }
        else if(HelperApp.IsNullOrEmpty(sContentLastname))
        {
            sHint = til_edit_firstname.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditNom, true, 3000, false, false);
            bFormularyOK = false;
        }

        return bFormularyOK;
    }

    private void FillInformationsAboutUser()
    {
        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();

        currentUserSQLite = cDaoUser.GetUserById(1);

        sInitialFirstname = currentUserSQLite.getsFirstname();
        sInitialLastname = currentUserSQLite.getsLastname();

        sContentFirstname = currentUserSQLite.getsFirstname();
        sContentLastname = currentUserSQLite.getsLastname();

        edit_firstname.setText(sContentFirstname);
        edit_lastname.setText(sContentLastname);

        cDaoUser.Close();
    }

    private void SaveInformationsAboutUser()
    {
        if(!sInitialFirstname.equals(sContentFirstname) || !sInitialLastname.equals(sContentLastname))
        {
            cDaoUser = new DAOUser(mContext);
            cDaoUser.Open();

            if(sUserSQLiteIdFirestore != null)
            {
                dbFirestore = FirebaseFirestore.getInstance();

                // Update an existing document
                DocumentReference docRef = dbFirestore.collection("users").document(sUserSQLiteIdFirestore);

                // Update age and favorite color
                Map<String, Object> updates = new HashMap<>();
                updates.put("firstname", sContentFirstname);
                updates.put("lastname", sContentLastname);

                docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        cDaoUser.UpdateFirstName(currentUserSQLite, sContentFirstname);
                        cDaoUser.UpdateLastName(currentUserSQLite, sContentLastname);

                        cDaoUser.Close();

                        Toast.makeText(mContext, R.string.modification_effectuee, Toast.LENGTH_LONG).show();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("modification", "true");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                });
            }
        }
        else
            finish();
    }
}
