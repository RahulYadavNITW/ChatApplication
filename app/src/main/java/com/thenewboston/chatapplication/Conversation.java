package com.thenewboston.chatapplication;


import java.util.Date;

/**
 * Created by rahul on 08/10/15.
 */
public class Conversation {
    public static final int STATUS_SENDING =0;
    public static final int STATUS_SENT =1;
    public static final int STATUS_DELIVERED_SEEN =2;
    private String message;
    private int status = STATUS_SENT;
    private Date lstmsgdate;
    private String sender;

    public Conversation(String message, Date lstmsgDate, String sender) {
        this.message = message;
        this.sender = sender;
        this.lstmsgdate = lstmsgDate;
    }
    public Conversation()
    {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getLstmsgdate() {
        return lstmsgdate;
    }

    public void setLstmsgdate(Date lstmsgdate) {
        this.lstmsgdate = lstmsgdate;
    }

    public boolean isSent()
    {
        return Userlist_LeftFragment.user.getUsername().equals("sender");
    }

}
