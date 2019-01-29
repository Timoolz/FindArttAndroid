package com.olamide.findartt.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.olamide.findartt.R;
import com.olamide.findartt.fragment.LogInFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {

    @BindView(R.id.sign_in_frame)
    FrameLayout signInFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        LogInFragment logInFragment = new LogInFragment();
        fragmentManager.beginTransaction()
                .add(R.id.sign_in_frame, logInFragment)
                .commit();

    }
}
