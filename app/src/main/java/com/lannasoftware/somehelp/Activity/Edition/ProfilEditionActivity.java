package com.lannasoftware.somehelp.Activity.Edition;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfilEditionActivity extends AppCompatActivity implements View.OnClickListener {

    static final int PROFIL_EDITION = 1;
    static final int REQUEST_GALLERY_IMG = 2;
    static final int REQUEST_TAKE_PHOTO = 3;

    private Boolean bCameraUsedToLoadPicture = false;
    private static final int MY_PERMISSIONS_REQUEST_Write = 0;
    private static final int MY_PERMISSIONS_REQUEST_Camera = 1;
    int iPermissionCheckWrite;
    int iPermissionCheckCamera;
    String sPicturePath;
    String sPictureFileName;
    String sFinalPictureFileName;
    Uri cUri;
    StorageReference storageRef;
    FirebaseStorage storage;

    Context mContext;

    FirebaseFirestore dbFirestore;
    DAOUser cDaoUser;
    User currentUserSQLite;
    String sUserSQLiteIdFirestore;

    AlertDialog alertDialog1;

    TextView txt_firstname;
    TextView txt_lastname;
    TextView txt_modifier_le_nom;
    TextView txt_content_a_propos_de_moi;
    TextView txt_content_sexe;
    TextView txt_content_ville;
    TextView txt_content_email;
    TextView txt_content_telephone;
    TextView txt_content_piece_identie;
    TextView txt_content_langue;

    RelativeLayout rl_edit_sexe;

    ImageView btn_back;
    ImageView img_user_image;

    RelativeLayout rl_edit_nom;
    RelativeLayout rl_edit_a_propos_de_moi;
    RelativeLayout rl_edit_email;

    String[] cGenderValues = {"Homme","Femme","Autre"};

    String sFirstname;
    String sLastname;
    String sAbout;
    String sSexe;
    String sSexeModified;
    String sVille;
    String sEmail;
    String sTelephone;
    String sPieceIdentite;
    String sLangue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_edition);

        mContext = this;

        txt_firstname = findViewById(R.id.txt_firstname);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_modifier_le_nom = findViewById(R.id.txt_modifier_le_nom);
        img_user_image = findViewById(R.id.img_user_image);
        rl_edit_nom = findViewById(R.id.rl_edit_nom);
        rl_edit_a_propos_de_moi = findViewById(R.id.rl_edit_a_propos_de_moi);
        txt_content_a_propos_de_moi = findViewById(R.id.txt_content_a_propos_de_moi);
        btn_back = findViewById(R.id.btn_back);
        txt_content_sexe = findViewById(R.id.txt_content_sexe);
        txt_content_ville = findViewById(R.id.txt_content_ville);
        txt_content_email = findViewById(R.id.txt_content_email);
        txt_content_telephone = findViewById(R.id.txt_content_telephone);
        txt_content_piece_identie = findViewById(R.id.txt_content_piece_identie);
        txt_content_langue = findViewById(R.id.txt_content_langue);
        rl_edit_sexe = findViewById(R.id.rl_edit_sexe);
        rl_edit_email = findViewById(R.id.rl_edit_email);

        HelperApp.SetFont(mContext, txt_firstname,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_lastname,"Roboto-Medium.ttf");
        HelperApp.SetFont(mContext, txt_modifier_le_nom,"Roboto-Bold.ttf");
        HelperApp.SetFont(mContext, txt_content_a_propos_de_moi,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_sexe,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_ville,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_email,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_telephone,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_piece_identie,"Roboto-Thin.ttf");
        HelperApp.SetFont(mContext, txt_content_langue,"Roboto-Thin.ttf");

        rl_edit_nom.setOnClickListener(this);
        rl_edit_a_propos_de_moi.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        rl_edit_sexe.setOnClickListener(this);
        rl_edit_email.setOnClickListener(this);
        img_user_image.setOnClickListener(this);

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
            case R.id.rl_edit_nom :
                AccessNamesEdition();
                break;

            case R.id.rl_edit_a_propos_de_moi :
                AccessAboutEdition();
                break;

            case R.id.rl_edit_sexe :
                AccessSexeEdition();
                break;

            case R.id.rl_edit_email :
                AccessEmailEdition();
                break;

            case R.id.img_user_image :
                OpenDialogCamera();
                break;

            case R.id.btn_back :
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PROFIL_EDITION) {
            if (resultCode == RESULT_OK) {

                String result = data.getStringExtra("modification");

                if(result.equals("true"))
                    FillInformationsAboutUser();
            }
        }
        else if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                cUri = Uri.parse(sPicturePath);
                File file = new File(cUri.getPath());
                try {
                    InputStream ims = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    return;
                }

                // ScanFile so it will be appeared on Gallery
                MediaScannerConnection.scanFile(this, new String[]{cUri.getPath()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
                sFinalPictureFileName = sPictureFileName;

                UpdateStateTask task = new UpdateStateTask();
                task.execute();
            }
        } else if (requestCode == REQUEST_GALLERY_IMG) {
            if (resultCode == RESULT_OK) {
                cUri = data.getData();

                Uri returnUri = data.getData();
                Cursor returnCursor = mContext.getContentResolver().query(returnUri, null, null, null, null);

                assert returnCursor != null;
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String name = returnCursor.getString(nameIndex);
                returnCursor.close();

                sFinalPictureFileName = name;

                UpdateStateTask task = new UpdateStateTask();
                task.execute();
            }
        }
    }

    private void FillInformationsAboutUser()
    {
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String sUserUid = currentFirebaseUser.getUid();
        String sUserProfilImageLink = null;

        if(sUserUid != null && !sUserUid.isEmpty())
            sUserProfilImageLink = "profils/"+sUserUid+".jpg";

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();

        currentUserSQLite = cDaoUser.GetUserById(1);

        sFirstname = currentUserSQLite.getsFirstname();
        sLastname = currentUserSQLite.getsLastname();
        sAbout = currentUserSQLite.getsAbout();
        sSexe = currentUserSQLite.getsSexe();
        sVille = currentUserSQLite.getsVille();
        sEmail = currentUserSQLite.getsEmail();
        sTelephone = currentUserSQLite.getsTelephone();
        sPieceIdentite = currentUserSQLite.getsPieceIdentite();
        sLangue = currentUserSQLite.getsLangue();

        txt_firstname.setText(sFirstname);
        txt_lastname.setText(sLastname);
        txt_content_a_propos_de_moi.setText(sAbout);
        txt_content_sexe.setText(sSexe);
        txt_content_ville.setText(sVille);
        txt_content_email.setText(sEmail);
        txt_content_telephone.setText(sTelephone);
        txt_content_piece_identie.setText(sPieceIdentite);
        txt_content_langue.setText(sLangue);

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

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

        cDaoUser.Close();
    }

    private void AccessNamesEdition()
    {
        Intent IAccessNamesEdition = new Intent(mContext, NameEditionActivity.class);
        startActivityForResult(IAccessNamesEdition, PROFIL_EDITION);
    }

    private void AccessAboutEdition()
    {
        Intent IAccessAboutEdition = new Intent(mContext, AboutEditionActivity.class);
        startActivityForResult(IAccessAboutEdition, PROFIL_EDITION);
    }

    private void AccessEmailEdition()
    {
        Intent IAccessEmailEdition = new Intent(mContext, EmailEditionActivity.class);
        startActivityForResult(IAccessEmailEdition, PROFIL_EDITION);
    }

    private void AccessSexeEdition()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilEditionActivity.this);

        int iChoice = -1;
        int iElem = 0;
        if(sSexe != null)
        {
            for (String sValue:
                    cGenderValues) {
                if(sSexe.equals(sValue))
                    iChoice = iElem;
                iElem++;
            }
        }

        builder.setTitle("Sexe");
        builder.setSingleChoiceItems(cGenderValues, iChoice, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:
                        if(sSexe == null || !sSexe.equals(cGenderValues[item]))
                        {
                            sSexeModified = cGenderValues[item];
                            SaveInformationsAboutSexe();
                        }
                        break;
                    case 1:
                        if(sSexe == null || !sSexe.equals(cGenderValues[item]))
                        {
                            sSexeModified = cGenderValues[item];
                            SaveInformationsAboutSexe();
                        }
                        break;
                    case 2:
                        if(sSexe == null || !sSexe.equals(cGenderValues[item]))
                        {
                            sSexeModified = cGenderValues[item];
                            SaveInformationsAboutSexe();
                        }
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }

    private void SaveInformationsAboutSexe()
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
            updates.put("gender", sSexeModified);

            docRef.update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    cDaoUser.UpdateSexe(currentUserSQLite, sSexeModified);
                    FillInformationsAboutUser();
                    cDaoUser.Close();


                }
            });
        }
    }

    public class UpdateStateTask extends AsyncTask<Void, Integer, Void> {

        String path = "";

        public UpdateStateTask() {
        }

        protected void onPreExecute() {
           /* daoUser = new DaoUser(mContext);
            daoUser.open();
            currentUser = daoUser.getUserById(1);
            daoUser.close();*/
        }

        protected Void doInBackground(Void... arg0) {

            try {

                path = HelperApp.GetPath(cUri, mContext);
/*
                UserAWS user = mapper.load(UserAWS.class, userIdAws);

                daoUser.open();
                if(image == 0){
                    user.setProfilImage("own");
                    daoUser.UpdateProfilImage(currentUser, "own");
                }

                daoUser.close();

                mapper.save(user);
*/
                beginUpload(path);


            } catch (URISyntaxException e) {
                Toast.makeText(mContext,
                        "Unable to get the file from the given URI.  See error log for details",
                        Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }
    }

    private void beginUpload(String filePath) {

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        String sUserUid = currentFirebaseUser.getUid();
        String sUserProfilImageLink = null;

        if(sUserUid != null)
            sUserProfilImageLink = "profils/"+sUserUid+".jpg";

        if (filePath == null) {
            Toast.makeText(mContext, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
            return;
        }

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef .child(sUserProfilImageLink);// .child("images/mountains.jpg");

        Bitmap scaledBitmap = HelperApp.CompressImage(filePath);

        //create a file to write bitmap data
        File f = new File(mContext.getCacheDir(), sFinalPictureFileName);
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] bitmapdata = bos.toByteArray();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*// Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();*/
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = bos.toByteArray();

        UploadTask uploadTask = mountainImagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(mContext,
                        "unsuccessful uploads",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getMetadata();// contains file metadata such as size, content-type, etc.

                img_user_image.setPadding(0,0,0,0);
                Glide.with(mContext)
                        .load(storageRef)
                        .thumbnail(0.5f)
                        .into(img_user_image);

                Toast.makeText(mContext,
                        "successful uploads",
                        Toast.LENGTH_LONG).show();
            }
        });
/*
        CannedAccessControlList can = CannedAccessControlList.PublicRead;
        TransferObserver observer = transferUtility.upload("lovaabucketprofilimage", s,
                f, can);

        observer.setTransferListener(new UploadListener());
*/
    }

    private void OpenDialogCamera(){

        final Dialog dialog1 = new Dialog(mContext);
        //dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setContentView(R.layout.dialog_choice_type_picture);

        RelativeLayout ll_take_photo = dialog1.findViewById(R.id.ll_take_photo);
        RelativeLayout ll_load_photo = dialog1.findViewById(R.id.ll_load_photo);

        ll_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bCameraUsedToLoadPicture = true;

                //openCamera();
                iPermissionCheckWrite = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                iPermissionCheckCamera = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);

                if(iPermissionCheckWrite == PackageManager.PERMISSION_GRANTED && iPermissionCheckCamera == PackageManager.PERMISSION_GRANTED)
                    DispatchTakePictureIntent();
                else
                    ActivityCompat.requestPermissions(ProfilEditionActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, MY_PERMISSIONS_REQUEST_Write);

                dialog1.dismiss();
            }
        });

        ll_load_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bCameraUsedToLoadPicture = false;

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
                    ActivityCompat.requestPermissions(ProfilEditionActivity.this,
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
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;

            try {
                photoFile = createImageFile(currentUserSQLite);
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(mContext,
                        mContext.getApplicationContext().getPackageName() + ".provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile(User objUser) throws IOException {

        if(objUser == null)
            return null;

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        sPictureFileName = objUser.getsLastname()+"-"+timeStamp;
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                sPictureFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        sPicturePath = "file:" + image.getAbsolutePath();
        return image;
    }
}
