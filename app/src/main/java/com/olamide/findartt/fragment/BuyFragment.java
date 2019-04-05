//package com.olamide.findartt.fragment;
//
//import android.content.Context;
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.olamide.findartt.R;
//import com.olamide.findartt.models.ArtworkSummary;
//import com.olamide.findartt.models.Buy;
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
//public class BuyFragment extends Fragment {
//
//    private Call<FindArttResponse<Buy>> responseCall;
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
//    @BindView(R.id.bt_buy)
//    Button btBuy;
//
//    @BindView(R.id.pb_loading)
//    ProgressBar loadingPb;
//
//
//    private OnFragmentInteractionListener mListener;
//
//    public BuyFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        accessToken = TempStorageUtils.readSharedPreferenceString(getContext(),ACCESS_TOKEN_STRING);
//
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View rootView = inflater.inflate(R.layout.fragment_buy, container, false);
//        ButterKnife.bind(this, rootView);
//
//        if (getArguments() != null) {
//            user = getArguments().getParcelable(CURRENT_USER);
//            artworkSummary = getArguments().getParcelable(ARTWORK_STRING);
//        }
//        loadUi();
//
//        return rootView;
//
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
//
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//    void loadUi(){
//        tvAmount.setText(artworkSummary.getMinimumAmount().toString());
//        if(artworkSummary.getCurrentBuy()!=null){
//            btBuy.setVisibility(View.INVISIBLE);
//            tvSold.setVisibility(View.VISIBLE);
//        }else {
//            btBuy.setVisibility(View.VISIBLE);
//            tvSold.setVisibility(View.INVISIBLE);
//        }
//    }
//    @OnClick(R.id.bt_buy)
//    void attemptPurchase(){
//
//        Buy buy = new Buy();
//        buy.setArtworkId(artworkSummary.getId());
//
//        loadingPb.setVisibility(View.VISIBLE);
//        btBuy.setVisibility(View.INVISIBLE);
//        responseCall = FindArttRepository.buyArt(accessToken,buy);
//        responseCall.enqueue(new Callback<FindArttResponse<Buy>>() {
//
//            @Override
//            public void onResponse(Call<FindArttResponse<Buy>> call, MVResponse<FindArttResponse<Buy>> response) {
//                loadingPb.setVisibility(View.INVISIBLE);
////                btBuy.setVisibility(View.VISIBLE);
//
//                if (response.body() != null) {
//                    FindArttResponse<Buy> arttResponse = response.body();
//                    Buy completedBuy = arttResponse.getData();
//
//                    UiUtils.showSuccessSnack("Successful Purchase. Welcome " , getContext(), clRoot);
//
//                } else {
//                    btBuy.setVisibility(View.VISIBLE);
//                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<FindArttResponse<Buy>> call, Throwable t) {
//                loadingPb.setVisibility(View.INVISIBLE);
//                btBuy.setVisibility(View.VISIBLE);
//                ErrorUtils.handleInternetError(getContext(), clRoot);
//                Timber.e(t);
//            }
//        });
//
//
//
//    }
//}
