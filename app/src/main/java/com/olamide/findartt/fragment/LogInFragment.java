package com.olamide.findartt.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.activity.SignInActivity.TYPE_STRING;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends Fragment {

    FragmentDataPasser dataPasser;
    private Call<FindArttResponse<UserResult>> responseCall;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    @BindView(R.id.et_email)
    EditText emailText;

    @BindView(R.id.et_password)
    EditText passwordText;

    @BindView(R.id.chb_remember_me)
    CheckBox rememberCheck;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    @BindView(R.id.btn_login)
    Button loginBt;


    private UserLogin login = new UserLogin();
    private String userEmail;
    private String userPassword;


    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }

        if(savedInstanceState!= null){

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        ButterKnife.bind(this, rootView);

        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString("userEmail");
            userPassword = savedInstanceState.getString("userPassword");
            emailText.setText(userEmail);
            passwordText.setText(userPassword);
        }
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

        dataPasser = (FragmentDataPasser) context;
        Bundle bundle = new Bundle();
        bundle.putString(TYPE_STRING, "LOGIN");
        dataPasser.onDataPass(bundle);
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateLogin();
        outState.putString("userEmail",userEmail);
        outState.putString("userPassword",userPassword);
//        outState.p
    }

    @OnClick(R.id.btn_login)
    void login() {
        loadingPb.setVisibility(View.VISIBLE);
        loginBt.setVisibility(View.INVISIBLE);
        updateLogin();
        login.setEmail(userEmail);
        login.setPassword(userPassword);

        responseCall = FindArttService.login(login);
        responseCall.enqueue(new Callback<FindArttResponse<UserResult>>() {

            @Override
            public void onResponse(Call<FindArttResponse<UserResult>> call, Response<FindArttResponse<UserResult>> response) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<UserResult> arttResponse = response.body();
                    UserResult userResult = arttResponse.getData();
                    Timber.e(userResult.getTokenInfo().getAccessToken());
                    UiUtils.showSnack("Successful Login. Welcome "+userResult.getUser().getName(),getContext(),clRoot );
                } else {
                    ErrorUtils.handleApiError(response.errorBody(), getContext(),clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<UserResult>> call, Throwable t) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);
                ErrorUtils.handleInternetError(getContext(),clRoot);
                Timber.e(t);
            }
        });
    }


    @OnClick(R.id.sign_up_sug)
    void loadSignUp() {
        FragmentManager fragmentManager = getFragmentManager();
        SignUpFragment signUpFragment = new SignUpFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.sign_in_frame, signUpFragment)
                .commit();

    }

    void updateLogin(){
        userEmail = emailText.getText().toString();
        userPassword = passwordText.getText().toString();
    }
}
