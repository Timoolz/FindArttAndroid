package com.olamide.findartt.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Parcelable;
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
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.adapter.ArtworkAdapter;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.AppAuthUtil;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.RecyclerViewUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
//import com.olamide.findartt.widget.ArttUpdateService;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
//import retrofit2.MVResponse;
import dagger.android.AndroidInjection;
import timber.log.Timber;

public class DashboardActivity extends AppCompatActivity implements ArtworkAdapter.ArtworkAdapterOnClickListener {


    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    AppAuthUtil appAuthUtil;
    @Inject
    ConnectionUtils connectionUtils;

    private final ObjectMapper objectMapper = new ObjectMapper();

    DashboardViewModel dashboardViewModel;

    private UserResult userResult;

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
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        userResult = appAuthUtil.authorize();


        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        dashboardViewModel.getArtWorkResponse().observe(this, this::displayUi);

        int spanCount = RecyclerViewUtils.getSpanCount(artworkRv, 450);
        layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        artworkRv.setLayoutManager(layoutManager);


        mAdapter = new ArtworkAdapter(artworkList, getApplicationContext(), this);
        mAdapter.setArtworkList(artworkList);
        Objects.requireNonNull(artworkRv.getLayoutManager()).onRestoreInstanceState(savedRecyclerLayoutState);
        artworkRv.setNestedScrollingEnabled(false);
        artworkRv.setAdapter(mAdapter);


        if (savedInstanceState == null) {
            loadArtWorks();
        }


    }

    private void displayUi(MVResponse mvResponse) {

        switch (mvResponse.status) {

            case LOADING:
                loadingPb.setVisibility(View.VISIBLE);
                break;
            case SUCCESS:
                displayUi();
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
                    try{
                        //Local data doesn't come in Api Response
                        artworkList = objectMapper.convertValue(mvResponse.data, new TypeReference<List<Artwork>>() {
                        });
                        if (artworkList.size() <= 0) {
                            showEmptyMessage();
                        } else {
                            mAdapter.setArtworkList(artworkList);
                        }
                    }catch (Exception ee){
                        Timber.e(ee);
                        ErrorUtils.handleError((this), clRoot);
                    }


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
        if(connectionUtils.handleNoInternet(this)){
            dashboardViewModel.findArtworks(userResult.getTokenInfo().getAccessToken());
        }

    }

    private void getFavouriteArt() {
        dashboardViewModel.findFavouriteArtworks();
    }


    void showEmptyMessage() {
        tvEmpty.setVisibility(View.VISIBLE);
        artworkRv.setVisibility(View.INVISIBLE);
        loadingPb.setVisibility(View.INVISIBLE);

    }
    void displayUi() {
        tvEmpty.setVisibility(View.INVISIBLE);
        artworkRv.setVisibility(View.VISIBLE);
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
                getFavouriteArt();
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
