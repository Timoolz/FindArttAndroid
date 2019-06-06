package com.olamide.findartt.ui.fragment;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.R;
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.GeneralUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.viewmodels.HomeViewModel;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;

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

        loadUi();
        return rootView;
    }

    private void loadUi() {

        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            Picasso.with(getContext())
                    .load(currentUser.getImageUrl())
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
        if(currentUser.getGender() == Gender.valueOf(getResources().getStringArray(R.array.gender)[0].toUpperCase())){
            genderSp.setSelection(0);
        }else{
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
