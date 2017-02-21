package com.rent_it_app.rent_it.json_models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Nagoya on 2/16/17.
 */

public class ChatMessage {

    private String id;
    private String text;
    private String name;
    private String sender;
    private String receiver;
    private String photoUrl;
    private String messageTime;
    private Date date;


    /*public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;*/

    public ChatMessage() {
    }

    public ChatMessage(String text, Date date, String name, String sender, String receiver, String photoUrl) {

        this.text = text;
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.photoUrl = photoUrl;
        this.date = date;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a,MMM dd");
        this.messageTime = sdf.format(cal.getTime());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}

