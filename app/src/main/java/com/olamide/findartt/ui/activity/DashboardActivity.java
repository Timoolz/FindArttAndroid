package com.olamide.findartt.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.olamide.findartt.R;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;
//import com.olamide.findartt.widget.ArttUpdateService;

import javax.inject.Inject;

import butterknife.ButterKnife;
//import retrofit2.MVResponse;
import dagger.android.AndroidInjection;

public class DashboardActivity extends AppCompatActivity   {



    @Inject
    AppAuthUtil appAuthUtil;

    private UserResult userResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        userResult = appAuthUtil.authorize();



    }


}
