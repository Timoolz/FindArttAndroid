package com.olamide.findartt.ui.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.olamide.findartt.R;
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserUpdate;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.ui.activity.ImagePickerActivity;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.GeneralUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.viewmodels.UserViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.olamide.findartt.AppConstants.IMAGE_URI_PATH;
import static com.olamide.findartt.AppConstants.RC_IMAGE_PICKER;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends BaseFragment {

    private HomeFragment.OnFragmentInteractionListener mListener;




    UserViewModel userViewModel;
    ViewGroup dummyFrame;

    @BindView(R.id.iv_avatar)
    ImageView avatarIv;

    @BindView(R.id.iv_edit_avatar)
    ImageView editAvatarIv;

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

    @BindView(R.id.bt_save)
    Button btSave;

    ProgressDialog progressDialog;

    private UserResult userResult;
    private User currentUser;
    private UserUpdate userUpdate = new UserUpdate();
    private String downloadString;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userResult = appAuthUtil.authorize();
        currentUser = userResult.getUser();
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, rootView);
        progressDialog = UiUtils.getProgressDialog(getContext(), getString(R.string.loading), false);
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(getActivity()));


        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
        userViewModel.getUpdateResponse().observe(this, this::consumeResponse);
        loadUi();
        return rootView;
    }

    private void consumeResponse(MVResponse mvResponse) {

        switch (mvResponse.status) {

            case LOADING:
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                    currentUser = objectMapper.convertValue(arttResponse.getData(), User.class);
                    userResult.setUser(currentUser);
                    TempStorageUtils.storeActiveUser(getContext(), userResult);
                    loadUi();
                    UiUtils.showSuccessSnack("Changes have been saved", getContext(), dummyFrame);

                } catch (Exception e) {
                    Timber.e(e);
                    ErrorUtils.handleError((Objects.requireNonNull(getContext())), dummyFrame);
                }
                break;

            case ERROR:
                progressDialog.dismiss();
                ErrorUtils.handleThrowable(mvResponse.error, getContext(), dummyFrame);
                break;

            default:
                break;
        }

    }

    private void loadUi() {


        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            fillImageView(currentUser.getImageUrl(), true);
        }

        fnameText.setText(currentUser.getFirstName());
        lnameText.setText(currentUser.getLastName());
        phoneText.setText(currentUser.getPhone());

        if (currentUser.getDateOfBirthEpoch() != null) {
            dobText.setText(GeneralUtils.dateFromNowFormat(currentUser.getDateOfBirthEpoch()));
        }

        if (currentUser.getGender() == Gender.valueOf(getResources().getStringArray(R.array.gender)[0].toUpperCase())) {
            genderSp.setSelection(0);
        } else {
            genderSp.setSelection(1);
        }
        addressText.setText(currentUser.getAddress());
        countryText.setText(currentUser.getCountry());


    }

    private void fillImageView(String imageUrl, boolean fromServer) {
        UiUtils.loadAvatarView(imageUrl, avatarIv, getContext());
        if (fromServer) {
            Activity rootActivity = getActivity();
            NavigationView sideNavView = rootActivity.findViewById(R.id.nav_view);
            if (sideNavView.getHeaderCount() > 0) {
                View hView = sideNavView.getHeaderView(0);
                ImageView ivAvatar = hView.findViewById(R.id.nav_avatar);
                UiUtils.loadAvatarView(imageUrl, ivAvatar, getContext());


            }
        }

    }

//    private void setViewAbility(boolean ability) {
//        editAvatarIv.setVisibility(ability ? View.VISIBLE : View.INVISIBLE);
//        fnameText.setEnabled(ability);
//        lnameText.setEnabled(ability);
//        phoneText.setEnabled(ability);
//        dobText.setEnabled(ability);
//        genderSp.setEnabled(ability);
//        addressText.setEnabled(ability);
//        countryText.setEnabled(ability);
//        btSave.setEnabled(ability);
//
//    }


    @Override
    public void onAttach(Context context) {
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
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        startActivityForResult(intent, RC_IMAGE_PICKER);
    }

    @OnClick(R.id.bt_save)
    void saveChanges() {


        updateUserUpdate();
        if ((!updateValid(userUpdate))) {
            ErrorUtils.handleUserError(getString(R.string.generic_form_validation), Objects.requireNonNull(getContext()), dummyFrame);
        } else {
            if (connectionUtils.handleNoInternet(getActivity())) {
                if (connectionUtils.handleNoInternet(getActivity())) {
                    userViewModel.updateUser(userResult.getTokenInfo().getAccessToken(), userUpdate);
                }
            }

        }


    }

    void updateUserUpdate() {

        userUpdate.setImageUrl(downloadString);
        userUpdate.setFirstName(fnameText.getText().toString());
        userUpdate.setLastName(lnameText.getText().toString());
        userUpdate.setDateOfBirth(dobText.getText().toString());
        userUpdate.setPhone(phoneText.getText().toString());
        userUpdate.setGender(Gender.valueOf(genderSp.getSelectedItem().toString().toUpperCase()));
        userUpdate.setCountry(countryText.getText().toString());
        userUpdate.setAddress(addressText.getText().toString());
        //temporary
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");
//        signup.setDateOfBirth(simpleDateFormat.format(new Date()));
//        signup.setDateOfBirth("1900-01-01T09:04:21.798Z");
    }

    private boolean updateValid(UserUpdate userUpdate1) {
        if ( userUpdate1.getFirstName().trim().isEmpty()
                || userUpdate1.getLastName().trim().isEmpty()
                || userUpdate1.getPhone().trim().isEmpty()
                || userUpdate1.getCountry().trim().isEmpty()

        ) {
            return false;
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_IMAGE_PICKER:
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImageUri = data.getParcelableExtra(IMAGE_URI_PATH);
                    firebaseUtil.storeUserImage(selectedImageUri, new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadString = Objects.requireNonNull(downloadUri).toString();
                                userUpdate.setImageUrl(downloadString);
                                fillImageView(downloadString, false);

                            } else {
                                ErrorUtils.handleUserError("Error uploading Image", getActivity(), UiUtils.getDummyFrame(getActivity()));
                            }
                        }
                    });
                }

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
