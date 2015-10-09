package com.thenewboston.chatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rahul on 08/10/15.
 */
public class Userlist_LeftFragment extends Fragment {
    private ArrayList<ParseUser> uList;
    public static  ParseUser user;
    public static final String ExtraData ="com.thenewboston.chatapplication.ExtraData";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.users_list,container, false );
       // getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        updateUserStatus(true);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updateUserStatus(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserList();
    }

    private void updateUserStatus(boolean online)
    {
        user.put("online", online);
        user.saveEventually();
        loadUserList();
    }

    private void loadUserList()
    {
        final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),null,"Please Wait, Loading...");
        Toast.makeText(getContext(),user.getObjectId()+"  "+ParseUser.getQuery(),Toast.LENGTH_SHORT).show();
        ParseUser.getQuery().whereNotEqualTo("objectId", user.getObjectId()).findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                progressDialog.dismiss();
                if (objects != null) {
                    if (objects.size() == 0) {
                        Toast.makeText(getActivity(), "No Users found", Toast.LENGTH_SHORT).show();
                        uList = new ArrayList<ParseUser>(objects);
                        ListView listView = (ListView) getView().findViewById(R.id.list);
                        listView.setAdapter(new UserAdapter());
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getActivity(), Chat.class).putExtra(ExtraData, uList.get(position).getUsername());
                                startActivity(intent);

                            }
                        });

                    }
                } else {
                    Utils.showDialog(getActivity(), "Error occured while finding users : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }
    public void newUser(ParseUser newuser)
    {
        newuser.put("online", true);
        newuser.saveEventually();
        loadUserList();
        return;
    }

    private class UserAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return uList.size();
        }

        @Override
        public ParseUser getItem(int position) {
            return uList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView=getActivity().getLayoutInflater().inflate(R.layout.users_info, null);

            ParseUser p = getItem(position);
            TextView tv=(TextView) convertView;
            tv.setText(p.getUsername());
            tv.setCompoundDrawablesRelativeWithIntrinsicBounds(p.getBoolean("online")?R.mipmap.ic_online
                                                               :R.mipmap.ic_offline,0,R.mipmap.ic_send,0);
            return convertView;
        }
    }

}
