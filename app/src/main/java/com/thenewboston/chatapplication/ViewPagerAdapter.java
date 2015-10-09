package com.thenewboston.chatapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
  * Created by hp1 on 21-01-2015.
  */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    Contacts_RightFragment contacts_rightFragment;

// Build a Constructor and assign the passed Values to appropriate values in the class
        public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

        }

            //This method return the fragment for the every position in the View Pager
            @Override
        public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            Userlist_LeftFragment tab1 = new Userlist_LeftFragment();
            return tab1;
        }
        else
        {
            contacts_rightFragment = new Contacts_RightFragment();
            return contacts_rightFragment;
        }


        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return Titles[position];
        }

        @Override
        public int getCount()
        {
            return NumbOfTabs;
        }
}