package com.thenewboston.chatapplication;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by rahul on 08/10/15.
 */
public class ParseClass extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "EvnsX4WDkazR7qFwizH5ArHAAgCCTL3H8AfJT9EH", "UcR0TMEUSIZXxfBtC5Y1UIl6dUBqo0iEnKV1kptY");
        ParseUser.enableAutomaticUser();
        ParseACL defauACL = new ParseACL();
        defauACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defauACL,true);

    }
}
