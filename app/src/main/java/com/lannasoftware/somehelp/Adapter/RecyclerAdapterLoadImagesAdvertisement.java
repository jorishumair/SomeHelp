package com.lannasoftware.somehelp.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lannasoftware.somehelp.R;

import java.util.ArrayList;

public class RecyclerAdapterLoadImagesAdvertisement extends RecyclerView.Adapter {

    private ArrayList<Uri> imageList;
    private Context mContext;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public RecyclerAdapterLoadImagesAdvertisement(Context context, ArrayList<Uri> imageList, RecyclerView recyclerView) {
        this.imageList = imageList;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionFooter (position)) {
            return TYPE_FOOTER;
        }else{
            return TYPE_ITEM;
        }
    }

    private boolean isPositionFooter (int position) {
        return position == imageList.size();
    }

    @Override
    public int getItemCount() {
        return imageList.size() +1;
    }

    private Uri getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gallery_load_image_advertisement, viewGroup, false);
            return new VHItem(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_gallery_load_new_image_advertisement, viewGroup, false);
            return new LoadHolder(view);
        }

        return  null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof VHItem) {
            ((VHItem) viewHolder).img.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Glide.with(mContext).load(getItem(i))
                    .thumbnail(0.5f)
                    .into(((VHItem) viewHolder).img);
        }
    }

    public class VHItem extends RecyclerView.ViewHolder{

        private ImageView img;

        public VHItem(View view) {
            super(view);

            img = view.findViewById(R.id.img);
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void clear() {
        imageList.clear();
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RecyclerAdapterLoadImagesAdvertisement.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecyclerAdapterLoadImagesAdvertisement.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}