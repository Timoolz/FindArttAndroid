package com.olamide.findartt.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.olamide.findartt.R;
import com.olamide.findartt.adapter.ArtworkAdapter;
import com.olamide.findartt.adapter.MyArtworkAdapter;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.RecyclerViewUtils;
import com.olamide.findartt.viewmodels.HomeViewModel;
import com.olamide.findartt.viewmodels.UserArtworksViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


public class MyArtworksFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    UserArtworksViewModel userArtworksViewModel;

    @BindView(R.id.rv_my_artwork)
    RecyclerView artworkRv;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private List<ArtworkSummary> summaryList;
    private MyArtworkAdapter mAdapter;
    GridLayoutManager layoutManager;

    public MyArtworksFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userArtworksViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserArtworksViewModel.class);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_artworks, container, false);
        ButterKnife.bind(this, rootView);

        userArtworksViewModel.getMyArtWorkResponse().observe(this, this::displayUi);
        int spanCount = RecyclerViewUtils.getSpanCount(artworkRv, getResources().getDimension(R.dimen.card_width));
        layoutManager = new GridLayoutManager(getContext(), spanCount);
        artworkRv.setLayoutManager(layoutManager);

        mAdapter = new MyArtworkAdapter(summaryList, getContext());
        mAdapter.setArtworkList(summaryList);
        artworkRv.setNestedScrollingEnabled(false);
        artworkRv.setAdapter(mAdapter);


        loadArtWorks();

        return rootView;
    }


    private void displayUi(MVResponse mvResponse) {

        switch (mvResponse.status) {

            case LOADING:
                if (summaryList == null || summaryList.size() <= 0) {
                    loadingPb.setVisibility(View.VISIBLE);
                }
                break;


            case SUCCESS:
                displayUi();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mvResponse.data, FindArttResponse.class);
                    summaryList = objectMapper.convertValue(arttResponse.getData(), new TypeReference<List<ArtworkSummary>>() {
                    });
                    if (summaryList.size() <= 0) {
                        showEmptyMessage();
                    } else {
                        mAdapter.setArtworkList(summaryList);
                    }

                } catch (Exception ee) {
                    Timber.e(ee);
                    ErrorUtils.handleError((getContext()), dummyFrame);
                }


                break;


            case ERROR:
                loadingPb.setVisibility(View.INVISIBLE);
                ErrorUtils.handleThrowable(mvResponse.error, getContext(), dummyFrame);
                break;

            default:
                loadingPb.setVisibility(View.INVISIBLE);
                break;


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


    void loadArtWorks() {
        if (connectionUtils.handleNoInternet(getActivity())) {
            userArtworksViewModel.findMyArtworks(userResult.getTokenInfo().getAccessToken());
        }

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
