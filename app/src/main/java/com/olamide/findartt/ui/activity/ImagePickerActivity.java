package com.olamide.findartt.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;

import com.olamide.findartt.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import timber.log.Timber;

import static com.olamide.findartt.AppConstants.IMAGE_URI_PATH;
import static com.olamide.findartt.AppConstants.INTENT_ACTION_STRING;
import static com.olamide.findartt.AppConstants.RC_CAMERA;
import static com.olamide.findartt.AppConstants.RC_GALLERY;

public class ImagePickerActivity extends AppCompatActivity {

    private int ASPECT_RATIO_X = 16, ASPECT_RATIO_Y = 16, bitmapMaxWidth = 1000, bitmapMaxHeight = 1000;
    private int IMAGE_COMPRESSION = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        // setup the alert builder
        LayoutInflater layoutInflater = LayoutInflater.from(ImagePickerActivity.this);
        View promptView = layoutInflater.inflate(R.layout.image_picker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(ImagePickerActivity.this);
        builder.setView(promptView);
        builder.setCancelable(true);
        builder.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        setResultCancelled();
                    }
                }
        );
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        promptView.findViewById(R.id.camera_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
                dialog.dismiss();
            }
        });

        promptView.findViewById(R.id.gallery_picker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchGallery();
                dialog.dismiss();
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, true);
        startActivityForResult(Intent.createChooser(intent, INTENT_ACTION_STRING), RC_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RC_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    cropImage(selectedImageUri);

                } else {
                    setResultCancelled();
                }

                break;
            case RC_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();

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

    private void cropImage(Uri sourceUri) {
        Uri destinationUri = Uri.fromFile(new File(getCacheDir(), queryName(getContentResolver(), sourceUri)));
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(IMAGE_COMPRESSION);

//        // applying UI theme
//        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary));

        //if (lockAspectRatio)
        options.withAspectRatio(ASPECT_RATIO_X, ASPECT_RATIO_Y);

        //if (setBitmapMaxWidthHeight)
        options.withMaxResultSize(bitmapMaxWidth, bitmapMaxHeight);

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
        finish();
    }

    private void setResultCancelled() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }


}
