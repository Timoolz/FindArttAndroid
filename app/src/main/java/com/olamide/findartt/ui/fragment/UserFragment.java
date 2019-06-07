package com.olamide.findartt.ui.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.olamide.findartt.R;
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserUpdate;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.GeneralUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.viewmodels.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;

import static com.olamide.findartt.AppConstants.FIREBASE_USER_IMAGE_PATH;
import static com.olamide.findartt.AppConstants.INTENT_ACTION_STRING;
import static com.olamide.findartt.AppConstants.RC_CAMERA;
import static com.olamide.findartt.AppConstants.RC_PHOTO_PICKER;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private HomeFragment.OnFragmentInteractionListener mListener;

    @Inject
    AppAuthUtil appAuthUtil;
    @Inject
    ConnectionUtils connectionUtils;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BindView(R.id.iv_avatar)
    ImageView avatarIv;

    @BindView(R.id.et_fname)
    EditText fnameText;

    @BindView(R.id.et_lname)
    EditText lnameText;

    @BindView(R.id.et_phone)
    EditText phoneText;

    @BindView(R.id.et_dob)
    EditText dobText;

    @BindView(R.id.sp_gender)
    Spinner genderSp;

    @BindView(R.id.et_address)
    EditText addressText;

    @BindView(R.id.et_country)
    EditText countryText;

    private UserResult userResult;
    private User currentUser;
    private UserUpdate userUpdate = new UserUpdate();
    private String downloadString;

    private FirebaseStorage mFirebaseStorage;
    private StorageReference storageReference;

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userResult = appAuthUtil.authorize();
        currentUser = userResult.getUser();
        initFirebaseStorage();
        if (getArguments() != null) {

        }
    }

    private void initFirebaseStorage() {
        mFirebaseStorage = FirebaseStorage.getInstance();
        storageReference = mFirebaseStorage.getReference().child(FIREBASE_USER_IMAGE_PATH);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, rootView);

        loadUi();
        return rootView;
    }

    private void loadUi() {
//        currentUser.setImageUrl("https://firebasestorage.googleapis.com/v0/b/findartt.appspot.com/o/bitmoji-20180722065326.png?alt=media&token=62fe50f7-d763-4c3e-9f40-3a9362eb95de");
        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            Picasso.with(getContext())
                    .load(currentUser.getImageUrl())
                    .transform(new RoundedTransformationBuilder().cornerRadiusDp((getResources().getDimension(R.dimen.avatar_dimen)) / 2).oval(false).build())
                    .placeholder(R.drawable.ic_avatar)
                    .error(R.drawable.ic_avatar)
                    .fit()
                    .into(avatarIv);
        }

        fnameText.setEnabled(false);
        fnameText.setText(currentUser.getFirstName());

        lnameText.setEnabled(false);
        lnameText.setText(currentUser.getLastName());

        phoneText.setEnabled(false);
        phoneText.setText(currentUser.getPhone());

        dobText.setEnabled(false);
        if (currentUser.getDateOfBirthEpoch() != null) {
            dobText.setText(GeneralUtils.dateFromNowFormat(currentUser.getDateOfBirthEpoch()));
        }

        genderSp.setEnabled(false);
        if (currentUser.getGender() == Gender.valueOf(getResources().getStringArray(R.array.gender)[0].toUpperCase())) {
            genderSp.setSelection(0);
        } else {
            genderSp.setSelection(1);
        }

        addressText.setEnabled(false);
        addressText.setText(currentUser.getAddress());

        countryText.setEnabled(false);
        countryText.setText(currentUser.getCountry());


    }


    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.iv_edit_avatar)
    void changeAvatar() {

        // setup the alert builder
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.image_picker, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(promptView);
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
        startActivityForResult(Intent.createChooser(intent, INTENT_ACTION_STRING), RC_PHOTO_PICKER);
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
            case RC_PHOTO_PICKER:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    StorageReference photoRef = storageReference.child(selectedImageUri.getLastPathSegment());
                    photoRef.putFile(selectedImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return photoRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadString = Objects.requireNonNull(downloadUri).toString();
                                userUpdate.setImageUrl(downloadString);
                                currentUser.setImageUrl(downloadString);
                                userResult.setUser(currentUser);
                                TempStorageUtils.storeActiveUser(getContext(), userResult);
                            } else {
                                ErrorUtils.handleUserError("Error uploading Image", getActivity(), UiUtils.getDummyFrame(getActivity()));
                            }
                        }
                    });


                }


                break;
            case RC_CAMERA:

                break;

            default:
                break;
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
