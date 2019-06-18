package com.olamide.findartt.ui.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.olamide.findartt.R;
import com.olamide.findartt.models.User;
import com.olamide.findartt.utils.UiUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import butterknife.BindView;
//import retrofit2.MVResponse;


public class DashboardActivity extends BaseActivity {

    private User currentUser;
    private AppBarConfiguration appBarConfiguration;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView sideNavView;

    @BindView(R.id.drawer_root)
    DrawerLayout drawerLayout;

    NavHostFragment host;
    NavController navController;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setSupportActionBar(toolbar);

        host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        // Set up Action Bar
        navController = Objects.requireNonNull(host).getNavController();
        //appBarConfiguration = new AppBarConfiguration.Builder((navController.getGraph()))
        appBarConfiguration = new AppBarConfiguration
                .Builder(new HashSet<>(Arrays.asList(R.id.home_dest, R.id.user_dest)))
                .setDrawerLayout(drawerLayout)
                .build();


        setupActionBar(navController, appBarConfiguration);
        setupNavigationMenu(navController);


    }

    private void setupActionBar(NavController navController, AppBarConfiguration appBarConfiguration) {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }


    private void setupNavigationMenu(NavController navController) {
        NavigationUI.setupWithNavController(sideNavView, navController);

        currentUser = userResult.getUser();
        if (sideNavView.getHeaderCount() > 0) {
            View hView = sideNavView.getHeaderView(0);
            TextView tvEmail = (TextView) hView.findViewById(R.id.nav_email);
            tvEmail.setText(currentUser.getEmail());
            if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
                ImageView ivAvatar = (ImageView) hView.findViewById(R.id.nav_avatar);
                UiUtils.loadAvatarView(currentUser.getImageUrl(), ivAvatar, getApplicationContext());
            }
        }

        //
        // *TO CALL THE LOGOUT METHOD*
        // *AND TO PREVENT JETPACK NAVIGATION FROM HANDLING IT*
        //
        sideNavView.getMenu().findItem(R.id.log_out).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                appAuthUtil.logout();
                return true;
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        //return super.onSupportNavigateUp();
        //return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
        return NavigationUI.navigateUp(navController, appBarConfiguration);
    }


}
