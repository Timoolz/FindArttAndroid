package com.olamide.findartt.utils.network;

import android.app.Application;
import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;

import java.util.Objects;

import javax.inject.Inject;

import static com.olamide.findartt.AppConstants.FIREBASE_ARTWORK_IMAGE_PATH;
import static com.olamide.findartt.AppConstants.FIREBASE_ARTWORK_VIDEO_PATH;
import static com.olamide.findartt.AppConstants.FIREBASE_USER_IMAGE_PATH;

public final class FirebaseUtil {

    private Application application;
    private Context context;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference storageReference;
    private String downloadString = null;

    @Inject
    public FirebaseUtil(Application application) {
        this.application = application;
        this.context = application;
        mFirebaseStorage = FirebaseStorage.getInstance();

    }

//    public FirebaseUtil(Context context) {
//        this.context = context;
//    }

    public void storeUserImage(Uri uri, OnCompleteListener<Uri> completeListener) {
        initFirebaseReference(FIREBASE_USER_IMAGE_PATH);
         saveUri(uri,completeListener );

    }

    public void storeArtworkImage(Uri uri, OnCompleteListener<Uri> completeListener) {
        initFirebaseReference(FIREBASE_ARTWORK_IMAGE_PATH);
         saveUri(uri, completeListener);
    }

    public void storeArtworkVideo(Uri uri, OnCompleteListener<Uri> completeListener) {
        initFirebaseReference(FIREBASE_ARTWORK_VIDEO_PATH);
         saveUri(uri, completeListener);
    }

    private void saveUri(@NonNull Uri uri, OnCompleteListener<Uri> completeListener) {

        StorageReference fileRef = storageReference.child(Objects.requireNonNull(uri.getLastPathSegment()));
        fileRef.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(completeListener);

    }

    private void initFirebaseReference(String path) {
        storageReference = mFirebaseStorage.getReference().child(path);
    }
}
