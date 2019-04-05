//package com.olamide.findartt.fragment;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Parcelable;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.FrameLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.olamide.findartt.R;
//import com.olamide.findartt.adapter.BidAdapter;
//import com.olamide.findartt.models.ArtworkSummary;
//import com.olamide.findartt.models.Bid;
//import com.olamide.findartt.models.FindArttResponse;
//import com.olamide.findartt.models.User;
//import com.olamide.findartt.utils.ErrorUtils;
//import com.olamide.findartt.utils.TempStorageUtils;
//import com.olamide.findartt.utils.UiUtils;
//import com.olamide.findartt.utils.network.FindArttRepository;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.MVResponse;
//import timber.log.Timber;
//
//import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
//import static com.olamide.findartt.Constants.ARTWORK_STRING;
//import static com.olamide.findartt.Constants.CURRENT_USER;
//
//
//public class BidFragment extends Fragment {
//
//
//    private Call<FindArttResponse<Bid>> responseCall;
//
//    private String accessToken;
//    private User user;
//    private ArtworkSummary artworkSummary;
//
//    @BindView(R.id.cl_root)
//    CoordinatorLayout clRoot;
//
//    @BindView(R.id.tv_amount)
//    TextView tvAmount;
//
//    @BindView(R.id.tv_sold)
//    TextView tvSold;
//
//    @BindView(R.id.et_bid_amount)
//    EditText etBidAmount;
//
//    @BindView(R.id.bt_bid)
//    Button btBid;
//
//    @BindView(R.id.pb_loading)
//    ProgressBar loadingPb;
//
//    @BindView(R.id.rv_bids)
//    RecyclerView rvBids;
//
//    @BindView(R.id.bid_frame)
//    FrameLayout bidFrame;
//
//
//    //To store the Recycler view Current state
//    private Parcelable savedRecyclerLayoutState;
//
//    private OnFragmentInteractionListener mListener;
//
//    private BidAdapter mAdapter;
//    LinearLayoutManager layoutManager;
//
//    public BidFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        accessToken = TempStorageUtils.readSharedPreferenceString(getContext(), ACCESS_TOKEN_STRING);
//
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_bid, container, false);
//        ButterKnife.bind(this, rootView);
//        if (getArguments() != null) {
//            user = getArguments().getParcelable(CURRENT_USER);
//            artworkSummary = getArguments().getParcelable(ARTWORK_STRING);
//
//            layoutManager = new LinearLayoutManager(getContext() );
//            rvBids.setLayoutManager(layoutManager);
//
//            if(savedInstanceState == null){
//                mAdapter = new BidAdapter(artworkSummary.getBids(),  getContext());
//                mAdapter.setBidList(artworkSummary.getBids());
//                //rvBids.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//
//                rvBids.setNestedScrollingEnabled(false);
//                rvBids.setAdapter(mAdapter);
//            }
//        }
//        loadUi();
//
//        return rootView;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//
//    void loadUi() {
//        tvAmount.setText(artworkSummary.getMinimumAmount().toString());
//        if (artworkSummary.getAcceptedBid() != null) {
//            btBid.setVisibility(View.INVISIBLE);
//            tvSold.setVisibility(View.VISIBLE);
//        } else {
//            btBid.setVisibility(View.VISIBLE);
//            tvSold.setVisibility(View.INVISIBLE);
//        }
//
//        if(artworkSummary.getBids()!=null && artworkSummary.getBids().size()>0){
//            bidFrame.setVisibility(View.VISIBLE);
//            rvBids.setVisibility(View.VISIBLE);
//
//
//        }
//    }
//
//    @OnClick(R.id.bt_bid)
//    void attemptPurchase() {
//
//        String stringAmount = etBidAmount.getText().toString();
//        Bid bid = new Bid();
//        bid.setAmount(Double.parseDouble(stringAmount));
//        bid.setArtworkId(artworkSummary.getId());
//
//        loadingPb.setVisibility(View.VISIBLE);
//        btBid.setVisibility(View.INVISIBLE);
//        responseCall = FindArttRepository.bidForArt(accessToken, bid);
//        responseCall.enqueue(new Callback<FindArttResponse<Bid>>() {
//
//            @Override
//            public void onResponse(Call<FindArttResponse<Bid>> call, MVResponse<FindArttResponse<Bid>> response) {
//                loadingPb.setVisibility(View.INVISIBLE);
////                btBuy.setVisibility(View.VISIBLE);
//
//                if (response.body() != null) {
//                    FindArttResponse<Bid> arttResponse = response.body();
//                    Bid completedBid = arttResponse.getData();
//                    UiUtils.showSuccessSnack("Successful Purchase. ", getContext(), clRoot);
//
//                } else {
//                    btBid.setVisibility(View.VISIBLE);
//                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<FindArttResponse<Bid>> call, Throwable t) {
//                loadingPb.setVisibility(View.INVISIBLE);
//                btBid.setVisibility(View.VISIBLE);
//                ErrorUtils.handleInternetError(getContext(), clRoot);
//                Timber.e(t);
//            }
//        });
//
//
//    }
//}
