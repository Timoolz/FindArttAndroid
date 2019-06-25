package com.olamide.findartt.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;
import com.olamide.findartt.viewmodels.HomeViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;


public class HomeFragment extends BaseFragment implements ArtworkAdapter.ArtworkAdapterOnClickListener {

    private OnFragmentInteractionListener mListener;


    HomeViewModel homeViewModel;


    @BindView(R.id.rv_artwork)
    RecyclerView artworkRv;

    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    private List<Artwork> artworkList;
    private ArtworkAdapter mAdapter;
    StaggeredGridLayoutManager layoutManager;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        this.setHasOptionsMenu(true);

        homeViewModel.getArtWorkResponse().observe(this, this::displayUi);
        int spanCount = RecyclerViewUtils.getSpanCount(artworkRv, 450);
        layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        artworkRv.setLayoutManager(layoutManager);


        mAdapter = new ArtworkAdapter(artworkList, getContext(), this);
        mAdapter.setArtworkList(artworkList);
        artworkRv.setNestedScrollingEnabled(false);
        artworkRv.setAdapter(mAdapter);


        if (artworkList == null || artworkList.size()<=0) {
            loadArtWorks();
        }

        return rootView;
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
                    try {
                        //Local data doesn't come in Api Response
                        artworkList = objectMapper.convertValue(mvResponse.data, new TypeReference<List<Artwork>>() {
                        });
                        if (artworkList.size() <= 0) {
                            showEmptyMessage();
                        } else {
                            mAdapter.setArtworkList(artworkList);
                        }
                    } catch (Exception ee) {
                        Timber.e(ee);
                        ErrorUtils.handleError((getContext()), dummyFrame);
                    }


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


    void loadArtWorks() {
        if (connectionUtils.handleNoInternet(getActivity())) {
            homeViewModel.findArtworks(userResult.getTokenInfo().getAccessToken());
        }

    }

    private void getFavouriteArt() {
        homeViewModel.findFavouriteArtworks();
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
        HomeFragmentDirections.GoToArtwork action = HomeFragmentDirections.goToArtwork(artwork);
        Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.nav_host_fragment)
                .navigate(action);

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.artwork_menu, menu);
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
}
