package com.thenewboston.chatapplication;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by rahul on 08/10/15.
 */
public class CustomActivity extends AppCompatActivity implements ListView_Interface,View.OnClickListener{

    public static final TouchEffect Touch = new TouchEffect();
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
    }




    public void searchContacts(final String query) {
        final Contacts_RightFragment contactsFragment = (Contacts_RightFragment) getSupportFragmentManager().findFragmentById(R.id.contacts);

        if (contactsFragment != null) {
            contactsFragment.searchContactsFragment(query);
            Toast.makeText(getApplicationContext(), "Query : " + query, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Contacts_RightFragment Fragment Not Created, Check HomeScreen Code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ClickNewUserForChat(ParseUser user) {
        if(user==null) return;
        Userlist_LeftFragment userlist = new Userlist_LeftFragment();
        userlist.newUser(user);
    }

    @Override
    public void onClick(View v) {

    }
    protected void setupActionBar()
    {
        final ActionBar actionBar = getSupportActionBar();
        if(actionBar==null)
        {
            Toast.makeText(getApplicationContext(),"Error!, No Action bar, Check code",Toast.LENGTH_LONG).show();

            return;
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.ColorPrimary));
        actionBar.setTitle("CHAT APP");
        actionBar.setIcon(R.drawable.logo);
    }

    public View setTouchAndClick(int id)
    {
        View view= setClick(id);
        if(view!=null)
        {
            view.setOnTouchListener(Touch);
        }
        return view;
    }

    public View setClick(int id)
    {
        View view = findViewById(id);
        if(view!=null)
            view.setOnClickListener(this);
        return view;
    }

//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//

}
