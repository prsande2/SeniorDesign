package com.rent_it_app.rent_it.views;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rent_it_app.rent_it.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

//import com.chatt.demo.custom.CustomActivity;
import com.rent_it_app.rent_it.json_models.ChatUser;
//import com.chatt.demo.utils.Const;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {

    /** Users database reference */
    DatabaseReference database;
    /** The Chat list. */
    private ArrayList<ChatUser> uList;

    /** The user. */
    public static ChatUser user;

    private ListView list;


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("CHAT LIST");

        list = (ListView) view.findViewById(R.id.list);


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
        database.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    if(!user.getId().contentEquals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                        uList.add(user);
                }

                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3)
                    {
                        startActivity(new Intent(UserList.this,
                                Chat.class).putExtra(
                                Const.EXTRA_DATA,  uList.get(pos)));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
