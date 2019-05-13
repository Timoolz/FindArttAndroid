package com.olamide.findartt.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.olamide.findartt.R;
import com.olamide.findartt.ViewModelFactory;
import com.olamide.findartt.adapter.BidAdapter;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.Bid;

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


public class BidFragment extends Fragment {


    @Inject
    ViewModelFactory viewModelFactory;

    ArtworkViewModel artworkViewModel;

    @Inject
    ConnectionUtils connectionUtils;
    private ArtworkSummary artworkSummary;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    @BindView(R.id.tv_amount)
    TextView tvAmount;

    @BindView(R.id.tv_sold)
    TextView tvSold;

    @BindView(R.id.et_bid_amount)
    EditText etBidAmount;

    @BindView(R.id.bt_bid)
    Button btBid;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    @BindView(R.id.rv_bids)
    RecyclerView rvBids;

    @BindView(R.id.bid_frame)
    FrameLayout bidFrame;

    private UserResult userResult;

    ProgressDialog progressDialog;
    ViewGroup dummyFrame;

    //To ensure it doesnt recur while getting the summary
    private boolean newCheck = false;

    //To store the Recycler view Current state
    private Parcelable savedRecyclerLayoutState;

    private OnFragmentInteractionListener mListener;

    private BidAdapter mAdapter;
    LinearLayoutManager layoutManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    public BidFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artworkViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), viewModelFactory).get(ArtworkViewModel.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_bid, container, false);
        ButterKnife.bind(this, rootView);
        artworkViewModel.getBidResponse().observe(this, this::handleBid);
        progressDialog = UiUtils.getProgressDialog(getContext(), getString(R.string.loading),false);
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(getActivity()));


        if (getArguments() != null) {
            userResult = getArguments().getParcelable(CURRENT_USER);
            artworkSummary = getArguments().getParcelable(ARTWORK_STRING);

            layoutManager = new LinearLayoutManager(getContext() );
            rvBids.setLayoutManager(layoutManager);

            if(savedInstanceState == null){
                mAdapter = new BidAdapter(artworkSummary.getBids(),  getContext());
                mAdapter.setBidList(artworkSummary.getBids());
                //rvBids.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

                rvBids.setNestedScrollingEnabled(false);
                rvBids.setAdapter(mAdapter);
            }
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
        AndroidSupportInjection.inject(this);
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
        if (artworkSummary.getAcceptedBid() != null) {
            btBid.setVisibility(View.INVISIBLE);
            tvSold.setVisibility(View.VISIBLE);
        } else {
            btBid.setVisibility(View.VISIBLE);
            tvSold.setVisibility(View.INVISIBLE);
        }

        if(artworkSummary.getBids()!=null && artworkSummary.getBids().size()>0){
            bidFrame.setVisibility(View.VISIBLE);
            rvBids.setVisibility(View.VISIBLE);


        }
    }


    private void handleBid(MVResponse mvResponse) {
        switch (mvResponse.status) {

            case LOADING:
                progressDialog.show();
                break;
            case SUCCESS:
                progressDialog.dismiss();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                    Bid completedBid = objectMapper.convertValue(arttResponse.getData(), Bid.class);
                    UiUtils.showSuccessSnack("Successful Purchase. Welcome ", getContext(), clRoot);
                    if(newCheck){
                        newCheck = false;
                        artworkViewModel.findArtSummary(userResult.getTokenInfo().getAccessToken(), completedBid.getArtworkId());
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


    @OnClick(R.id.bt_bid)
    void attemptPurchase() {

        String stringAmount = etBidAmount.getText().toString();
        Bid bid = new Bid();
        bid.setAmount(Double.parseDouble(stringAmount));
        bid.setArtworkId(artworkSummary.getId());

        if (connectionUtils.handleNoInternet(getActivity())) {
            newCheck = true;
            artworkViewModel.bidForArt(userResult.getTokenInfo().getAccessToken(), bid);
        }

    }
}
