package com.thenewboston.chatapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

public class HomeScreen extends CustomActivity implements SearchView.OnQueryTextListener {

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    ActionBar bar;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"INBOX", "CONTACTS"};
    int Numboftabs = 2;
    SearchView searchView;
    ListView_Interface listView_interface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ParseUser currentuser = ParseUser.getCurrentUser();
        String struser = currentuser.getUsername().toString();
        Toast.makeText(getApplicationContext(), "Welcome " + struser + " :)", Toast.LENGTH_SHORT);

        adapter = new ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        tabs.setViewPager(pager);
        handleIntent(getIntent());
    }


    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        return super.onPrepareOptionsPanel(view, menu);
    }

    public void chatWithUser(View v) {
        SingleContact contact = new SingleContact(v.findViewById(R.id.contactName).toString(), v.findViewById(R.id.contactNumber).toString(), v.findViewById(R.id.emailid).toString(), null, 0);
        ParseUser user = new ParseUser();
        user.setUsername(contact.getName());
        user.setPassword(contact.getNumber());
        user.setEmail(contact.getEmailid());
        listView_interface.ClickNewUserForChat(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.searchview);
        searchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        searchView.setOnQueryTextListener(HomeScreen.this);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

//
////        MenuItem mSearchMenuItem = menu.findItem(R.id.searchview);
////        SearchView searchView = (EnglishVerbSearchView) MenuItemCompat.getActionView(mSearchMenuItem);
////        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), HomeScreen.class)));
//
//            Toast.makeText(getApplicationContext(),"tick1",Toast.LENGTH_SHORT).show();
//
//        MenuItem searchMenuItem = menu.findItem(R.id.searchview);
//        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView = MySearchView.getSearchView(HomeScreen.this, "");
//            Toast.makeText(getApplicationContext(),"tick2",Toast.LENGTH_SHORT).show();
//        setupSearchView(searchMenuItem);
//
//            Toast.makeText(getApplicationContext(),"tick3",Toast.LENGTH_SHORT).show();
//        searchView.setOnQueryTextListener(this);
//             Toast.makeText(getApplicationContext(),"tick4",Toast.LENGTH_SHORT).show();
//        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do something when collapsed
//                return true;  // Return true to collapse action view
//            }
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do something when expanded
//                return true;  // Return true to expand action view
//            }
//        });
//        Toast.makeText(getApplicationContext(),"tuck5",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            //   searchContacts(query);
        } else {
            // Toast.makeText(getApplicationContext(), "UNKNOWN INTENT CALLED IS : " + intent.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupSearchView(MenuItem searchItem) {
        searchView.setIconifiedByDefault(true);
        // searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(false);
        //searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        // | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Toast.makeText(getApplicationContext(), "typed" + newText, Toast.LENGTH_SHORT).show();
        Log.d("type", newText);
        // searchContacts(newText);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getApplicationContext(), "Item Selected" + item.getTitle(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void ClickNewUserForChat(ParseUser user) {
        if (user == null) return;
        Userlist_LeftFragment userlist = new Userlist_LeftFragment();
        userlist.newUser(user);
    }
}