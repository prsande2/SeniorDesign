package com.rent_it_app.rent_it.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;
import com.rent_it_app.rent_it.ChatActivity;
import com.rent_it_app.rent_it.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.chatt.demo.custom.CustomActivity;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.ChatUser;
//import com.chatt.demo.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.rent_it_app.rent_it.testing.MsgActivity;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    /** Users database reference */
    private DatabaseReference mFirebaseDatabaseReference;

    /** The Chat list. */
    private ArrayList<ChatUser> uList;

    /** The user. */
    public static ChatUser user;

    public ListView list;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CHAT LIST");

        list = (ListView) view.findViewById(R.id.list);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();


        return view;
    }

    /* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    public void onResume()
    {
        super.onResume();
        loadChatList();

    }

    private void loadChatList()
    {
        final ProgressDialog dia = ProgressDialog.show(getActivity(), null,
                "Loading...");

        // Pull the users list once no sync required.
        mFirebaseDatabaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {dia.dismiss();
                long size  = dataSnapshot.getChildrenCount();
                if(size == 0) {
                    Toast.makeText(getActivity(), "No chat found", Toast.LENGTH_SHORT).show();
                    return;
                }
                uList = new ArrayList<ChatUser>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatUser user = ds.getValue(ChatUser.class);
                    Logger.getLogger(ChatListFragment.class.getName()).log(Level.ALL,user.getUsername());
                    Log.d("Test","user.getId(): "+user.getId());
                    Log.d("Test","user.getUsername(): "+user.getUsername());
                    if(!user.getId().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        uList.add(user);
                }
                //ListView list = (ListView) view.findViewById(R.id.list);
                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3)
                    {
                        startActivity(new Intent(getActivity(), ChatActivity.class)
                                .putExtra(Config.EXTRA_DATA,  uList.get(pos)));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * The Class UserAdapter is the adapter class for User ListView. This
     * adapter shows the user name and it's only online status for each item.
     */
    private class UserAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return uList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public ChatUser getItem(int arg0)
        {
            return uList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getActivity().getLayoutInflater().inflate(R.layout.chat_item, arg2, false);

            ChatUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            /*lbl.setCompoundDrawablesWithIntrinsicBounds(
                    c.isOnline() ? R.drawable.ic_online
                            : R.drawable.ic_offline, 0, R.drawable.arrow, 0);*/

            return v;
        }

    }

}
