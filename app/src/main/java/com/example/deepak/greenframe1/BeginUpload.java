package com.example.deepak.greenframe1;

import android.net.Uri;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;

/**
 * Created by deepak on 20/7/16.
 */
public interface BeginUpload {
    public TransferObserver Upload(Uri imageUri, TransferListener transferListener);
}
