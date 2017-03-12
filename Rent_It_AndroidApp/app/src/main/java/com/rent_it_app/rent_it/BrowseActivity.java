package com.rent_it_app.rent_it;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Category;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;
import com.rent_it_app.rent_it.views.AvailabeItemFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malhan on 3/7/17.
 */

public class BrowseActivity extends BaseActivity{

    private Category myCategory;
    private String category_id;
    private String category_name;
    private ArrayList<Item> brwsList;
    private ArrayList<File> photoList;
    private ListView blist;
    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    Gson gson;

    private CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoSyncManager syncClient;
    private AmazonS3 s3;
    private TransferUtility transferUtility;
    private File imageFile;
    private HashMap<Integer, File> imageHash;
    private HashMap<Integer, ImageView> imageViewHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        photoList = new ArrayList<>();

        imageHash = new HashMap<>();
        imageViewHash = new HashMap<>();

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),  // getApplicationContext(),
                Constants.COGNITO_POOL_ID, // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_WEST_2, // Region
                credentialsProvider);

        s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());

        /*Intent intent = getIntent();
        String value = intent.getStringExtra("key");*/
        myCategory = (Category) getIntent().getSerializableExtra(Config.EXTRA_DATA);
        category_id = myCategory.getId();
        category_name = myCategory.getName();
        blist = (ListView) findViewById(R.id.browselist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle(category_name);

        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);

        brwsList = new ArrayList<Item>();
        //Call<ArrayList<Item>> call = itemEndpoint.getItems();
        Call<ArrayList<Item>> call = itemEndpoint.getItemsByCategory(category_id);
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                int statusCode = response.code();
                //List<Item> items = response.body();
                brwsList = response.body();

                /*for(Item item : brwsList) {
                    // load the bitmapList with the images
                    File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
                    try {
                        File imageFile = File.createTempFile(item.getImage(), "", outputDir);
                        imageFile.deleteOnExit();
                        TransferObserver transferObserver =
                                transferUtility.download(Constants.BUCKET_NAME, item.getImage(), imageFile);
                        photoList.add(imageFile);
                        Log.d("pos:id ", pos + ":" + transferObserver.getId());
                        transferObserver.setTransferListener(new TransferListener() {
                            @Override
                            public void onStateChanged(int id, TransferState state) {
                                if(state == TransferState.COMPLETED) {
                                    //myPhoto.setImageResource(R.drawable.bg);
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imageHash.get(id).getAbsolutePath());
                                    imageViewHash.get(id).setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 150, false));
                                    Log.d("onStateChanged.id:", ""+id);
                                }
                            }

                            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                                // Do something in the callback.
                            }

                            public void onError(int id, Exception e) {
                                // Do something in the callback.
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/


                //tv1.setText(sb.toString());
                blist.setAdapter(new BrowseAdapter());
                blist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3) {
                        //startActivity(new Intent(this, EditItemActivity.class).putExtra(Config.EXTRA_DATA, brwsList.get(pos)));
                        Intent myIntent = new Intent(BrowseActivity.this, ListingActivity.class);
                        myIntent.putExtra(Config.MORE_DATA, brwsList.get(pos));
                        BrowseActivity.this.startActivity(myIntent);
                    }
                });

                Log.d("retrofit.call.enqueue", ""+statusCode);
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
            }
        });
    }
    private class BrowseAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() { return brwsList.size(); }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Item getItem(int arg0)
        {
            return brwsList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem_id(int)
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
                v = getLayoutInflater().inflate(R.layout.listing_item, arg2, false);

            Item c = getItem(pos);

            /*TextView lbl = (TextView) v;
            lbl.setText(c.getTitle());*/
            LinearLayout ll = (LinearLayout) v; // get the parent layout view
            TextView title = (TextView) ll.findViewById(R.id.txtTitle); // get the child text view
            TextView city = (TextView) ll.findViewById(R.id.txtCity);
            TextView rate = (TextView) ll.findViewById(R.id.txtRate);
            ImageView photo = (ImageView) ll.findViewById(R.id.photo);
            title.setText(c.getTitle());
            city.setText(c.getCity());
            rate.setText("$" + c.getRate() + " /day");
            File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer

            try {
                File imageFile = File.createTempFile(c.getImage(), "", outputDir);
                imageFile.deleteOnExit();
                TransferObserver transferObserver =
                        transferUtility.download(Constants.BUCKET_NAME, c.getImage(), imageFile);
                imageHash.put(transferObserver.getId(), imageFile);
                imageViewHash.put(transferObserver.getId(), photo);
                Log.d("pos:id ", pos + ":" + transferObserver.getId());
                transferObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if(state == TransferState.COMPLETED) {
                            //myPhoto.setImageResource(R.drawable.bg);
                            Bitmap myBitmap = BitmapFactory.decodeFile(imageHash.get(id).getAbsolutePath());
                            imageViewHash.get(id).setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 150, false));
                            Log.d("onStateChanged.id:", ""+id);
                        }
                    }

                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                        // Do something in the callback.
                    }

                    public void onError(int id, Exception e) {
                        // Do something in the callback.
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }

            return v;
        }

    }

}
