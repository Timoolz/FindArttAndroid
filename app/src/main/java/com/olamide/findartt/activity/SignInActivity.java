package com.olamide.findartt.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.fragment.LogInFragment;
import com.olamide.findartt.fragment.SignUpFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SignInActivity extends AppCompatActivity implements FragmentDataPasser {

    public final static String TYPE_STRING = "typeString";

    private String typeString = "";

    @BindView(R.id.sign_in_frame)
    FrameLayout signInFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());

        if(savedInstanceState != null){
            typeString = savedInstanceState.getString(TYPE_STRING);
        }else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            LogInFragment logInFragment = new LogInFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.sign_in_frame, logInFragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TYPE_STRING,typeString);
    }


    @Override
    public void onDataPass(Bundle bundle) {
        if (bundle!=null){
            Timber.e("hello " + bundle.getString(TYPE_STRING));
            typeString = bundle.getString(TYPE_STRING);
        }

    }

}
