package com.example.deepak.greenframe1.Presenter;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created on 3/8/16.
 */
public class PickerPresenter {
    public boolean accept(File file,String okFileExtensions[]) {
        for (String extension : okFileExtensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Uri> getSelectedUris(Uri uri[],int selected[]){
        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < uri.length; i++) {
            if (selected[i] == 1) {
                uris.add(uri[i]);
            }
        }
        return uris;
    }
}
