package com.olamide.findartt.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.R;
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.Buy;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.viewmodels.ArtworkViewModel;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.AndroidSupportInjection;


import timber.log.Timber;


import static com.olamide.findartt.AppConstants.ARTWORK_STRING;
import static com.olamide.findartt.AppConstants.CURRENT_USER;


public class BuyFragment extends BaseFragment {




    private ArtworkSummary artworkSummary;

    private ArtworkViewModel artworkViewModel;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.tv_sold)
    TextView tvSold;

    @BindView(R.id.bt_buy)
    Button btBuy;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;



    //To ensure it doesnt recur while getting the summary
    private boolean newCheck = false;


    private OnFragmentInteractionListener mListener;

    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artworkViewModel = ViewModelProviders.of(Objects.requireNonNull(getParentFragment()), viewModelFactory).get(ArtworkViewModel.class);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
        ButterKnife.bind(this, rootView);
        artworkViewModel.getBuyResponse().observe(this, this::handleBuy);

        if (getArguments() != null) {
            artworkSummary = getArguments().getParcelable(ARTWORK_STRING);
        }
        loadUi();

        return rootView;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    void loadUi() {
        tvAmount.setText(artworkSummary.getMinimumAmount().toString());
        if (artworkSummary.getCurrentBuy() != null) {
            btBuy.setVisibility(View.INVISIBLE);
            tvSold.setVisibility(View.VISIBLE);
        } else {
            btBuy.setVisibility(View.VISIBLE);
            tvSold.setVisibility(View.INVISIBLE);
        }
    }

    private void handleBuy(MVResponse mvResponse) {
        switch (mvResponse.status) {

            case LOADING:
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                    Buy completedBuy = objectMapper.convertValue(arttResponse.getData(), Buy.class);
                    UiUtils.showSuccessSnack("Successful Purchase. Welcome ", getContext(), clRoot);
                    if(newCheck){
                        newCheck = false;
                        artworkViewModel.findArtSummary(userResult.getTokenInfo().getAccessToken(), completedBuy.getArtworkId());
                    }


                } catch (Exception e) {
                    Timber.e(e);
                    ErrorUtils.handleError((Objects.requireNonNull(getActivity())), clRoot);
                }
                break;

            case ERROR:
                progressDialog.dismiss();
                ErrorUtils.handleThrowable(mvResponse.error, getActivity(), clRoot);
                break;

            default:
                break;
        }

    }


    @OnClick(R.id.bt_buy)
    void attemptPurchase() {

        Buy buy = new Buy();
        buy.setArtworkId(artworkSummary.getId());

        if (connectionUtils.handleNoInternet(getActivity())) {
            newCheck = true;
            artworkViewModel.buyArt(userResult.getTokenInfo().getAccessToken(), buy);
        }

    }
}
