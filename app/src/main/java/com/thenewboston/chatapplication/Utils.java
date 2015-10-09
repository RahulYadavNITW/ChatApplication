package com.thenewboston.chatapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

/**
 * Created by rahul on 08/10/15.
 */
public class Utils {

    public static AlertDialog showDialog(Context context,String message, String Button1, String Button2,
                                         DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton(Button1,listener1);
        if(Button2!=null && listener2!=null)
            builder.setNegativeButton(Button2,listener2);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }
    public static AlertDialog showDialog(Context context,int message, int Button1, int Button2,
                                         DialogInterface.OnClickListener listener1,
                                         DialogInterface.OnClickListener listener2)
    {
        return showDialog(context,context.getString(message),context.getString(Button1),context.getString(Button2),
                         listener1, listener2);
    }

    public static AlertDialog showDialog(Context context, String msg,DialogInterface.OnClickListener listener)
    {
        return showDialog(context,msg,"OKAY",null,listener,null);
    }
    public static AlertDialog showDialog(Context context, String msg)
    {
        return showDialog(context,msg,new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void showDialog(Context context,int title, int message,
                                         DialogInterface.OnClickListener listener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OKAY",listener);
        builder.setTitle(title);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public static AlertDialog showDialog(Context context, int msg)
    {
        return showDialog(context,context.getString(msg));
    }

    public static final void hideKeyBoard(Activity activity)
    {
        if(activity.getCurrentFocus()!=null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(),0);
        }
    }
    public static final void hideKeyBoard(Activity activity,View v)
    {
        try
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(v.getWindowToken(),0);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}

