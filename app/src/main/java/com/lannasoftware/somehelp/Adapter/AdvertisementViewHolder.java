package com.lannasoftware.somehelp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lannasoftware.somehelp.R;

import java.util.ArrayList;

public class AdvertisementViewHolder extends RecyclerView.ViewHolder {

    Context mContext;

    private View view;

    private FirebaseUser currentFirebaseUser;
    FirebaseStorage storage;

    public AdvertisementViewHolder(View itemView, Context Context) {
        super(itemView);

        mContext = Context;

        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();

        view = itemView;
    }

    public void setAdvertisementTitle(String title) {
        TextView textView = view.findViewById(R.id.txt_title);
        textView.setText(title);
    }

    public void setAdvertisementPrice(String price) {
        TextView textView = view.findViewById(R.id.txt_price);
        textView.setText(price);
    }

    public void setAdvertisementDescription(String description) {
        TextView textView = view.findViewById(R.id.txt_description);
        textView.setText(description);
    }

    public void setAdvertisementLocation(String location) {
        TextView textView = view.findViewById(R.id.txt_location);
        textView.setText(location);
    }

    public void setAdvertisementUserName(String username) {
        TextView textView = view.findViewById(R.id.txt_user_name);
        textView.setText(username);
    }

    public void setAdvertisementImages(ArrayList<String> cImages) {

        final ImageView image = view.findViewById(R.id.img_advertisement);

        for (String sImage : cImages) {

            StorageReference storageReference = storage.getReference().child("advertisements/"+sImage);

            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Glide.with(mContext)
                                .load(task.getResult())
                                .into(image);
                    }
                    else {
                        Toast.makeText(mContext, "Error loading image. Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

