package com.lannasoftware.somehelp.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;

import com.lannasoftware.somehelp.Helper.CEnum;
import com.lannasoftware.somehelp.R;

public class CreateAdvertisementParentFragment extends AppCompatActivity implements CreateAdvertisementChildFragment1.OnFragmentInteractionListener, CreateAdvertisementChildFragment2.OnFragmentInteractionListener, View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private Toolbar mToolbar;
    Button btn_next;

    String sAnnonceType = CEnum.AnnonceType.None.name();
    String sTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advertisement);

        Bundle extras = getIntent().getExtras();
        sAnnonceType = extras.getString("annonceType", CEnum.AnnonceType.None.name());
        sTitle = extras.getString("annonceName", null);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.container);
        btn_next = (Button) findViewById(R.id.btn_next);

        btn_next.setOnClickListener(this);

        if(sAnnonceType != CEnum.AnnonceType.None.name() && sTitle != null)
            mToolbar.setTitle(sTitle);

        setSupportActionBar(mToolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_next :
                mViewPager.setCurrentItem(getItem(+1), true);
                break;
        }
    }

    @Override
    public void onFragmentInteraction(String uri) {
        System.out.println(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_create_advertisement, menu);
        return true;
    }

    private int getItem(int i) {
        return mViewPager.getCurrentItem() + i;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_create_advertisement, container, false);

            return rootView;
        }
    }

    public static CreateAdvertisementChildFragment1 newInstanceCreateAdvertisementChildFragment1(String sAnnonceType) {
        CreateAdvertisementChildFragment1 f = new CreateAdvertisementChildFragment1();
        Bundle args = new Bundle();
        args.putString("sAnnonceType", sAnnonceType);
        f.setArguments(args);
        return f;
    }

    public static CreateAdvertisementChildFragment2 newInstanceCreateAdvertisementChildFragment2(String sAnnonceType) {
        CreateAdvertisementChildFragment2 f = new CreateAdvertisementChildFragment2();
        Bundle args = new Bundle();
        args.putString("sAnnonceType", sAnnonceType);
        f.setArguments(args);
        return f;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] childFragments;

        CreateAdvertisementChildFragment1 fragment1;
        CreateAdvertisementChildFragment2 fragment2;
        Bundle args;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            childFragments = new Fragment[] {
                    newInstanceCreateAdvertisementChildFragment1(sAnnonceType),
                    newInstanceCreateAdvertisementChildFragment2(sAnnonceType)
            };
        }

        @Override
        public Fragment getItem(int position) {
            return childFragments[position];
        }

        @Override
        public int getCount() {
            return childFragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = getItem(position).getClass().getName();
            return title.subSequence(title.lastIndexOf(".") + 1, title.length());
        }
    }
}
