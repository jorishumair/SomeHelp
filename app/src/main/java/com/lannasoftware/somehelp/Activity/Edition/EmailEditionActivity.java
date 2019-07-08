package com.lannasoftware.somehelp.Activity.Edition;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
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

import static com.lannasoftware.somehelp.Helper.HelperApp.IsValidEmail;

public class EmailEditionActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "firebase_aparcar";

    ImageView img_validation;
    TextInputLayout til_edit_email;
    TextView txt_header;
    EditText edit_email;

    Context mContext;

    FirebaseFirestore dbFirestore;
    DAOUser cDaoUser;
    User currentUserSQLite;
    String sUserSQLiteIdFirestore;

    String sContentEmail;

    String sInitialEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_edition);

        mContext = this;

        img_validation = findViewById(R.id.img_validation);
        txt_header = findViewById(R.id.txt_header);
        til_edit_email = findViewById(R.id.til_edit_email);
        edit_email = findViewById(R.id.edit_email);

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

                sContentEmail = edit_email.getText().toString();

                if(IsValidEmail(sContentEmail))
                    SaveInformationsAboutUser();
                break;
        }
    }

    private void FillInformationsAboutUser()
    {
        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();

        currentUserSQLite = cDaoUser.GetUserById(1);

        sInitialEmail = currentUserSQLite.getsEmail();

        sContentEmail = currentUserSQLite.getsEmail();

        edit_email.setText(sContentEmail);

        cDaoUser.Close();
    }

    private void SaveInformationsAboutUser()
    {
        if(sInitialEmail == null || !sInitialEmail.equals(sContentEmail))
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
                updates.put("about", sContentEmail);

                docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        cDaoUser.UpdateEmail(currentUserSQLite, sContentEmail);

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
