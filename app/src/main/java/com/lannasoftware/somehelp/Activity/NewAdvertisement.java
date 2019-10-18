package com.lannasoftware.somehelp.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lannasoftware.somehelp.Adapter.RecyclerAdapterLoadImagesAdvertisement;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.CEnum;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NewAdvertisement extends AppCompatActivity implements View.OnClickListener {

    String TAG = "firebase_NewAdvertisement";

    Context mContext;

    DAOUser cDaoUser;
    User currentSQLiteUser;
    String sUserIdFirestore;

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

    RecyclerAdapterLoadImagesAdvertisement adapter;
    RecyclerView recycler_images;
    ArrayList<Uri> arrayListPathImages;
    ArrayList<String> arrayNameImages;
    int iNumImage = 0;

    Spannable mspanable;

    int hashTagIsComing = 0;

    String sAdervisementTitle;
    String sAdertisementPrice;
    String sAdvertisementCategorie;
    String sAdvertisementLocation;
    String sAdvertisementDescription;
    ArrayList<String> cAdertisementHashtag;

    boolean bUseCamera = false;
    int iPermissionCheckWrite;
    int iPermissionCheckCamera;
    private static final int MY_PERMISSIONS_REQUEST_Write = 0;
    private static final int MY_PERMISSIONS_REQUEST_Camera = 1;
    static final int REQUEST_GALLERY_IMG = 0;
    static final int REQUEST_TAKE_PHOTO = 1;
    String sImageFileName;
    String sCurrentPhotoPath;
    Uri uUri;
    StorageReference mStorageRef;
    FirebaseStorage mStorage;

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

        recycler_images = findViewById(R.id.recycler_images);

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
                    ChangeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }

                if(startChar.equals(" ")){
                    hashTagIsComing = 0;
                }

                if(hashTagIsComing != 0) {
                    ChangeTheColor(s.toString().substring(start), start, start + count);
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

        arrayListPathImages = new ArrayList<>();
        arrayNameImages = new ArrayList<>();

        adapter = new RecyclerAdapterLoadImagesAdvertisement(mContext, arrayListPathImages, recycler_images);

        recycler_images.setHasFixedSize(false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_images.setLayoutManager(layoutManager);

        recycler_images.addOnItemTouchListener(new RecyclerAdapterLoadImagesAdvertisement.RecyclerTouchListener(mContext, recycler_images, new RecyclerAdapterLoadImagesAdvertisement.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                if (position == arrayListPathImages.size()) {
                    OpenDialogCamera();
                } else {

                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        recycler_images.setAdapter(adapter);

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

                cAdertisementHashtag = new ArrayList<>();

                cAdertisementHashtag.add(edit_hashtag.getText().toString());

                if(ControlFormulary())
                    CheckImagesBeforeSaveNewAdvertisement();
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

        mStorage = FirebaseStorage.getInstance();

        StorageReference storageReference = mStorage.getReference().child(sUserProfilImageLink);//.child("images/").child(user.getUid());
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

    private void CheckImagesBeforeSaveNewAdvertisement()
    {
        if(sUserIdFirestore != null)
        {
            Long tsLong = System.currentTimeMillis()/1000;
            String sTimestampNow = tsLong.toString();

            SaveNewAdvertisement(sTimestampNow);
        }
    }

    private void SaveNewAdvertisement(String sTimestampNow)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading advertisement ..");
        progressDialog.show();

        dbFirestore = FirebaseFirestore.getInstance();

        String sCollection = "advertisementsall";
        String sDocument = sAnnonceType+"user"+sUserIdFirestore+"date"+sTimestampNow;

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentSQLiteUser = cDaoUser.GetUserById(1);

        String sUserName = currentSQLiteUser.getsFirstname();
        String sUserLocation = currentSQLiteUser.getsVille();

        cDaoUser.Close();

        Map<String, Object> newAdvertisement = new HashMap<>();
        newAdvertisement.put("advertisement_type", sAnnonceType);
        newAdvertisement.put("user_id_firestore", sUserIdFirestore);
        newAdvertisement.put("advertisement_date", sTimestampNow);
        newAdvertisement.put("advertisement_images", arrayNameImages);
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

                        if(arrayNameImages.size() == 0)
                        {
                            progressDialog.dismiss();
                            finish();
                        }
                        else
                            UploadPictureToFirebase(arrayListPathImages);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Error writing document : " + e, Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            uUri = Uri.parse(sCurrentPhotoPath);

            arrayListPathImages.add(uUri);

            String sNameImage = sImageFileName + UUID.randomUUID().toString()+".jpg";
            arrayNameImages.add(sNameImage);

            adapter.notifyDataSetChanged();

        } else if (requestCode == REQUEST_GALLERY_IMG && resultCode == RESULT_OK) {

            uUri = data.getData();

            Cursor returnCursor = mContext.getContentResolver().query(uUri, null, null, null, null);

            assert returnCursor != null;
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = returnCursor.getString(nameIndex);
            returnCursor.close();

            sImageFileName = name;

            arrayListPathImages.add(uUri);

            String sNameImage = sImageFileName + UUID.randomUUID().toString()+".jpg";
            arrayNameImages.add(sNameImage);

            adapter.notifyDataSetChanged();
        }
    }

    void OpenDialogCamera(){

        final Dialog dialog1 = new Dialog(mContext);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setContentView(R.layout.dialog_choice_photo);

        RelativeLayout ll_take_photo = dialog1.findViewById(R.id.ll_take_photo);
        RelativeLayout ll_load_photo = dialog1.findViewById(R.id.ll_load_photo);

        ll_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bUseCamera = true;

                iPermissionCheckWrite = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                iPermissionCheckCamera = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);

                if(iPermissionCheckWrite == PackageManager.PERMISSION_GRANTED && iPermissionCheckCamera == PackageManager.PERMISSION_GRANTED)
                    DispatchTakePictureIntent();
                else
                    ActivityCompat.requestPermissions(NewAdvertisement.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, MY_PERMISSIONS_REQUEST_Write);

                dialog1.dismiss();
            }
        });

        ll_load_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bUseCamera = false;

                iPermissionCheckWrite = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if(iPermissionCheckWrite == PackageManager.PERMISSION_GRANTED){
                    Intent intent = new Intent();
                    if (Build.VERSION.SDK_INT >= 19) {
                        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    } else {
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                    }

                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_GALLERY_IMG);
                }else{
                    ActivityCompat.requestPermissions(NewAdvertisement.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_Write);
                }


                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    private void DispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = CreateImageFile();
            } catch (IOException ex) {
            }

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        mContext.getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File CreateImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        sImageFileName = "advertisement"+timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                sImageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        sCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void UploadPictureToFirebase(final ArrayList<Uri> arrayListPathImages)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploadin images ...");
        progressDialog.show();

        iNumImage = 0;

        for (Uri mUri : arrayListPathImages) {

            String sPath = null;

            try {
                sPath = HelperApp.GetPath(mContext, mUri);

            } catch (URISyntaxException e) {
                Toast.makeText(mContext,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
            }

            if(sPath != null)
            {
                Bitmap scaledBitmap = HelperApp.CompressImage(sPath);

                //create a file to write bitmap data
                File f = new File(mContext.getCacheDir(), sImageFileName);
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

                byte[] data = bos.toByteArray();

                String sNameImage = "advertisements/"+arrayNameImages.get(iNumImage);

                mStorage = FirebaseStorage.getInstance();

                mStorageRef = mStorage.getReference();

                StorageReference sStorageNameImage = mStorageRef.child(sNameImage);

                UploadTask uploadTask = sStorageNameImage.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        progressDialog.dismiss();
                        Toast.makeText(mContext,
                                "unsuccessful uploads of one of the pictures. Error :  " + exception,
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata();// contains file metadata such as size, content-type, etc.

                        if(iNumImage == arrayListPathImages.size())
                        {
                            progressDialog.dismiss();
                            finish();
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
            }
            iNumImage ++;
        }
    }

    private void ChangeTheColor(String s, int start, int end) {
        mspanable.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
