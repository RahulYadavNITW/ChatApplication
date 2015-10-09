package com.thenewboston.chatapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rahul on 08/10/15.
 */
public class Chat extends CustomActivity {
    private ArrayList<Conversation> conversationArrayList;
    private ChatAdapter chatAdapter;
    private EditText message;
    private String buddyName;
    private Date lstmsgdate;
    private Boolean isRunning;
    private static Handler handler;
    public static final String EXTRA_DATA ="com.thenewboston.chatapplication.ExtraData";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatscreen);
        conversationArrayList = new ArrayList<Conversation>();
        ListView listView = (ListView) findViewById(R.id.list);
        chatAdapter = new ChatAdapter();
        listView.setAdapter(chatAdapter);
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setStackFromBottom(true);

        message = (EditText) findViewById(R.id.chatInput);
        message.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

        setTouchAndClick(R.id.sendbutton);
        buddyName = getIntent().getStringExtra(EXTRA_DATA);
        getActionBar().setTitle(buddyName);

        handler = new Handler();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
        loadConversationList();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==R.id.sendbutton)
        {
            sendMessage();
        }
    }

    public void sendMessage()
    {
        if(message.length()==0)
            return;
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(message.getWindowToken(),0);

        String s = message.getText().toString();
        final Conversation c = new Conversation(s,new Date(), Userlist_LeftFragment.user.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
        conversationArrayList.add(c);
        chatAdapter.notifyDataSetChanged();
        message.setText(null);

        ParseObject po = new ParseObject("Chat");
        po.put("sender", Userlist_LeftFragment.user.getUsername());
        po.put("receiver",buddyName);
        po.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null)
                {
                    c.setStatus(Conversation.STATUS_DELIVERED_SEEN);
                }
                else
                    c.setStatus(Conversation.STATUS_SENT);
                chatAdapter.notifyDataSetChanged();
            }
        });



    }
    public void loadConversationList()
    {
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Chat");
        if(conversationArrayList.size()==0)
        {
            ArrayList<String> li = new ArrayList<String>();
            li.add(buddyName);
            li.add(Userlist_LeftFragment.user.getUsername());
            q.whereContainedIn("sender", li);
            q.whereContainedIn("receiver",li);
        }
        else
        {
            if(lstmsgdate !=null)
                q.whereGreaterThan("createdAt", lstmsgdate);
            q.whereEqualTo("sender",buddyName);
            q.whereEqualTo("receiver", Userlist_LeftFragment.user.getUsername());
        }
        q.orderByDescending("createdAt");
        q.setLimit(50);
        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects!=null &&objects.size()>0)
                {
                    for(int i=objects.size()-1;i>=0;i--)
                    {
                        ParseObject po = objects.get(i);
                        Conversation conversation = new Conversation(po.getString("message"),po.getCreatedAt(),po.getString("sender"));
                        conversationArrayList.add(conversation);
                        if(lstmsgdate ==null|| lstmsgdate.before(conversation.getLstmsgdate()))
                            lstmsgdate = conversation.getLstmsgdate();
                        chatAdapter.notifyDataSetChanged();
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(isRunning)
                            loadConversationList();
                    }
                },1000);
            }
        });

    }

    private class ChatAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return conversationArrayList.size();
        }

        @Override
        public Conversation getItem(int position) {
            return conversationArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Conversation conversation = getItem(position);
            if(conversation.isSent())
            {
                convertView =getLayoutInflater().inflate(R.layout.chat_item_send,null);
            }
            else
                convertView =getLayoutInflater().inflate(R.layout.chat_item_recieve,null);
            TextView txt = (TextView) findViewById(R.id.time);
            txt.setText(DateUtils.getRelativeDateTimeString(Chat.this, conversation.getLstmsgdate().getTime(),
                                                                DateUtils.SECOND_IN_MILLIS,DateUtils.DAY_IN_MILLIS,0));

            txt = (TextView) findViewById(R.id.message);
            txt.setText(conversation.getMessage());

            ImageView imageView1 = (ImageView) findViewById(R.id.status_Sent);
            ImageView imageView2 = (ImageView) findViewById(R.id.status_Delivered);
            ImageView imageView3 = (ImageView) findViewById(R.id.status_DeliveredandSeen);
            if(conversation.isSent())
            {
                if(conversation.getStatus()==Conversation.STATUS_SENDING)
                {
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                }
                else if(conversation.getStatus()==Conversation.STATUS_SENT)
                {
                    imageView2.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                }
                else if(conversation.getStatus()==Conversation.STATUS_DELIVERED_SEEN)
                {
                    imageView3.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                }
            }

            return convertView;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
