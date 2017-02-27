package com.rent_it_app.rent_it.json_models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.rent_it_app.rent_it.views.ChatListFragment;

/**
 * Created by Nagoya on 2/16/17.
 */

public class ChatMessage {

    private String id;
    private String msg;
    private String name;
    private String sender;
    private String receiver;
    private String photoUrl;
    //private String messageTime;
    private Date date;

    /** The status. */
    private int status = STATUS_SENT;


    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;

    public ChatMessage() {
    }

    public ChatMessage(String msg, Date date, String name, String sender, String receiver, String photoUrl) {

        this.msg = msg;
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.photoUrl = photoUrl;
        this.date = date;
        /*Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a,MMM dd");
        this.messageTime = sdf.format(cal.getTime());*/
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /*public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
*/
    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getReceiver()
    {
        return receiver;
    }

    public void setReceiver(String receiver)
    {
        this.receiver = receiver;
    }

    public String getSender()
    {
        return sender;
    }

    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public boolean isSent()
    {
        //return ChatListFragment.user.getId().contentEquals(sender);
        return ChatListFragment.myUser.getUid().contentEquals(sender);
    }

    public Date getDate() { return date;}

    public void setDate(Date date)
    {
        this.date = date;
    }

}

