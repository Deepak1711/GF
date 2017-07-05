package com.example.deepak.greenframe1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.model.CannedAccessControlList;

import java.util.Observable;

/**
 * Created on 20/7/16.
 */
public class UploadInformation{
    TransferObserver transferObserver;
    UploadProgress uploadProgress;
    Uri selectedUris;
    TransferListener transferListener = new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {uploadProgress.progressUpdate();}

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d("Deep", String.format("onProgressChangedDeepak: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
            uploadProgress.progressUpdate();
        }

        @Override
        public void onError(int id, Exception ex) {
        }
    };
}
