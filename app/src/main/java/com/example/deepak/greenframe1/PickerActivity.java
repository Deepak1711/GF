package com.example.deepak.greenframe1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.deepak.greenframe1.Presenter.PickerPresenter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created on 18/7/16.
 */
public class PickerActivity extends AppCompatActivity implements View.OnClickListener {
    private File files[];
    private final String[] okFileExtensions = new String[]{"jpg"};
    Button upload;
    ViewPager viewPager;
    PickerAdapter adapter;
    Uri uri[];
    PickerPresenter pickerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        pickerPresenter=new PickerPresenter();
        File dirpath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + getResources().getString(R.string.relative_directory_path));
        if (dirpath.isDirectory()) {
            files = dirpath.listFiles();

        } else {
            Toast.makeText(this, "Directory does not exist", Toast.LENGTH_LONG).show();
            finish();
        }
        if(files != null) {
            uri = new Uri[files.length];
            int k = 0;
            for (int i = 0; i < files.length; i++) {
                if (pickerPresenter.accept(files[i], okFileExtensions)) {
                    uri[k] = Uri.fromFile(files[i]);
                    k++;
                }
            }
            upload = (Button) findViewById(R.id.upload);
            viewPager = (ViewPager) findViewById(R.id.view_Pager);
            adapter = new PickerAdapter(this, uri);
            viewPager.setAdapter(adapter);

            upload.setOnClickListener(this);
        }
        else{
            Toast.makeText(this, "There are no images to select", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onClick(View v) {
        if (Util.isNetworkAvailable(this)) {
            ArrayList<Uri> uris;
            int selected[] = adapter.getSelected();
            uris=pickerPresenter.getSelectedUris(uri,selected);
            if (!uris.isEmpty() && uris.size() <= 10) {
                Intent intent = new Intent(this, UploadActivity.class);
                intent.putParcelableArrayListExtra("Selected Images", uris);
                startActivity(intent);
            } else if (uris.isEmpty()) {
                Toast.makeText(this, "Select atleast one image", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Not more than 10 images can be uploaded at one time", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Make Sure you are connected to internet", Toast.LENGTH_LONG).show();
        }
    }
}
