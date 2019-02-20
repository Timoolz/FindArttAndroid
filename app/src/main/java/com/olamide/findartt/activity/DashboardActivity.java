package com.olamide.findartt.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.olamide.findartt.Constants;
import com.olamide.findartt.R;
import com.olamide.findartt.adapter.ArtworkAdapter;
import com.olamide.findartt.enums.ConnectionStatus;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.User;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.RecyclerViewUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.ARTWORK_STRING;
import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.utils.network.ConnectionUtils.getConnectionStatus;

public class DashboardActivity extends AppCompatActivity implements ArtworkAdapter.ArtworkAdapterOnClickListener{

    private String accessToken;
    private User user;

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


    private Call<FindArttResponse<List<Artwork>>> responseCall;

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
        Timber.plant(new Timber.DebugTree());
        accessToken = TempStorageUtils.readSharedPreferenceString(getApplicationContext(), ACCESS_TOKEN_STRING);

        int spanCount = RecyclerViewUtils.getSpanCount(artworkRv, 450);
        layoutManager = new StaggeredGridLayoutManager( spanCount, StaggeredGridLayoutManager.VERTICAL);
        artworkRv.setLayoutManager(layoutManager);

        if (savedInstanceState == null) {
            loadArtWorks();

            mAdapter = new ArtworkAdapter(artworkList,  getApplicationContext(), this);
            mAdapter.setArtworkList(artworkList);
            artworkRv.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

            artworkRv.setNestedScrollingEnabled(false);
            artworkRv.setAdapter(mAdapter);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                user = extras.getParcelable(CURRENT_USER);
            }
        }


    }


    void loadArtWorks() {
        ConnectionStatus connectionStatus = getConnectionStatus(getApplicationContext());
        if (connectionStatus.connectionStatus.equals(ConnectionStatus.NONE)) {
            ErrorUtils.handleInternetError(this, clRoot);

            return;
        }

        loadingPb.setVisibility(View.VISIBLE);

        responseCall = FindArttService.findArt(accessToken);
        responseCall.enqueue(new Callback<FindArttResponse<List<Artwork>>>() {

            @Override
            public void onResponse(Call<FindArttResponse<List<Artwork>>> call, Response<FindArttResponse<List<Artwork>>> response) {
                loadingPb.setVisibility(View.INVISIBLE);


                if (response.body() != null) {
                    FindArttResponse<List<Artwork>> arttResponse = response.body();
                    List<Artwork> artworkList = arttResponse.getData();
                    if (artworkList.size() <= 0) {
                        showEmptyMessage();
                    } else {

                        mAdapter.setArtworkList(artworkList);
                        //UiUtils.showSuccessSnack("Successful Login. Welcome " + userResult.getUser().getName(), getContext(), clRoot);
                    }

                } else {
                    ErrorUtils.handleApiError(response.errorBody(), getApplicationContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<List<Artwork>>> call, Throwable t) {
                loadingPb.setVisibility(View.INVISIBLE);
                ErrorUtils.handleInternetError(getApplicationContext(), clRoot);
                Timber.e(t);
            }
        });


    }

    void showEmptyMessage() {
        tvEmpty.setVisibility(View.VISIBLE);
        artworkRv.setVisibility(View.INVISIBLE);
        loadingPb.setVisibility(View.INVISIBLE);

    }


    @Override
    public void onClickListener(Artwork artwork) {

        Intent intent = new Intent(getApplicationContext(), ArtworkActivity.class);
        intent.putExtra(ARTWORK_STRING,artwork);
        intent.putExtra(CURRENT_USER,user);
        startActivity(intent);
    }
}
