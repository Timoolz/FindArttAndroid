package com.olamide.findartt.activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.fragment.LogInFragment;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.utils.TempStorageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import timber.log.Timber;

import static com.olamide.findartt.AppConstants.TYPE_STRING;
import static com.olamide.findartt.AppConstants.USEREMAIL_STRING;
import static com.olamide.findartt.AppConstants.USERPASSWORD_STRING;

public class SignInActivity extends AppCompatActivity implements FragmentDataPasser {


    private String typeString = "";

    @BindView(R.id.sign_in_frame)
    FrameLayout signInFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            typeString = savedInstanceState.getString(TYPE_STRING);
        } else {

            UserLogin userLogin = TempStorageUtils.getUserLogin(getApplicationContext());
            FragmentManager fragmentManager = getSupportFragmentManager();
            LogInFragment logInFragment = new LogInFragment();

            if (!userLogin.getEmail().isEmpty() && !userLogin.getPassword().isEmpty()) {
                Bundle bundle = new Bundle();
                bundle.putString(USEREMAIL_STRING, userLogin.getEmail());
                bundle.putString(USERPASSWORD_STRING, userLogin.getPassword());
                logInFragment.setArguments(bundle);
            }


            fragmentManager.beginTransaction()
                    .add(R.id.sign_in_frame, logInFragment)
                    .commit();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TYPE_STRING, typeString);
    }


    @Override
    public void onDataPass(Bundle bundle) {
        if (bundle != null) {
            Timber.e("hello %s", bundle.getString(TYPE_STRING));
            typeString = bundle.getString(TYPE_STRING);
        }

    }

}
