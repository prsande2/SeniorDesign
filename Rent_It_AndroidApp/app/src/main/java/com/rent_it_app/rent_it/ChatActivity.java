package com.rent_it_app.rent_it;

/**
 * Created by Nagoya on 2/21/17.
 */
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.json_models.Chat;
import com.rent_it_app.rent_it.json_models.ChatUser;
import com.rent_it_app.rent_it.json_models.ChatMessage;
import com.rent_it_app.rent_it.firebase.Config;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rent_it_app.rent_it.json_models.Conversation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ChatActivity extends BaseActivity {
    /**
     * The Conversation list.
     */
    //private ArrayList<ChatMessage> convList;
    private ArrayList<Chat> msgList;

    /**
     * The chat adapter.
     */
    private ChatAdapter adp;

    /**
     * The Editext to compose the message.
     */
    private EditText txt;

    /**
     * The user name of buddy.
     */
    private ChatUser buddy;
    private String buddyId;
    private Conversation myConversation;
    private Chat myChat;

    /**
     * The date of last message in conversation.
     */
    private Date lastMsgDate;

    private String rental_id;

    private FirebaseUser myUser;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        myConversation = (Conversation) getIntent().getSerializableExtra(Config.EXTRA_DATA);
        rental_id = myConversation.getRental_id();

        //convList = new ArrayList<Chat>();
        msgList = (ArrayList) myConversation.getChat();
        ListView list = (ListView) findViewById(R.id.list);
        adp = new ChatAdapter();
        list.setAdapter(adp);
        list.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        list.setStackFromBottom(true);

        txt = (EditText) findViewById(R.id.txt);
        txt.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_FLAG_MULTI_LINE);

        setTouchNClick(R.id.btnSend);

        //buddy = new ChatUser("Uhph7zwnAyQnSqHnw0uWbHI6zpv1");
        //buddy = (ChatUser) getIntent().getSerializableExtra(Config.EXTRA_DATA);




        myUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("Test","my uid: "+myUser.getUid());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(myUser.getUid().contentEquals(myConversation.getRenter())) {
            buddyId = myConversation.getOwner();
            Log.d("Test","owner: "+myConversation.getOwner());
            this.getSupportActionBar().setTitle(buddyId);
        }else{
            buddyId = myConversation.getRenter();
            Log.d("Test","renter: "+myConversation.getRenter());
            this.getSupportActionBar().setTitle(buddyId);
        }

    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadConversationList();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onPause()
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /* (non-Javadoc)
     * @see com.socialshare.custom.CustomFragment#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btnSend) {
            sendMessage();
        }

    }

    /**
     * Call this method to Send message to opponent. It does nothing if the text
     * is empty otherwise it creates a Parse object for Chat message and send it
     * to Parse server.
     */
    private void sendMessage() {
        if (txt.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(txt.getWindowToken(), 0);

        String s = txt.getText().toString();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            //final ChatMessage conversation = new ChatMessage(s,
            //        Calendar.getInstance().getTime(), "Mimi",
            //        user.getUid(), buddy.getId(),
            //        "");
            final Chat c = new Chat(Calendar.getInstance().getTime(), s, buddyId, user.getUid());
            //c.setStatus(ChatMessage.STATUS_SENDING);
            c.setStatus(Chat.STATUS_SENDING);
            //convList.add(conversation);
            msgList.add(c);
            /*final String key = FirebaseDatabase.getInstance()
                    .getReference("messages")
                    .push().getKey();*/
            /*final String key = FirebaseDatabase.getInstance()
                    .getReference("conversations")
                    .push().getKey();*/
            //FirebaseDatabase.getInstance().getReference("messages").child(key)
            FirebaseDatabase.getInstance().getReference("conversations").child(rental_id).child("chat")
                    .setValue(msgList)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if (task.isSuccessful()) {
                                                       msgList.get(msgList.indexOf(c)).setStatus(Chat.STATUS_SENT);
                                                   } else {
                                                       msgList.get(msgList.indexOf(c)).setStatus(Chat.STATUS_FAILED);
                                                   }
                                                   FirebaseDatabase.getInstance()
                                                           .getReference("conversations")
                                                           .child(rental_id).child("chat").setValue(msgList)
                                                           .addOnCompleteListener(new
                                                                                          OnCompleteListener<Void>() {
                                                                                              @Override
                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                  adp.notifyDataSetChanged();
                                                                                              }
                                                                                          });

                                               }
                                           }
                    );
        }
        adp.notifyDataSetChanged();
        txt.setText(null);
    }

    /**
     * Load the conversation list from Parse server and save the date of last
     * message that will be used to load only recent new messages
     */
    private void loadConversationList() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("conversations").child(rental_id);

        //FirebaseDatabase.getInstance().getReference("messages").addListenerForSingleValueEvent(new ValueEventListener() {
        //FirebaseDatabase.getInstance().getReference("conversations").addListenerForSingleValueEvent(new ValueEventListener() {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    myConversation = dataSnapshot.getValue(Conversation.class);
                    //for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        //ChatMessage conversation = ds.getValue(ChatMessage.class);
                        //Conversation tmpConversation = ds.getValue(Conversation.class);
                        //myChat = ds.getValue(Chat.class);

                        //if (conversation.getReceiver().contentEquals(user.getUid()) || conversation.getSender().contentEquals(user.getUid())) {
                        /*if (tmpConversation.getRental_id().contentEquals(rental_id)) {
                            //convList.add(conversation);
                            myConversation = tmpConversation;

                            msgList = (ArrayList) myConversation.getChat();
                            //if (lastMsgDate == null
                            //        || lastMsgDate.before(tmpConversation.getDate()))
                            //    lastMsgDate = tmpConversation.getDate();

                            adp.notifyDataSetChanged();

                        }*/
                        adp.notifyDataSetChanged();
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * The Class ChatAdapter is the adapter class for Chat ListView. This
     * adapter shows the Sent or Receieved Chat message in each list item.
     */
    private class ChatAdapter extends BaseAdapter {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() {
            return msgList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Chat getItem(int arg0) {
            return msgList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem_id(int)
         */
        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @SuppressLint("InflateParams")
        @Override
        public View getView(int pos, View v, ViewGroup arg2) {
            Chat c = getItem(pos);
            //if (c.getSent())
            if(c.getSender().contentEquals(myUser.getUid())) {
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, arg2, false);
            }else {
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, arg2, false);
            }

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            //if (c.getSent()) {
            if(c.getSender().contentEquals(myUser.getUid())) {
                if (c.getStatus() == Chat.STATUS_SENT)
                    lbl.setText("Delivered");
                else {
                    if (c.getStatus() == Chat.STATUS_SENDING)
                        lbl.setText("Sending...");
                    else {
                        lbl.setText("Failed");
                    }
                }
            } else
                lbl.setText("");

            return v;
        }

    }

    /* (non-Javadoc)
     * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
