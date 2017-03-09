package com.rent_it_app.rent_it.json_models;

/**
 * Created by malhan on 3/8/17.
 */

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("item")
    @Expose
    private String item;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("author")
    @Expose
    private String author;
    private final static long serialVersionUID = -8583743193637410536L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Review() {
    }

    /**
     *
     * @param id
     * @param author
     * @param title
     * @param item
     * @param rating
     * @param comment
     */
    public Review(String id, String item, String title, String comment, Double rating, String author) {
        super();
        this.id = id;
        this.item = item;
        this.title = title;
        this.comment = comment;
        this.rating = rating;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
