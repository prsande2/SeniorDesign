package com.rent_it_app.rent_it.views;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.HomeActivity;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListItemFragment extends Fragment {

    private Spinner spinner1;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;
    private static final String TAG = HomeActivity.class.getName();

    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    EditText txtTitle, txtDescription, txtCondition, txtCategory, txtZipcode;
    EditText txtTags, txtValue, txtRate, txtCity;
    private TextView myStatusText;
    private FirebaseAuth mAuth;
    private String userId;
    Gson gson;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;

    File photo_destination;
    String imgS3Name;

    public ListItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("LIST ITEM");

        retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.REST_API_BASE_URL))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);

        gson = new Gson();

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),  // getApplicationContext(),
                getString(R.string.COGNITO_POOL_ID), // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getContext(),
                Regions.US_WEST_2, // Region
                credentialsProvider);

        //Define
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        txtTitle = (EditText)view.findViewById(R.id.title);
        txtDescription = (EditText)view.findViewById(R.id.description);
        txtCondition = (EditText)view.findViewById(R.id.condition);
        txtZipcode = (EditText)view.findViewById(R.id.zipcode);
        txtCity = (EditText)view.findViewById(R.id.city);
        txtTags = (EditText)view.findViewById(R.id.tags);
        txtValue = (EditText)view.findViewById(R.id.value);
        txtValue.setFilters(new InputFilter[] {
                //https://gist.github.com/gaara87/3607765
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtValue.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }

                }
        });

        txtRate = (EditText)view.findViewById(R.id.rate);
        //txtRate.addTextChangedListener(new NumberTextWatcher(txtRate, "#,###"));
        txtRate.setFilters(new InputFilter[] {
                //https://gist.github.com/gaara87/3607765
                new DigitsKeyListener(Boolean.FALSE, Boolean.TRUE) {
                    int beforeDecimal = 5, afterDecimal = 2;

                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String temp = txtRate.getText() + source.toString();

                        if (temp.equals(".")) {
                            return "0.";
                        }
                        else if (temp.toString().indexOf(".") == -1) {
                            // no decimal point placed yet
                            if (temp.length() > beforeDecimal) {
                                return "";
                            }
                        } else {
                            temp = temp.substring(temp.indexOf(".") + 1);
                            if (temp.length() > afterDecimal) {
                                return "";
                            }
                        }

                        return super.filter(source, start, end, dest, dstart, dend);
                    }

                }

        });



        final Button listButton = (Button) view.findViewById(R.id.list_button);
        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);


        //getuid
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid().toString();
        //Button - List
        listButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                // Create a record in a dataset and synchronize with the server
                /*Dataset dataset = syncClient.openOrCreateDataset("myDataset");
                dataset.put("myKey", "myValue");
                dataset.synchronize(new DefaultSyncCallback() {
                    @Override
                    public void onSuccess(Dataset dataset, List newRecords) {
                        //Your handler code here
                    }
                });*/

                //Toast.makeText(getActivity(), spinner1.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                //post Item
                imgS3Name = UUID.randomUUID().toString()+".jpg";

                Item listing_item = new Item();
                listing_item.setUid(userId);
                listing_item.setTitle(txtTitle.getText().toString());
                listing_item.setDescription(txtDescription.getText().toString());
                listing_item.setCondition(txtCondition.getText().toString());
                listing_item.setCity(txtCity.getText().toString());
                listing_item.setZipcode(Integer.parseInt(txtZipcode.getText().toString()));
                String myTags = txtTags.getText().toString();
                List<String> tags = Arrays.asList(myTags.split("\\s*,\\s*"));
                listing_item.setTags(tags);
                listing_item.setValue(Double.parseDouble(txtValue.getText().toString()));
                listing_item.setRate(Double.parseDouble(txtRate.getText().toString()));

                if(photo_destination != null) {
                    listing_item.setImage(imgS3Name);
                }

                /*if(listing_item != null)
                {
                    Log.d("item.getUid:", listing_item.getUid());
                    Log.d("item.getTitle:", listing_item.getTitle());
                    Log.d("item.tags null?", "" + (listing_item.getTags() == null));
                    Log.d("item.tags empty?", "" + listing_item.getTags().isEmpty());
                    for(String t: listing_item.getTags())
                    {
                        Log.d("item.tag null?", "" + (t == null));
                        Log.d("item.tag is: ", t);
                    }
                    String itemString = gson.toJson(listing_item);
                    Log.d("gson'ed Item: ", itemString);
                }*/

                Call<Item> call = itemEndpoint.addItem(listing_item);
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        int statusCode = response.code();

                        Log.d("retrofit.call.enqueue", ""+statusCode);

                        //Log.d("photo_dest!=null?", photo_destination.toString());
                        if(photo_destination != null)
                        {
                            Log.d("photo_destination!=null", ""+(photo_destination!=null));
                            AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                            TransferUtility transferUtility = new TransferUtility(s3, getContext());
                            TransferObserver observer = transferUtility.upload(
                                    getString(R.string.BUCKET_NAME),
                                    imgS3Name,
                                    photo_destination
                            );
                        }
                        Toast.makeText(getActivity(),"Sucessfully Created New Listing",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Log.d("retrofit.call.enqueue", t.toString());
                    }

                });
                //end of post item
            }
        });
        //Button - Image
        Button imageButton = (Button) view.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });
        ivImage = (ImageView) view.findViewById(R.id.preview);
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        photo_destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            photo_destination.createNewFile();
            fo = new FileOutputStream(photo_destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Log.d("photo_des.createNewFile", "success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.d("photo_dest!=null?", photo_destination.toString());

        //Rsizing the image
        Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 900, 600, true);

        /*ivImage.setImageBitmap(thumbnail);*/
        ivImage.setImageBitmap(resized);

        Log.d(TAG, "Height: " + resized.getHeight() + " & Width: " + resized.getWidth());
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        photo_destination = new File(getRealPathFromURI(getContext(), getImageUri(getContext(), bm)));

        ivImage.setImageBitmap(bm);
        Log.d("PATH",""+ photo_destination);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Context inContext, Uri uri) {
        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}
