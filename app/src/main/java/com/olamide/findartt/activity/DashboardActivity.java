package com.olamide.findartt.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.DashboardViewModel;
import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.R;
import com.olamide.findartt.SignUpViewModel;
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.adapter.ArtworkAdapter;
import com.olamide.findartt.enums.ConnectionStatus;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.AuthUtil;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.RecyclerViewUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttRepository;
import com.olamide.findartt.viewmodels.MainViewModel;
//import com.olamide.findartt.widget.ArttUpdateService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.MVResponse;
import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.ARTWORK_STRING;
import static com.olamide.findartt.Constants.BUNDLE;
import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.utils.network.ConnectionUtils.getConnectionStatus;

public class DashboardActivity extends AppCompatActivity implements ArtworkAdapter.ArtworkAdapterOnClickListener {


    @Inject
    ViewModelFactory viewModelFactory;


    AuthUtil authUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    DashboardViewModel dashboardViewModel;

    private  UserResult userResult;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

//    @BindView((R.id.toolbar))
//    Toolbar toolbar;

    @BindView(R.id.rv_artwork)
    RecyclerView artworkRv;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    private List<Artwork> artworkList;
    private ArtworkAdapter mAdapter;
    StaggeredGridLayoutManager layoutManager;

    //To store the Recycler view Current state
    private Parcelable savedRecyclerLayoutState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        ((FindArttApplication) getApplication()).getAppComponent().doInjection(this);
        authUtil = new AuthUtil(this);
        userResult = authUtil.authorize();
        if(userResult==null){
            authUtil.logout();
            return;
        }

        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        dashboardViewModel.getArtWorkResponse().observe(this, this::displayUi);

        int spanCount = RecyclerViewUtils.getSpanCount(artworkRv, 450);
        layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        artworkRv.setLayoutManager(layoutManager);

        if (savedInstanceState == null) {
            mAdapter = new ArtworkAdapter(artworkList, getApplicationContext(), this);
            mAdapter.setArtworkList(artworkList);
            Objects.requireNonNull(artworkRv.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);
            artworkRv.setNestedScrollingEnabled(false);
            artworkRv.setAdapter(mAdapter);

        }
        loadArtWorks();


    }

    private void displayUi(MVResponse mvResponse) {

        switch (mvResponse.status) {

            case LOADING:
                loadingPb.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                loadingPb.setVisibility(View.INVISIBLE);
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                     artworkList = objectMapper.convertValue(arttResponse.getData(), new TypeReference<List<Artwork>>() {
                     });
                    if (artworkList.size() <= 0) {
                        showEmptyMessage();
                    } else {
                        mAdapter.setArtworkList(artworkList);
                    }

                } catch (Exception e) {
                    Timber.e(e);
                    ErrorUtils.handleError((this), clRoot);

                }
                break;

            case ERROR:
                loadingPb.setVisibility(View.INVISIBLE);
                ErrorUtils.handleThrowable(mvResponse.error, this, clRoot);
                break;

            default:
                loadingPb.setVisibility(View.INVISIBLE);
                break;
        }
    }


    void loadArtWorks() {
        ConnectionStatus connectionStatus = getConnectionStatus(getApplicationContext());
        if (connectionStatus.connectionStatus.equals(ConnectionStatus.NONE)) {
            ErrorUtils.handleInternetError(this, clRoot);
            return;
        }
        dashboardViewModel.findArtworks(userResult.getTokenInfo().getAccessToken());

    }

//    private void getFavouriteArt() {
//        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        viewModel.getArtworks().observe(this, new Observer<List<Artwork>>() {
//            @Override
//            public void onChanged(@Nullable List<Artwork> favArt) {
//                Timber.d("Updating list of artworks from LiveData in ViewModel");
//                artworkList = favArt;
//
//                if (artworkList.size() <= 0) {
//                    showEmptyMessage();
//                } else {
//                    mAdapter.setArtworkList(favArt);
//                }
//
//                startArttWidgetService();
//            }
//        });
//    }

    void showEmptyMessage() {
        tvEmpty.setVisibility(View.VISIBLE);
        artworkRv.setVisibility(View.INVISIBLE);
        loadingPb.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onClickListener(Artwork artwork) {

//        Intent intent = new Intent(getApplicationContext(), ArtworkActivity.class);
//        intent.putExtra(ARTWORK_STRING, artwork);
//        intent.putExtra(CURRENT_USER, user);
//        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.all:

//                currentPage = 1;
//                currentCategory = new SortType(SortType.TOP_RATED);

                loadArtWorks();
                return true;
            case R.id.favourite:

//                currentCategory = new SortType(SortType.FAVOURITE);
//                getFavouriteArt();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    void startArttWidgetService() {
//        Intent i = new Intent(getApplicationContext(), ArttUpdateService.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(ARTWORK_STRING, (ArrayList<? extends Parcelable>) artworkList);
//        i.putExtra(BUNDLE, bundle);
//        i.setAction(ArttUpdateService.ACTION_UPDATE_ARTT_WIDGET);
//        startService(i);
//    }

}
