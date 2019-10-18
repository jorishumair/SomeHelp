package com.lannasoftware.somehelp.Activity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome1;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome2;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome3;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome4;
import com.lannasoftware.somehelp.Activity.Fragments.FragmentHome5;
import com.lannasoftware.somehelp.Entity.User;
import com.lannasoftware.somehelp.Helper.HelperApp;
import com.lannasoftware.somehelp.Helper.ViewPager;
import com.lannasoftware.somehelp.R;
import com.lannasoftware.somehelp.SQLite.DAOUser;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;

    DAOUser cDaoUser;
    User currentUserSQLite;

    private TabLayout cTabLayout;
    private ViewPager cViewPager;

    String sHostMode = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        cTabLayout = findViewById(R.id.tabs);
        cViewPager = findViewById(R.id.viewpager);

        cViewPager.setEnableSwipe(true);

        cDaoUser = new DAOUser(mContext);
        cDaoUser.Open();
        currentUserSQLite = cDaoUser.GetUserById(1);
        sHostMode = currentUserSQLite.getsModeHost();
        cDaoUser.Close();

        Toast.makeText(mContext, sHostMode + "", Toast.LENGTH_SHORT).show();

        List fragments = new Vector();
        Bundle args_fragment_1 = new Bundle();
        /*args_fragment_3.putParcelableArrayList("dataPublicationsMatch", dataPublicationsMatch);
        args_fragment_3.putParcelableArrayList("dataPublicationsDiscussion", dataPublicationsDiscussion);*/

        fragments.add(Fragment.instantiate(mContext, FragmentHome1.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome2.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome3.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome4.class.getName()));
        fragments.add(Fragment.instantiate(mContext, FragmentHome5.class.getName(), args_fragment_1));

        SampleFragmentPagerAdapter pagerAdapter = new SampleFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        cViewPager.setAdapter(pagerAdapter);

        cTabLayout.setupWithViewPager(cViewPager);

        for (int i = 0; i < cTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = cTabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

    private class SampleFragmentPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragments;

        private int[] tabIcons = {
                R.drawable.tab_icon1,
                R.drawable.tab_icon2,
                R.drawable.tab_icon3,
                R.drawable.tab_icon3,
                R.drawable.tab_icon4
        };

        public SampleFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        public View getTabView(int position) {

            View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
            ImageView img = v.findViewById(R.id.imgView);
            img.setImageResource(tabIcons[position]);

            TextView tv = v.findViewById(R.id.textView);

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
                    tv.setText("MESSAGES");
                    break;
                case 4:
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
