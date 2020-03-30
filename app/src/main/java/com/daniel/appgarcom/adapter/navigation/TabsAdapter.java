package com.daniel.appgarcom.adapter.navigation;



import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by polo on 05/07/2018.
 */

public class TabsAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragments = new ArrayList<>();
    private List<String> listFragmentsTitle =  new ArrayList<>();

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    public void add(Fragment frag, String title){
        Log.i("IFMG", "addFragment");
        this.listFragments.add(frag);
        this.listFragmentsTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragments.get(position);
    }

    @Override
    public int getCount() {
        return ((!listFragments.isEmpty()) ? listFragments.size(): 0);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return listFragmentsTitle.get(position);
    }
}
