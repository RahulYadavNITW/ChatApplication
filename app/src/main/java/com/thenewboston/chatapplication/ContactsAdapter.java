package com.thenewboston.chatapplication;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 24/09/15.
 */
public class ContactsAdapter extends BaseAdapter {
    private List<SingleContact> Ccontactlist;
    private List<SingleContact> Updatedlist;
    private ListViewFilter filter;
    //private ArrayList<SingleContact> _data;

    private Context Ccontext;
    Cursor cursor;
    public LayoutInflater inflater = null;
    public ContactsListViewHolder viewHolder;

    //public FetchContacts fetchContacts;
    public ContactsAdapter(Context context, ArrayList<SingleContact> contactslist) {
        this.Ccontext = context;
        this.Ccontactlist = contactslist;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return Ccontactlist.size();
    }

    @Override
    public Object getItem(int position) {
        return Ccontactlist.get(position);
    }
    String[] mOriginalFrom;
    @Override
    public long getItemId(int position) {
        return position;
    }

//    public Cursor swapCursor(Cursor c) {
//        // super.swapCursor() will notify observers before we have
//        // a valid mapping, make sure we have a mapping before this
//        // happens
//        findColumns(c, mOriginalFrom);
//        return swapCursor(c);
//    }

    protected boolean mDataValid;
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */

    protected Cursor mCursor;
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */

    protected int mRowIDColumn;
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    protected ChangeObserver mChangeObserver;
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */
    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }
    protected boolean mAutoRequery;
    protected void onContentChanged() {
        if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
            if (false) Log.v("Cursor", "Auto requerying " + mCursor + " due to update");
            mDataValid = mCursor.requery();
        }
    }
    protected DataSetObserver mDataSetObserver;
    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide}
     */

    public Cursor swapCursor(Cursor newCursor) {
        findColumns(newCursor, mOriginalFrom);
        if (newCursor == mCursor) {
            return null;
        }
        Cursor oldCursor = mCursor;
        if (oldCursor != null) {
            if (mChangeObserver != null) oldCursor.unregisterContentObserver(mChangeObserver);
            if (mDataSetObserver != null) oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (newCursor != null) {
            if (mChangeObserver != null) newCursor.registerContentObserver(mChangeObserver);
            if (mDataSetObserver != null) newCursor.registerDataSetObserver(mDataSetObserver);
            mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            mRowIDColumn = -1;
            mDataValid = false;
            // notify the observers about the lack of a data set
            notifyDataSetInvalidated();
        }
        return oldCursor;
    }
    protected int[] mFrom;

    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView == null) {
            v = inflater.inflate(R.layout.contactlist_row, null);
            viewHolder = new ContactsListViewHolder();
            viewHolder.contactname = (TextView) v.findViewById(R.id.contactName);
            viewHolder.number = (TextView) v.findViewById(R.id.contactNumber);
            viewHolder.emailid = (TextView) v.findViewById(R.id.emailid);
            viewHolder.imageval = (ImageView) v.findViewById(R.id.contactImage);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ContactsListViewHolder) v.getTag();
        }
        SingleContact currentP = (SingleContact) getItem(position);
        if(currentP!=null)
        {
            viewHolder.contactname.setText(currentP.getName());
            viewHolder.number.setText(currentP.getNumber());
            viewHolder.emailid.setText(currentP.getEmailid());

        }
     /*   fetchContacts = new FetchContacts(Ccontext,_data,viewHolder);
        fetchContacts.execute(position+"");*/
        return v;
    }


    public Filter getFilter() {
        if (filter == null){
            filter  = new ListViewFilter();
        }
        return filter;
    }

    private class ListViewFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<SingleContact> filteredItems = new ArrayList<SingleContact>();
                SingleContact contact;
                for(int i = 0, l = Ccontactlist.size(); i < l; i++)
                {
                     contact = Ccontactlist.get(i);
                    //Toast.makeText(Ccontext,contact.getName().toString(),Toast.LENGTH_SHORT).show();
                    //Log.i("Toast",contact.getName());
                    if(contact.getName().toString().toLowerCase().startsWith(constraint.toString()))
                    {
                        Log.i("Toast", contact.getName() + " Constraint : " + constraint);
                        filteredItems.add(contact);
                    }

                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = Ccontactlist;
                    result.count = Ccontactlist.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            Updatedlist = (ArrayList<SingleContact>)results.values;
            notifyDataSetChanged();
            Ccontactlist.clear();
            for(int i = 0, l = Updatedlist.size(); i < l; i++)
                Ccontactlist.add(Updatedlist.get(i));
            notifyDataSetInvalidated();
        }
    }
    public void setData(ArrayList<SingleContact> contactCompleteModels) {
        this.Ccontactlist = contactCompleteModels;
        // this.mainContactList=contactCompleteModels;
        notifyDataSetChanged();
    }

}
