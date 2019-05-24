package com.olamide.findartt.ui.activity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.olamide.findartt.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;


public class SignInActivity extends AppCompatActivity {


    @BindView(R.id.drawer_root)
    DrawerLayout drawerLayout;
    NavHostFragment host;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.pre_auth_nav_host_fragment);
        navController = Objects.requireNonNull(host).getNavController();


    }

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//
//    }


}
