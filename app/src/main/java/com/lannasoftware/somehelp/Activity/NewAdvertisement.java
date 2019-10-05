package com.lannasoftware.somehelp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lannasoftware.somehelp.Activity.Authentification.SignInActivity;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.CEnum;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAdvertisement extends AppCompatActivity implements View.OnClickListener {

    String TAG = "firebase_NewAdvertisement";

    Context mContext;

    DAOUser cDaoUser;
    User currentSQLiteUser;
    String sUserIdFirestore;

    FirebaseStorage storage;
    FirebaseFirestore dbFirestore;

    String sAnnonceType;

    TextView txt_next;
    TextView txt_user_name;

    ImageView img_user_image;

    TextInputLayout til_edit_titre_annonce;
    TextInputLayout til_edit_prix;
    TextInputLayout til_edit_categorie;
    TextInputLayout til_edit_lieu;
    TextInputLayout til_edit_description;
    TextInputLayout til_edit_hashtag;

    EditText edit_titre_annonce;
    EditText edit_prix;
    EditText edit_categorie;
    EditText edit_lieu;
    EditText edit_hashtag;
    EditText edit_description;

    Spannable mspanable;

    int hashTagIsComing = 0;

    String sAdervisementTitle;
    String sAdertisementPrice;
    String sAdvertisementCategorie;
    String sAdvertisementLocation;
    String sAdvertisementDescription;
    ArrayList<String> cAdertisementHashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advertisement);

        mContext = this;

        Bundle extras = getIntent().getExtras();
        sAnnonceType = extras.getString("annonceType", CEnum.AnnonceType.None.name());

        til_edit_titre_annonce = findViewById(R.id.til_edit_titre_annonce);
        til_edit_prix = findViewById(R.id.til_edit_prix);
        til_edit_categorie = findViewById(R.id.til_edit_categorie);
        til_edit_lieu = findViewById(R.id.til_edit_lieu);
        til_edit_description = findViewById(R.id.til_edit_description);
        til_edit_hashtag = findViewById(R.id.til_edit_hashtag);

        edit_titre_annonce = findViewById(R.id.edit_titre_annonce);
        edit_prix = findViewById(R.id.edit_prix);
        edit_categorie = findViewById(R.id.edit_categorie);
        edit_lieu= findViewById(R.id.edit_lieu);
        edit_description = findViewById(R.id.edit_description);
        edit_hashtag = findViewById(R.id.edit_hashtag);

        img_user_image = findViewById(R.id.img_user_image);

        txt_user_name = findViewById(R.id.txt_user_name);
        txt_next = findViewById(R.id.txt_next);

        txt_next.setOnClickListener(this);

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentSQLiteUser = cDaoUser.GetUserById(1);
        sUserIdFirestore = currentSQLiteUser.getsIdFirestore();
        cDaoUser.Close();

        edit_hashtag.setText("#");

        if(!sAnnonceType.equals(CEnum.AnnonceType.None.name()))
        {
            if(sAnnonceType.equals(CEnum.AnnonceType.DemanderService.name()))
            {
                //txt_intro.setText("Vous cherchez quelqu'un pour poser vos nouvelles lampes, vous aider à déménager ou encore faire votre ménage ? Indiquez à la communauté ce dont vous avez besoin !");
                //mEdit_contenu_annonce.setHint("Dites-en plus sur ce dont vous avez besoin ..");
            }
            else if(sAnnonceType.equals(CEnum.AnnonceType.ProposerService.name()))
            {
                //txt_intro.setText("Vous savez changer les pneus d'une voiture ou remplir une déclaration d'impôts ? Offrez vos services au tarif que vous souhaitez !");
                //mEdit_contenu_annonce.setHint("Dites-en plus sur ce que vous pouvez faire ..");
            }
            else if(sAnnonceType.equals(CEnum.AnnonceType.LouerArticle.name()))
            {
                //txt_intro.setText("Des vêtements que vous ne mettez plus ou une télévsion à changer.. Vendez ce que vous souhaitez !");
                //mEdit_contenu_annonce.setHint("Dites-en plus à propos de vos articles ..");
            }
        }

        mspanable = edit_hashtag.getText();

        edit_hashtag.addTextChangedListener(new TextWatcher() {

            boolean bSpace = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //if (mEdit_hashtag.length() == 0) {
                //    mEdit_hashtag.setText("#");
                //}

                String startChar = null;

                try{
                    startChar = Character.toString(s.charAt(start));
                    Log.i(getClass().getSimpleName(), "CHARACTER OF NEW WORD: " + startChar);
                }
                catch(Exception ex){
                    startChar = "";
                }

               /* if(startChar.equals(" "))
                    bSpace = true;
                else bSpace = false;*/

                if (startChar.equals("#")) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }

                if(startChar.equals(" ")){
                    hashTagIsComing = 0;
                }

                if(hashTagIsComing != 0) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                /*if (bSpace) {
                    text.append(" #");
                }*/
                if (text.charAt(text.length() -1) == ' ')
                    text.append('#');
            }
        });

        FillInformationsAboutUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.txt_next :

                sAdervisementTitle = edit_titre_annonce.getText().toString();
                sAdertisementPrice = edit_prix.getText().toString();
                sAdvertisementCategorie = edit_categorie.getText().toString();
                sAdvertisementLocation = edit_lieu.getText().toString();
                sAdvertisementDescription = edit_description.getText().toString();

                cAdertisementHashtag = new ArrayList<String>();

                cAdertisementHashtag.add(edit_hashtag.getText().toString());

                if(ControlFormulary())
                    SaveNewAdvertisement();
                break;
        }
    }

    private void FillInformationsAboutUser() {

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentSQLiteUser = cDaoUser.GetUserById(1);
        txt_user_name.setText(currentSQLiteUser.getsFirstname());
        cDaoUser.Close();

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

    private Boolean ControlFormulary()
    {
        boolean bFormularyOK = true;

        String sHint;

        String sEmptyEditPrenom = "Veuillez indiquer votre prénom";
        String sEmptyEditNom = "Veuillez indiquer votre prénom";

        if(HelperApp.IsNullOrEmpty(sAdervisementTitle))
        {
            sHint = til_edit_titre_annonce.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditNom, true, 3000, false, false);
            bFormularyOK = false;
        }

        if(HelperApp.IsNullOrEmpty(sAdertisementPrice))
        {
            sHint = til_edit_prix.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }

        if(HelperApp.IsNullOrEmpty(sAdvertisementCategorie))
        {
            sHint = til_edit_categorie.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }

        if(HelperApp.IsNullOrEmpty(sAdvertisementLocation))
        {
            sHint = til_edit_lieu.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }

        if(cAdertisementHashtag == null || cAdertisementHashtag.size() == 0)
        {
            sHint = til_edit_hashtag.getHint().toString();
            HelperApp.DialogMessage(mContext, sHint + " vide " + ": " + sEmptyEditPrenom, true, 3000, false, false);
            bFormularyOK = false;
        }

        return bFormularyOK;
    }

    private void SaveNewAdvertisement()
    {
        if(sUserIdFirestore != null)
        {
            dbFirestore = FirebaseFirestore.getInstance();

            Long tsLong = System.currentTimeMillis()/1000;
            String sTimestampNow = tsLong.toString();

            String sCollection = "advertisementsall";
            String sDocument = sAnnonceType+"user"+sUserIdFirestore+"date"+sTimestampNow;

            cDaoUser = new DAOUser(mContext);
            cDaoUser.Open();
            currentSQLiteUser = cDaoUser.GetUserById(1);

            String sUserName = currentSQLiteUser.getsFirstname();
            String sUserLocation = currentSQLiteUser.getsVille();

            cDaoUser.Close();

            Map<String, Object> newAdvertisement = new HashMap<>();
            newAdvertisement.put("type", sAnnonceType);
            newAdvertisement.put("user_id_firestore", sUserIdFirestore);
            newAdvertisement.put("advertisement_date", sTimestampNow);
            newAdvertisement.put("user_name", sUserName);
            newAdvertisement.put("user_location", sUserLocation);
            newAdvertisement.put("advertisement_title", sAdervisementTitle);
            newAdvertisement.put("advertisement_price", sAdertisementPrice);
            newAdvertisement.put("advertisement_categorie", sAdvertisementCategorie);
            newAdvertisement.put("advertisement_location", sAdvertisementLocation);
            newAdvertisement.put("advertisement_description", sAdvertisementDescription);
            newAdvertisement.put("advertisement_hashtag", cAdertisementHashtag);

            dbFirestore
                    .collection(sCollection)
                    .document(sDocument)
                    .set(newAdvertisement)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(mContext, R.string.modification_effectuee, Toast.LENGTH_LONG).show();

                    finish();

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(mContext, "Error writing document : " + e, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void changeTheColor(String s, int start, int end) {
        mspanable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
