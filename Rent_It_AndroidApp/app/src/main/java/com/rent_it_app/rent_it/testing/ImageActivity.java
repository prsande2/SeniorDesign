package com.rent_it_app.rent_it.testing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.rent_it_app.rent_it.BaseActivity;
import com.rent_it_app.rent_it.R;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Miz on 2/10/17.
 * https://www.numetriclabz.com/integrate-amazon-s3-to-android-tutorial/
 */

public class ImageActivity extends BaseActivity {

    File fileToUpload = new File("/storage/emulated/0/1486645429743.jpg");
    File fileToDownload = new File("1486645429743.jpg");
    AmazonS3 s3;
    TransferUtility transferUtility;
    ImageView myImage;
    /*File outputDir = this.getCacheDir();
    File imageFile = File.createTempFile("prefix", "extension", outputDir);
*/
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        myImage = (ImageView) findViewById(R.id.myImage);

    }


    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                getString(R.string.COGNITO_POOL_ID), // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    /**
     *  Create a AmazonS3Client constructor and pass the credentialsProvider.
     * @param credentialsProvider
     */
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_WEST_2));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    /**
     * This method is used to upload the file to S3 by using TransferUtility class
     * @param view
     */
    public void setFileToUpload(View view){

        TransferObserver transferObserver = transferUtility.upload(
                getString(R.string.BUCKET_NAME),     /* The bucket to upload to */
                fileToUpload.getName(),       /* The key for the uploaded object */
        fileToUpload       /* The file where the data to upload exists */
        );
        transferObserverListener(transferObserver, fileToUpload);
    }

    /**
     *  This method is used to Download the file to S3 by using transferUtility class
     * @param view
     **/
    public void setFileToDownload(View view){

        File outputDir = getApplicationContext().getCacheDir();
        File imageFile;
        try{
            imageFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg", outputDir);
            TransferObserver transferObserver = transferUtility.download(
                    getString(R.string.BUCKET_NAME),     /* The bucket to download from */
                    fileToDownload.getName(),    /* The key for the object to download */
                    imageFile        /* The file to download the object to */
            );
            transferObserverListener(transferObserver, imageFile);

            /*Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            myImage.setImageBitmap(bitmap);

            Log.d("bmp height x width: ", bitmap.getHeight() + " x " + bitmap.getWidth());*/
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }


    }

    /**
     * This is listener method of the TransferObserver
     * Within this listener method, we get status of uploading and downloading file,
     * to display percentage of the part of file to be uploaded or downloaded to S3
     * It displays an error, when there is a problem in  uploading or downloading file to or from S3.
     * @param transferObserver
     */

    public void transferObserverListener(TransferObserver transferObserver, final File file){


        transferObserver.setTransferListener(new TransferListener(){


            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
                if(state == TransferState.COMPLETED){
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    myImage.setImageBitmap(bitmap);

                    Log.d("bmp height x width: ", bitmap.getHeight() + " x " + bitmap.getWidth());
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = bytesTotal > 0 ? (int) (bytesCurrent/bytesTotal * 100) : 0;
                Log.e("percentage",percentage + "");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error: " + ex);
            }

        });
    }



}
