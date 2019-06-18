package com.olamide.findartt.ui.activity;

import android.os.Bundle;
import android.os.PersistableBundle;


import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;

import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @Inject
    protected AppAuthUtil appAuthUtil;
    protected UserResult userResult;

    @Inject
    ViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(this instanceof SignInActivity)) {
            userResult = appAuthUtil.authorize();
        }
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }


}
