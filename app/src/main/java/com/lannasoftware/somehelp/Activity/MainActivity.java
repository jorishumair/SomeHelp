package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome1;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome2;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome3;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome4;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.Helper.ViewPager;
import com.lannasoftware.somehelp.R;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /*
    FloatingActionButton cFloatingButtonItem1;
    FloatingActionButton cFloatingButtonItem2;
    FloatingActionButton cFloatingButtonItem3;
*/
    Context mContext;

    private TabLayout cTabLayout;
    private ViewPager cViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        cTabLayout = (TabLayout) findViewById(R.id.tabs);
        cViewPager = (ViewPager) findViewById(R.id.viewpager);

        cViewPager.setEnableSwipe(true);

        List fragments = new Vector();
        Bundle args_fragment_1 = new Bundle();
        /*args_fragment_3.putParcelableArrayList("dataPublicationsMatch", dataPublicationsMatch);
        args_fragment_3.putParcelableArrayList("dataPublicationsDiscussion", dataPublicationsDiscussion);*/

        fragments.add(Fragment.instantiate(mContext, FragmentHome1.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome2.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome3.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome4.class.getName(), args_fragment_1));

        SampleFragmentPagerAdapter pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        cViewPager.setAdapter(pagerAdapter);

        cTabLayout.setupWithViewPager(cViewPager);

        for (int i = 0; i < cTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = cTabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        /*
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        /*
        cFloatingButtonItem1 = findViewById(R.id.floatingActionButton_item1);
        cFloatingButtonItem2 = findViewById(R.id.floatingActionButton_item2);
        cFloatingButtonItem3 = findViewById(R.id.floatingActionButton_item3);

        cFloatingButtonItem1.setOnClickListener(this);
        cFloatingButtonItem2.setOnClickListener(this);
        cFloatingButtonItem3.setOnClickListener(this);
        */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
/*
            case R.id.floatingActionButton_item1 :
                Intent CreateAdvertisement1 = new Intent(MainActivity.this, CreateAdvertisementParentFragment.class);
                CreateAdvertisement1.putExtra("annonceType", CEnum.AnnonceType.DemanderService.name());
                CreateAdvertisement1.putExtra("annonceName", cFloatingButtonItem1.getLabelText());
                startActivity(CreateAdvertisement1);
                break;
            case R.id.floatingActionButton_item2 :
                Intent CreateAdvertisement2 = new Intent(MainActivity.this, CreateAdvertisementParentFragment.class);
                CreateAdvertisement2.putExtra("annonceType", CEnum.AnnonceType.ProposerService.name());
                CreateAdvertisement2.putExtra("annonceName", cFloatingButtonItem2.getLabelText());
                startActivity(CreateAdvertisement2);
                break;

            case R.id.floatingActionButton_item3 :
                Intent CreateAdvertisement3 = new Intent(MainActivity.this, CreateAdvertisementParentFragment.class);
                CreateAdvertisement3.putExtra("annonceType", CEnum.AnnonceType.VendreArticle.name());
                CreateAdvertisement3.putExtra("annonceName", cFloatingButtonItem3.getLabelText());
                startActivity(CreateAdvertisement3);
                break;
                */
        }
    }

    private class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments;

        private int[] tabIcons = {
                R.drawable.tab_icon1,
                R.drawable.tab_icon2,
                R.drawable.tab_icon3,
                R.drawable.tab_icon4
        };

        public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public View getTabView(int position) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            ImageView img = (ImageView) v.findViewById(R.id.imgView);
            img.setImageResource(tabIcons[position]);

            TextView tv = (TextView) v.findViewById(R.id.textView);

            HelperApp.SetFont(mContext, tv,"Roboto-Black.ttf");

            switch (position){
                case 0:
                    tv.setText("EXPLORER");
                    break;
                case 1:
                    tv.setText("ENREGISTRÉ");
                    break;
                case 2:
                    tv.setText("VOYAGES");
                    break;
                case 3:
                    tv.setText("PROFIL");
                    break;
            }
            return v;
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}
