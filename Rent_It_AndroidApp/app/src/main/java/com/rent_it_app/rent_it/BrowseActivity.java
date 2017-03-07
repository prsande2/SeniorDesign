package com.rent_it_app.rent_it;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

/**
 * Created by malhan on 3/7/17.
 */

public class BrowseActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
    }

}
