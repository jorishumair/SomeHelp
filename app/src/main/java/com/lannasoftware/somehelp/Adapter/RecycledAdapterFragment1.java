package com.lannasoftware.somehelp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lannasoftware.somehelp.Entity.Advertisement;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.OnLoadMoreListener;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecycledAdapterFragment1 extends RecyclerView.Adapter {
    private ArrayList<Object> galleryList;
    private Context mContext;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_ADVIEW = 3;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    boolean isMoreDataAvailable = false;
    private OnLoadMoreListener onLoadMoreListener;

    DAOUser daoUser;
    User currentUser;
    String userIdLanna;

    List<String> lst_cherche;

    String status = "";

    public RecycledAdapterFragment1(Context context, ArrayList<Object> galleryList, RecyclerView recyclerView) {

        this.galleryList = galleryList;
        this.mContext = context;

        if (galleryList != null && galleryList.size() == 0) {
            isMoreDataAvailable = false;
        }

        daoUser = new DAOUser(context);
        daoUser.Open();
        currentUser = daoUser.GetUserById(1);
        userIdLanna = currentUser.getsIdFirestore();
        daoUser.Close();

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager
                            .findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        } else if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        } else {
            if (status.equals("premium")) {
                return TYPE_ITEM;
            } else {
                return (position % 5 == 0) ? TYPE_ADVIEW : TYPE_ITEM;
            }
        }
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private boolean isPositionFooter(int position) {
        return position == galleryList.size() + 1;
    }

    @Override
    public int getItemCount() {
        return galleryList.size() + 2;
    }

    private Advertisement getItem(int position) {
        return (Advertisement) galleryList.get(position - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_advertisement_in_list, viewGroup, false);
            return new VHItem(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_advertisement_header_in_list, viewGroup, false);
            return new VHeader(view);
        } else if (viewType == TYPE_FOOTER) {
            //return new LoadHolder(inflater.inflate(R.layout.footer_load_more,parent,ic_false));
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_advertisement_progresbar_in_list, viewGroup, false);
            return new LoadHolder(view);
        } else if (viewType == TYPE_ADVIEW) {
            // View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.native_express_ad_container, viewGroup, false);
            // return new NativeExpressAdViewHolder(view);
        }

        return null;
    }

/*    public String GetProfilPictureUser(int position) {
        if (getItem(position).getProfilImage() == null) return null;
        String url = "https://s3.amazonaws.com/lovaabucketprofilimage/" + getItem(position).getIdAws();
        return url;
    }*/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

        final int position = i;
        int viewType = getItemViewType(i);

        switch (viewType) {

            case TYPE_HEADER:

                ArrayList<String> str_tab = new ArrayList<>();
                StringBuilder str_info_more = new StringBuilder();
/*
                if(currentUser.getVille() != null){
                    str_tab.add(currentUser.getVille());
                }

                if(currentUser.getRegion() != null){
                    str_tab.add(currentUser.getRegion());
                }

                if(currentUser.getPays() != null){
                    str_tab.add(currentUser.getPays());
                }

                if(currentUser.getChercheSexe() != null){

                    if(currentUser.getChercheSexe().equals("1")){
                        str_tab.add(lst_cherche.get(0));
                    }else if (currentUser.getChercheSexe().equals("2")){
                        str_tab.add(lst_cherche.get(1));
                    }else if (currentUser.getChercheSexe().equals("3")){
                        str_tab.add(lst_cherche.get(2));
                    }
                }

                if(currentUser.getBetweenAge() != null){
                    String age_between = currentUser.getBetweenAge();

                    String min_age = age_between.substring(0, age_between.indexOf(","));
                    String max_age = age_between.substring(age_between.indexOf(",")+1, age_between.length());
*/
                str_tab.add("entre " + " et " + " ans");
                // }

                if (!str_tab.isEmpty()) {
                    for (int j = 0; j < str_tab.size() - 1; j++) {
                        if (j == 2) {
                            str_info_more.append(str_tab.get(j) + "\n");
                        } else {
                            str_info_more.append(str_tab.get(j) + ", ");
                        }
                    }

                    str_info_more.append(str_tab.get(str_tab.size() - 1));
                }

                ((VHeader) viewHolder).txt_fil.setText(str_info_more);

                break;

            case TYPE_ITEM:

                ((VHItem) viewHolder).rl_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        //bundle.putSerializable("data", galleryList);
                        User usr = (User) galleryList.get(position - 1);
                        bundle.putParcelable("data", usr);
                        bundle.putInt("position", position - 1);
/*
                        FragmentTransaction ft = ((MainActivityFragment)mContext).getSupportFragmentManager().beginTransaction();
                        DialogUser newFragment = DialogUser.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "publication");
                        */
                    }
                });

                ((VHItem) viewHolder).img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        //bundle.putSerializable("data", galleryList);
                        User usr = (User) galleryList.get(position - 1);
                        bundle.putParcelable("data", usr);
                        bundle.putInt("position", position - 1);
/*
                        FragmentTransaction ft = ((MainActivityFragment)mContext).getSupportFragmentManager().beginTransaction();
                        DialogUser newFragment = DialogUser.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "publication");
                        */
                    }
                });

                ((VHItem) viewHolder).img.setScaleType(ImageView.ScaleType.CENTER_CROP);
/*
                Glide.with(mContext).load(getImg(i))
                        .thumbnail(0.5f)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .error(iUserNoImage)
                        .into(((VHItem) viewHolder).img);
*/

                if(getItem(i).getsAdvertisementTitle() != null)
                    ((VHItem) viewHolder).txt_nom_personne.setText(getItem(i).getsAdvertisementTitle());

                break;

            case TYPE_ADVIEW:
/*
                NativeExpressAdViewHolder nativeExpressHolder =  (NativeExpressAdViewHolder) viewHolder;
                NativeExpressAdView adView = (NativeExpressAdView) galleryList.get(i-1);

                ViewGroup adCardView = (ViewGroup) nativeExpressHolder.itemView;
                adCardView.removeAllViews();

                if(adView.getParent() != null){
                    ((ViewGroup)adView.getParent()).removeView(adView);
                }

                adCardView.addView(adView);
*/
                break;

            case TYPE_FOOTER:

                if (galleryList.size() == 0) {
                    ((LoadHolder) viewHolder).progressBar.setVisibility(View.GONE);
                    ((LoadHolder) viewHolder).txt_info_more.setVisibility(View.VISIBLE);
                    ((LoadHolder) viewHolder).txt_info_more.setText(mContext.getResources().getString(R.string.aurevoir));
                } else if (isMoreDataAvailable) {
                    ((LoadHolder) viewHolder).progressBar.setIndeterminate(true);
                    ((LoadHolder) viewHolder).txt_info_more.setVisibility(View.GONE);
                } else {
                    ((LoadHolder) viewHolder).progressBar.setVisibility(View.GONE);
                    ((LoadHolder) viewHolder).txt_info_more.setVisibility(View.VISIBLE);
                    ((LoadHolder) viewHolder).txt_info_more.setText(mContext.getResources().getString(R.string.aurevoir));
                }

                break;
        }
    }

    public void setLoaded() {
        loading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    class VHeader extends RecyclerView.ViewHolder {

        TextView txt_fil;

        public VHeader(View itemView) {
            super(itemView);

            //txt_fil = (TextView) itemView.findViewById(R.id.txt_place);
            //txt_fil.setTypeface(typeface_thin);
        }
    }


    public class VHItem extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView txt_nom_personne;
        private RelativeLayout rl_main;
        private TextView txt_emplacement;
        private ImageView img_en_ligne;
        private ImageView img_status;

        public VHItem(View view) {
            super(view);

            img = (ImageView) view.findViewById(R.id.img);
            txt_nom_personne = (TextView) view.findViewById(R.id.txt_nom_personne);
            rl_main = (RelativeLayout) view.findViewById(R.id.rl_main);
            txt_emplacement = (TextView) view.findViewById(R.id.txt_emplacement);
            img_en_ligne = (ImageView) view.findViewById(R.id.img_en_ligne);
            img_status = (ImageView) view.findViewById(R.id.img_status);

            //txt_nom_personne.setTypeface(typeface_bold);
            //txt_emplacement.setTypeface(typeface_normal);
        }
    }

    public class LoadHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;
        public TextView txt_info_more;

        public LoadHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar1);
            txt_info_more = itemView.findViewById(R.id.txt_info_more);

            //txt_info_more.setTypeface(typeface_thin);
        }
    }

    public void clear() {
        galleryList.clear();
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private RecycledAdapterFragment1.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final RecycledAdapterFragment1.ClickListener clickListener) {
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
