package com.thenewboston.chatapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 30/09/15.
 */
public class SomeTask extends AsyncTask<Void , Void,List<SingleContact>> {

    private List singleContactList;
    private Context ctx;
    AsyncTaskInterface asyncTaskInterface;

    public SomeTask(Context ctx, AsyncTaskInterface asyncTaskInterface) {
        this.ctx = ctx;
        this.asyncTaskInterface = asyncTaskInterface;
        singleContactList = new ArrayList();
    }

    @Override
    protected List<SingleContact> doInBackground(Void... params) {
      List<SingleContact> s=  fillContacts();
        return s;
    }

    public List fillContacts() {
        // manger.deviceList.clear();
        if(singleContactList.size()>0)
        singleContactList.clear();

        ContentResolver contentResolver = ctx.getContentResolver();

        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // Log.d("name",name);
            String image_uri = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            String email = null;

            Bitmap bitmap = null;
            String number = null;


/*
            if (image_uri != null) {

                try {

                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), parse(image_uri));
                } catch (FileNotFoundException e){

                } catch (IOException e) {


                } catch (Exception e){

                }

            }
*/

            //get phone numbers
            Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            while (phones.moveToNext()) {
                int type = phones.getInt(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                if (type == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE) {
                    number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
            }
            phones.close();

            Cursor emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId, null, null);
            while (emails.moveToNext()) {
                int type = emails.getInt(emails.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                if (type == ContactsContract.CommonDataKinds.Email.TYPE_WORK) {
                    email = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                }
            }
            emails.close();
            if (number != null) {
                SingleContact contact = new SingleContact();
                contact.setEmailid(email);
                contact.setNumber(number);
                contact.setName(name);
                contact.setThumb(bitmap);
                //manger.deviceList.add(contact);
                singleContactList.add(contact);
            }
            cursor.moveToNext();

        }
        cursor.close();
return singleContactList;

    }

    @Override
    protected void onPostExecute(List<SingleContact> singleContacts) {
        super.onPostExecute(singleContacts);
        asyncTaskInterface.doTheDew(singleContacts);
    }
}
