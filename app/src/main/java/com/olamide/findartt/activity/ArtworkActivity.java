package com.olamide.findartt.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.olamide.findartt.R;
import com.olamide.findartt.enums.ConnectionStatus;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.User;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
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

public class ArtworkActivity extends AppCompatActivity {

    private String accessToken;
    private User user;
    private Artwork artwork;
    private ArtworkSummary artworkSummary;


    private Call<FindArttResponse<ArtworkSummary>> responseCall;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork);

        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        accessToken = TempStorageUtils.readSharedPreferenceString(getApplicationContext(), ACCESS_TOKEN_STRING);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            artwork = extras.getParcelable(ARTWORK_STRING);
            user = extras.getParcelable(CURRENT_USER);
        }

        getArtSummary();


    }


    void getArtSummary() {
        ConnectionStatus connectionStatus = getConnectionStatus(getApplicationContext());
        if (connectionStatus.connectionStatus.equals(ConnectionStatus.NONE)) {
            ErrorUtils.handleInternetError(this, clRoot);

            return;
        }


        responseCall = FindArttService.getArtSummary(accessToken, artwork.getId());
        responseCall.enqueue(new Callback<FindArttResponse<ArtworkSummary>>() {

            @Override
            public void onResponse(Call<FindArttResponse<ArtworkSummary>> call, Response<FindArttResponse<ArtworkSummary>> response) {


                if (response.body() != null) {
                    FindArttResponse<ArtworkSummary> arttResponse = response.body();
                    ArtworkSummary artworkSummary = arttResponse.getData();
                    artworkSummary.getBids();


                } else {
                    ErrorUtils.handleApiError(response.errorBody(), getApplicationContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<ArtworkSummary>> call, Throwable t) {
                ErrorUtils.handleInternetError(getApplicationContext(), clRoot);
                Timber.e(t);
            }
        });


    }


}
