package com.olamide.findartt.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
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
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.olamide.findartt.R;
import com.olamide.findartt.utils.ImageUtils;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.olamide.findartt.AppConstants.IMAGE_URI_PATH;
import static com.olamide.findartt.AppConstants.INTENT_ACTION_STRING;
import static com.olamide.findartt.AppConstants.RC_CAMERA;
import static com.olamide.findartt.AppConstants.RC_GALLERY;

public class ImagePickerActivity extends AppCompatActivity {


    public static String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        // setup the alert builder
        View promptView = LayoutInflater.from(ImagePickerActivity.this).inflate(R.layout.image_picker, null);
        BottomSheetDialog bottomDialog = new BottomSheetDialog(this);
        bottomDialog.setContentView(promptView);
        bottomDialog.setCancelable(true);
        bottomDialog.setOnCancelListener(
                dialog -> setResultCancelled()
        );
        bottomDialog.show();

        promptView.findViewById(R.id.camera_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                bottomDialog.dismiss();
            }
        });

        promptView.findViewById(R.id.gallery_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGallery();
                bottomDialog.dismiss();
            }
        });
    }


    void launchGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, INTENT_ACTION_STRING), RC_GALLERY);
    }

    void launchCamera() {

        fileName = System.currentTimeMillis() + ".jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getCacheImagePath(fileName));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(Intent.createChooser(intent, INTENT_ACTION_STRING), RC_CAMERA);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    cropImage(selectedImageUri, ImageUtils.defaultUcropOptions(true));

                } else {
                    setResultCancelled();
                }

                break;
            case RC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = getCacheImagePath(fileName);
                    cropImage(selectedImageUri, ImageUtils.defaultUcropOptions(true));

                } else {
                    setResultCancelled();
                }

                break;


            case UCrop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    handleUCropResult(data);
                } else {
                    setResultCancelled();
                }
                break;

            case UCrop.RESULT_ERROR:
                final Throwable cropError = UCrop.getError(data);
                Timber.e(cropError, "Crop error: ");
                setResultCancelled();
                break;
            default:
                setResultCancelled();
                break;


        }

    }

    private void cropImage(Uri sourceUri, UCrop.Options options) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));

        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(this);
    }


    private static String queryName(ContentResolver resolver, Uri uri) {
        Cursor returnCursor =
                resolver.query(uri, null, null, null, null);
        assert returnCursor != null;
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String name = returnCursor.getString(nameIndex);
        returnCursor.close();
        return name;
    }


    private void handleUCropResult(Intent data) {
        if (data == null) {
            setResultCancelled();
            return;
        }
        final Uri resultUri = UCrop.getOutput(data);
        setResultOk(resultUri);
    }

    private void setResultOk(Uri imagePath) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_URI_PATH, imagePath);
        setResult(Activity.RESULT_OK, intent);
        clearCache(this);
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }


    private Uri getCacheImagePath(String fileName) {
        File path = new File(getExternalCacheDir(), "camera");
        if (!path.exists()) path.mkdirs();
        File image = new File(path, fileName);
        return getUriForFile(ImagePickerActivity.this, getPackageName() + ".provider", image);
    }

    /**
     * Calling this will delete the images from cache directory
     * useful to clear some memory
     */
    private static void clearCache(Context context) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (path.exists() && path.isDirectory()) {
            for (File child : path.listFiles()) {
                child.delete();
            }
        }
    }


}
