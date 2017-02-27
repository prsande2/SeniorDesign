package com.rent_it_app.rent_it.json_models;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.rent_it_app.rent_it.views.ChatListFragment;

public class Chat implements Serializable
{

    @SerializedName("msg_id")
    @Expose
    private Integer msg_id;
    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("receiver")
    @Expose
    private String receiver;
    @SerializedName("sender")
    @Expose
    private String sender;
    @SerializedName("sent")
    @Expose
    private Boolean sent;
    @SerializedName("status")
    @Expose
    private Integer status = STATUS_SENT;
    private final static long serialVersionUID = -2224466915616109928L;

    public static final int STATUS_SENDING = 0;
    public static final int STATUS_SENT = 1;
    public static final int STATUS_FAILED = 2;

    /**
     * No args constructor for use in serialization
     *
     */
    public Chat() {
    }

    /**
     *
     * @param sender
     //* @param sent
     * @param receiver
     //* @param status
     //* @param msg_id
     * @param date
     * @param msg
     */
    public Chat(/*Integer msg_id, */Date date, String msg, String receiver, String sender) {
        super();
        //this.msg_id = msg_id;
        this.date = date;
        this.msg = msg;
        this.receiver = receiver;
        this.sender = sender;
        //this.sent = sent;
        //this.status = status;
    }

    public Integer getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(Integer msg_id) {
        this.msg_id = msg_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Boolean getSent() {
        return sent;
    }


    public void setSent(Boolean sent) {
        this.sent = sent;
    }

    /*public boolean isSent()
    {
        return ChatListFragment.user.getId().contentEquals(sender);
        //return ChatListFragment.myUser.getId().contentEquals(sender);
    }*/

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}