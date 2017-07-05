package com.example.deepak.greenframe1;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 19/7/16.
 */
public class UploadActivity extends AppCompatActivity implements BeginUpload{
    private TransferUtility transferUtility;
    ArrayList<Uri> uris=new ArrayList<>();
    String selectedImages[];
    int length = 0;
    RecyclerView recyclerView;
    UploadAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        uris = getIntent().getParcelableArrayListExtra("Selected Images");
        selectedImages = new String[uris.size()];
        for (Uri t : uris) {
            try {
                selectedImages[length] = getPath(t);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            length++;
        }
        transferUtility = Util.getTransferUtility(this);
        recyclerView=(RecyclerView) findViewById(R.id.drawerlist);
        adapter=new UploadAdapter(this,this,getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public List<UploadInformation> getData(){
        List<UploadInformation> data=new ArrayList<>();
        for(int i=0;i<uris.size();i++)
        {
            UploadInformation current=new UploadInformation();
            current.selectedUris=uris.get(i);
            data.add(current);
        }
        return data;
    }

    @Override
    public TransferObserver Upload(Uri imageUri, TransferListener transferListener) {
        String imagePath = "";
        try {
            imagePath = getPath(imageUri);
            File resizedFile=resize(imagePath);


        if (imagePath == null) {
            Toast.makeText(this, "Could not find the filepath of the selected file",
                    Toast.LENGTH_LONG).show();
        } else {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            TransferObserver observer = transferUtility.upload(Constants.BUCKET_NAME, "PartyZing_"+ts+"_"+resizedFile.getName(), resizedFile);
            observer.setTransferListener(transferListener);
            return observer;
        }
    }catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File resize(String imagePath){
        File file = new File(imagePath);
        File imageStorageDir = new File(Environment.getExternalStorageDirectory()+"/PartyZing");
        Bitmap bMap= BitmapFactory.decodeFile(imagePath);
        Bitmap out = Bitmap.createScaledBitmap(bMap, 500, 500, false);
        File resizedFile = new File(imageStorageDir, file.getName());

        OutputStream fOut=null;
        try {
            fOut = new BufferedOutputStream(new FileOutputStream(resizedFile));
            out.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();
            bMap.recycle();
            out.recycle();

        } catch (Exception e) { // TODO

        }
        return resizedFile;
    }

    @SuppressLint("NewApi")
    private String getPath(Uri uri) throws URISyntaxException {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
