package com.thenewboston.chatapplication;

import android.graphics.Bitmap;

/**
 * Created by rahul on 24/09/15.
 */
public class SingleContact {
    private final String nameText = "NAME : "  ;
    private final String numberText = "CONTACT : ";
    private final String emailText = "EMAIL ID : ";
    private String name;
    private String number;
    private String emailid;
    private int imageval;
    private Bitmap thumb;

    public SingleContact(){

    }

    public SingleContact(String name, String number, String emailid, Bitmap thumb, int imageval) {
        this.imageval = imageval;
        this.emailid = emailid;
        this.name = name;
        this.number = number;
        this.thumb = thumb;
    }

    public Bitmap getThumb() { return thumb; }

    public void setThumb(Bitmap thumb) { this.thumb = thumb; }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageval() {
        return imageval;
    }

    public void setImageval(int imageval) {
        this.imageval = imageval;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }
}
