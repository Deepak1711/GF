package com.example.deepak.greenframe1;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 22/7/16.
 */
public class DisplayActivity extends AppCompatActivity{
    private static AmazonS3Client sS3Client;
    ArrayList<URL> urls=new ArrayList<>();
    ViewPager viewPager;
    DisplayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.images_display);
        viewPager= (ViewPager) findViewById(R.id.view_Pager);
        sS3Client=Util.getS3Client(this);
        new GetFileListTask().execute();

    }

    private class GetFileListTask extends AsyncTask<Void, Void, Void> {
        // The list of objects we find in the S3 bucket
        private List<S3ObjectSummary> s3ObjList;
        // A dialog to let the user know we are retrieving the files
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(DisplayActivity.this,
                    getString(R.string.refreshing),
                    getString(R.string.please_wait));
        }

        @Override
        protected Void doInBackground(Void... inputs) {
            // Queries files in the bucket from S3.
            s3ObjList = sS3Client.listObjects(Constants.BUCKET_NAME).getObjectSummaries();

            for (S3ObjectSummary summary : s3ObjList) {
                urls.add(sS3Client.getUrl(Constants.BUCKET_NAME,summary.getKey()));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            adapter=new DisplayAdapter(getBaseContext(),urls);
            viewPager.setAdapter(adapter);
        }
    }
}

