package com.thenewboston.chatapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 25/09/15.
 */
public class Contacts_RightFragment extends Fragment implements AsyncTaskInterface,LoaderManager.LoaderCallbacks<Cursor> {

    String query ="";
    LoaderManager.LoaderCallbacks<Cursor> callback;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView_Interface listView_interface;
    ListView list;
    ContactsAdapter adapter = null;
    ContactsAdapter emptyadapter=null;
    ArrayList<SingleContact> singleContact;
    ArrayList<SingleContact> emptyContactList;
    View view;
    private final static String[] FROM_COLUMNS = {

            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Phone.NUMBER,

    };

    private final static int[] TO_IDS = {
            R.id.contactName,
            R.id.contactNumber,
            //R.id.email
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceStat) {
        view =inflater.inflate(R.layout.contacts,container,false);
         getLoaderManager().initLoader(0,null,this);
        simpleCursorAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.contactlist_row,
                null,
                FROM_COLUMNS, TO_IDS,
                0);

        callback =this;

        list = (ListView) view.findViewById(R.id.contactlistview);
        SomeTask asyncTask = new SomeTask(getActivity(), Contacts_RightFragment.this);
        singleContact = new ArrayList<>();
        emptyContactList = new ArrayList<>();
        emptyContactList.clear();

        list.setAdapter(simpleCursorAdapter);
        list.setClickable(true);
        list.setTextFilterEnabled(true);


//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (adapter != null && adapter.getCount() > 0) {
//                    SingleContact contact = (SingleContact) adapter.getItem(position);
//                    String phone = contact.getNumber();
//                    if (phone != null && phone.length() > 0) {
//                        phone = "tel:" + phone;
//                        Toast.makeText(getActivity(), "contacts" + contact.getName() + "...", Toast.LENGTH_SHORT).show();
//                        Intent i = new Intent(Intent.ACTION_CALL);
//                        // i.setData(parse(phone));
//                        startActivity(i);
//                    }
//
//                }
//            }
//        });
        return view;
    }




    @Override
    public void onStart() {
        super.onStart();


    }



    public void searchContactsFragment(final String query) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putString("query", query);

                getLoaderManager().restartLoader(1,bundle,callback);
                simpleCursorAdapter = new SimpleCursorAdapter(
                        getActivity(),
                        R.layout.contactlist_row,
                        null,
                        FROM_COLUMNS, TO_IDS,
                        0);
                list.setAdapter(simpleCursorAdapter);
//                adapter.getFilter().filter(query);
                list.setClickable(true);
                list.setTextFilterEnabled(true);
            }
        });

    }

    public void changePosition(final String query) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (query.equals("")) {
                    emptyadapter = new ContactsAdapter(getActivity(),emptyContactList);
                    list.setAdapter(emptyadapter);
                    //SomeTask asyncTask = new SomeTask(getActivity(), Contacts_RightFragment.this);
                   // asyncTask.execute();
                    list.setAdapter(simpleCursorAdapter);
                    list.setClickable(true);
                    list.setTextFilterEnabled(true);

                } else {
                    Bundle bundle = new Bundle();
                bundle.putString("query", query);
                getLoaderManager().restartLoader(1,bundle,callback);
                simpleCursorAdapter = new SimpleCursorAdapter(
                        getActivity(),
                        R.layout.contactlist_row,
                        null,
                        FROM_COLUMNS, TO_IDS,
                        0);
                    list.setAdapter(simpleCursorAdapter);
                    list.setClickable(true);
                    list.setTextFilterEnabled(true);

                }

            }
        });

    }


    @Override
    public void doTheDew(List<SingleContact> list) {
        singleContact.addAll(list);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String bundleQuery = "";
        String selectionArgs[] = null;

        if (args != null) {
            bundleQuery = args.getString("query");
        }

        if (getIds(bundleQuery))
        {
            view.findViewById(R.id.no_contacts).setVisibility(View.GONE);
            if (bundleQuery.length() > 1) {
                Log.i("query", query);
                String q = "%" + bundleQuery + "%";
                selectionArgs = new String[]{q};
//                Toast.makeText(getActivity()," length > 1 " ,Toast.LENGTH_SHORT).show();
            } else {
                Log.i("query", query);
                String q = bundleQuery + "%";
                selectionArgs = new String[]{q};
                Log.i("size", "" + selectionArgs.length);

   //             Toast.makeText(getActivity()," length = 1",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            view.findViewById(R.id.no_contacts).setVisibility(View.VISIBLE);
        }



        switch (id) {

            case 0:
                return new android.support.v4.content.CursorLoader(
                        getActivity(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        null,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE"

                );

            default:
//                Toast.makeText(getActivity(),"In "+selectionArgs[0],Toast.LENGTH_SHORT).show();


                    android.support.v4.content.CursorLoader cursorLoader=  new android.support.v4.content.CursorLoader(
                        getActivity(),
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " LIKE ? ",
                        selectionArgs,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY + " COLLATE NOCASE"
                    );

                //Toast.makeText(getActivity(),cursorLoader.getSelection().toString(),Toast.LENGTH_SHORT).show();
                return cursorLoader;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }


    public boolean getIds(String query){
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor;
        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" LIKE ?";
        String selectionArgs[];

        if (query.equals(""))
        {
            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" COLLATE NOCASE");
        }
        else {

            if (query.length() > 1) {
                selectionArgs = new String[]{"%" +query + "%"};
            } else {
                selectionArgs = new String[]{query + "%"};
            }

            cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, selection, selectionArgs, ContactsContract.Contacts.DISPLAY_NAME_PRIMARY+" COLLATE NOCASE");
        }
        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            Toast.makeText(getActivity(), "No Contacts_RightFragment Matching " + query, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //this.listView_interface = ((HomeScreen) getActivity()) ;
    }
}

