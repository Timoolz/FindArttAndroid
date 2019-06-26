package com.olamide.findartt.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;

import com.olamide.findartt.R;
import com.olamide.findartt.models.Artwork;
import com.olamide.findartt.models.ArtworkSummary;
import com.olamide.findartt.viewmodels.HomeViewModel;
import com.olamide.findartt.viewmodels.UserArtworksViewModel;

import java.util.List;

import butterknife.ButterKnife;


public class MyArtworksFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;

    UserArtworksViewModel userArtworksViewModel;

    private List<ArtworkSummary> summaryList;

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


        loadArtWorks();

        return rootView;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
