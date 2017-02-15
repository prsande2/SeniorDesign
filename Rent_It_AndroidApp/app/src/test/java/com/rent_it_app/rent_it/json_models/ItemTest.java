package com.rent_it_app.rent_it.json_models;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

/**
 * Created by Nagoya on 2/14/17.
 */
public class ItemTest {
    @Test
    public void getId() throws Exception {
        //given
        final Item item = new Item();
        final Field field = item.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(item, "an_id");

        //when
        final String result = item.getId();

        //then
        assertEquals("field wasn't retrieved properly", result, "an_id");
    }

    @Test
    public void setId() throws Exception {
        //given
        final Item item = new Item();

        //when
        item.setId("an_id");

        //then
        final Field field = item.getClass().getDeclaredField("id");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(item), "an_id");
    }

    @Test
    public void getUid() throws Exception {

    }

    @Test
    public void setUid() throws Exception {

    }

    @Test
    public void getTitle() throws Exception {

    }

    @Test
    public void setTitle() throws Exception {

    }

    @Test
    public void getDescription() throws Exception {

    }

    @Test
    public void setDescription() throws Exception {

    }

    @Test
    public void getCondition() throws Exception {

    }

    @Test
    public void setCondition() throws Exception {

    }

    @Test
    public void getLocation() throws Exception {

    }

    @Test
    public void setLocation() throws Exception {

    }

    @Test
    public void getCity() throws Exception {

    }

    @Test
    public void setCity() throws Exception {

    }

    @Test
    public void getZipcode() throws Exception {

    }

    @Test
    public void setZipcode() throws Exception {

    }

    @Test
    public void getCategory() throws Exception {

    }

    @Test
    public void setCategory() throws Exception {

    }

    @Test
    public void getTags() throws Exception {

    }

    @Test
    public void setTags() throws Exception {

    }

    @Test
    public void getValue() throws Exception {

    }

    @Test
    public void setValue() throws Exception {

    }

    @Test
    public void getRate() throws Exception {

    }

    @Test
    public void setRate() throws Exception {

    }

    @Test
    public void getImage() throws Exception {

    }

    @Test
    public void setImage() throws Exception {

    }

    @Test
    public void getVisible() throws Exception {

    }

    @Test
    public void setVisible() throws Exception {

    }

}