package com.example.deepak.greenframe1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.services.s3.internal.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created on 20/7/16.
 */
public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.MyViewHolder> implements UploadProgress {
    private ImageLoader imageLoader;
    private LayoutInflater inflator;
    List<UploadInformation> data = new ArrayList<>();
    Context ctx;
    BeginUpload beginUpload;


    public UploadAdapter(Context ctx, BeginUpload beginUpload, List<UploadInformation> data) {
        inflator = LayoutInflater.from(ctx);
        imageLoader = ImageLoader.getInstance();
        this.data = data;
        this.ctx = ctx;
        this.beginUpload = beginUpload;

        for (UploadInformation imageObject : data) {
            imageObject.transferObserver = beginUpload.Upload(imageObject.selectedUris, imageObject.transferListener);
            imageObject.uploadProgress = this;
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.adapter_upload, parent, false);
        final MyViewHolder holder = new MyViewHolder(view, this);
        return holder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        UploadInformation current = data.get(position);
        holder.initializeUpload(holder);
        imageLoader.displayImage(current.selectedUris.toString(), holder.currentImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void progressUpdate() {
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private final ProgressBar progressBar;
        ImageView currentImage;
        TextView progressText;

        public MyViewHolder(View itemView, UploadAdapter uploadAdapter) {
            super(itemView);
            currentImage = (ImageView) itemView.findViewById(R.id.imageView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            progressText = (TextView) itemView.findViewById(R.id.progress);
        }

        public void initializeUpload(MyViewHolder holder) {
            UploadInformation uploadInformation = data.get(getLayoutPosition());
            switch (uploadInformation.transferObserver.getState()) {
                case IN_PROGRESS:
                case RESUMED_WAITING:
                case WAITING:
                case PAUSED:
                case WAITING_FOR_NETWORK:
                case PENDING_CANCEL:
                case PENDING_PAUSE:
                case PENDING_NETWORK_DISCONNECT:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case COMPLETED:
                    new setObjectAcl().execute(uploadInformation);
                default:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
            int progress = (int) ((double) uploadInformation.transferObserver.getBytesTransferred() * 100 / uploadInformation.transferObserver.getBytesTotal());
            holder.progressText.setText(progress + "%");

        }
    }
    private class setObjectAcl extends AsyncTask<UploadInformation,Void,Void> {
        @Override
        protected Void doInBackground(UploadInformation...  uploadInformation) {
            Util.getS3Client(ctx).setObjectAcl(Constants.BUCKET_NAME, uploadInformation[0].transferObserver.getKey(), CannedAccessControlList.PublicRead);
            return null;
        }
    }
}
