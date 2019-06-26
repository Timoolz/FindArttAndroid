package com.olamide.findartt.ui.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.R;
import com.olamide.findartt.viewmodels.factory.ViewModelFactory;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.utils.network.FirebaseUtil;

import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragment extends DaggerFragment {

    @Inject
    protected ViewModelFactory viewModelFactory;

    @Inject
    protected AppAuthUtil appAuthUtil;

    protected UserResult userResult;
    @Inject
    protected ConnectionUtils connectionUtils;

    protected ViewGroup dummyFrame;

    protected ProgressDialog progressDialog;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    FirebaseUtil firebaseUtil;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(this instanceof LogInFragment) && !(this instanceof SignUpFragment)) {
            userResult = appAuthUtil.authorize();
        }
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(getActivity()));
        progressDialog = UiUtils.getProgressDialog(getContext(), getString(R.string.loading), false);
    }
}
