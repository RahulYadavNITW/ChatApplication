package com.thenewboston.chatapplication;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by HPLAP on 9/27/2015.
 */
public class FetchContacts extends AsyncTask<String, Void, Void>
{
    ProgressDialog pd;
    public Context context;
    ArrayList<String> nListIds = new ArrayList<String>();
    ArrayList<String> num= new ArrayList<String>();
    ContactsListViewHolder viewHolder;
    public FetchContacts(Context context, ArrayList<String> nListIds, ContactsListViewHolder viewHolder)
    {
        this.context = context;
        this.nListIds = nListIds;
        this.viewHolder = viewHolder;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        pd = ProgressDialog.show(context,
                "Loading..", "Please Wait", true, false);
        viewHolder.contactname.setVisibility(View.VISIBLE);
        viewHolder.number.setVisibility(View.VISIBLE);
        viewHolder.emailid.setVisibility(View.VISIBLE);
        viewHolder.imageval.setVisibility(View.VISIBLE);
        viewHolder.contactname.setText("");
        viewHolder.number.setText("");
        viewHolder.emailid.setText("");
        viewHolder.imageval.setImageResource(R.drawable.namini);
    }

    @Override
    protected Void doInBackground(String... params) {

        ContentResolver contentResolver = context.getContentResolver();

        int position = Integer.parseInt(params[0]);
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,null, null, null);
       /* Cursor cursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{nListIds.get(position)}, null);*/

//                Log.i("email count", "" + cursor.getCount());
        if(cursor.getCount()>0) {
            while (cursor.moveToNext()) {

                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                nListIds.add(name);
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = contentResolver.query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                            new String[]{id},
                            null);
                    while (pCur.moveToNext())
                    {
                        String phoneNumber = pCur.getString(pCur.getColumnIndex( ContactsContract.CommonDataKinds.Phone.NUMBER));
                        num.add(phoneNumber);
                    }
                    pCur.close();
                }

            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        pd.dismiss();

        //ContactsAdapter cus = new ContactsAdapter(context);
        // ArrayAdapter<String>   arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,aa);
       // .setAdapter(cus);



    }
}