package com.olamide.findartt.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
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

import com.google.android.material.navigation.NavigationView;
import com.olamide.findartt.R;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.AppAuthUtil;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
//import retrofit2.MVResponse;
import dagger.android.AndroidInjection;

public class DashboardActivity extends AppCompatActivity {


    @Inject
    AppAuthUtil appAuthUtil;


    private AppBarConfiguration appBarConfiguration;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.nav_view)
    NavigationView sideNavView;

    @BindView(R.id.drawer_root)
    DrawerLayout drawerLayout;

    NavHostFragment host;
    NavController navController;

    private UserResult userResult;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        userResult = appAuthUtil.authorize();
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
        if (currentUser.getImageUrl() != null && !currentUser.getImageUrl().isEmpty()) {
            View hView = sideNavView.inflateHeaderView(R.layout.nav_header);
            ImageView ivAvatar = (ImageView) hView.findViewById(R.id.nav_avatar);
            Picasso.with(getApplicationContext())
                    .load(currentUser.getImageUrl())
                    .fit()
                    .into(ivAvatar);
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
