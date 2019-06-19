package com.olamide.findartt.ui.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ViewGroup;


import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.R;
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.utils.network.FirebaseUtil;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    protected AppAuthUtil appAuthUtil;
    protected UserResult userResult;

    @Inject
    protected ViewModelFactory viewModelFactory;

    @Inject
    protected ConnectionUtils connectionUtils;

    protected ViewGroup dummyFrame;

    protected ProgressDialog progressDialog;
    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    FirebaseUtil firebaseUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this instanceof SignInActivity)) {
            userResult = appAuthUtil.authorize();
        }
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(this));
        progressDialog = UiUtils.getProgressDialog(getApplicationContext(), getString(R.string.loading), false);
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


}
