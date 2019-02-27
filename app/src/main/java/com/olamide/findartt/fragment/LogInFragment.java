package com.olamide.findartt.fragment;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.olamide.findartt.Constants;
import com.olamide.findartt.activity.DashboardActivity;
import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.Constants.RC_SIGN_IN;
import static com.olamide.findartt.Constants.TYPE_STRING;
import static com.olamide.findartt.Constants.USEREMAIL_STRING;
import static com.olamide.findartt.Constants.USERPASSWORD_STRING;



public class LogInFragment extends Fragment {

    FragmentDataPasser dataPasser;
    private Call<FindArttResponse<UserResult>> responseCall;

    private Call<FindArttResponse<User>> loginResponseCall;
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

    @BindView(R.id.pb_loading_google)
    ProgressBar loadingPbGoogle;

    @BindView(R.id.btn_login)
    Button loginBt;

    @BindView(R.id.btn_login_google)
    SignInButton signInButton;


    private String accessToken;
    private UserLogin login = new UserLogin();
    private String userEmail;
    private String userPassword;

    private boolean remember = false;

    private GoogleSignInClient mGoogleSignInClient;

    public LogInFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString(USEREMAIL_STRING);
            userPassword = savedInstanceState.getString(USERPASSWORD_STRING);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            userEmail = savedInstanceState.getString(USEREMAIL_STRING);
            userPassword = savedInstanceState.getString(USERPASSWORD_STRING);
        }

        accessToken = TempStorageUtils.readSharedPreferenceString(getContext(),ACCESS_TOKEN_STRING);
        if(accessToken!=null && !accessToken.isEmpty()){
            getUserFromToken(accessToken);
        }

        if (getArguments() != null) {
            userEmail = getArguments().getString(USEREMAIL_STRING);
            userPassword = getArguments().getString(USERPASSWORD_STRING);
            updateLogin(false);
            login(login);
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Constants.GOOGLE_WEB_CLIENT_ID)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            //call api google login
            loginGoogle(account);
        }

        return rootView;
    }

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
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        updateLogin(true);
        outState.putString(USEREMAIL_STRING, userEmail);
        outState.putString(USERPASSWORD_STRING, userPassword);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            loginGoogle(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("signInResult:failed code=" + e.getStatusCode());
            ErrorUtils.handleError(getContext(), clRoot);
        }
    }

    void loginGoogle(GoogleSignInAccount account) {
        account.getIdToken();
        loadingPbGoogle.setIndeterminate(true);
        loadingPbGoogle.getIndeterminateDrawable().setColorFilter(getContext().getColor(R.color.color_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        loadingPbGoogle.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.INVISIBLE);

        String idToken = account.getIdToken();
        responseCall = FindArttService.loginGoogle(new TokenInfo(idToken));
        responseCall.enqueue(new Callback<FindArttResponse<UserResult>>() {

            @Override
            public void onResponse(Call<FindArttResponse<UserResult>> call, Response<FindArttResponse<UserResult>> response) {
                loadingPbGoogle.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<UserResult> arttResponse = response.body();
                    UserResult userResult = arttResponse.getData();
                    storeAccessToken(userResult.getTokenInfo().getAccessToken());
                    UiUtils.showSuccessSnack("Successful Login. Welcome " + userResult.getUser().getName(), getContext(), clRoot);

                    goToDashboard(userResult.getUser());
                } else {
                    googleSignOut();
                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<UserResult>> call, Throwable t) {
                loadingPbGoogle.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                ErrorUtils.handleInternetError(getContext(), clRoot);
                Timber.e(t);
            }
        });


    }

    @OnClick(R.id.btn_login)
    void loginClick() {
        updateLogin(true);
        login(login);
    }

    void login(final UserLogin logins) {
        login = logins;
        loadingPb.setVisibility(View.VISIBLE);
        loginBt.setVisibility(View.INVISIBLE);
        responseCall = FindArttService.login(logins);
        responseCall.enqueue(new Callback<FindArttResponse<UserResult>>() {

            @Override
            public void onResponse(Call<FindArttResponse<UserResult>> call, Response<FindArttResponse<UserResult>> response) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<UserResult> arttResponse = response.body();
                    UserResult userResult = arttResponse.getData();
                    storeAccessToken(userResult.getTokenInfo().getAccessToken());
                    if (remember) {
                        storeUserCredentials(logins);
                    }
                    UiUtils.showSuccessSnack("Successful Login. Welcome " + userResult.getUser().getName(), getContext(), clRoot);

                    goToDashboard(userResult.getUser());
                } else {
                    removeUserCredentials();
                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<UserResult>> call, Throwable t) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);
                ErrorUtils.handleInternetError(getContext(), clRoot);
                Timber.e(t);
            }
        });


    }



    void getUserFromToken(String accessToken) {

        loadingPb.setVisibility(View.VISIBLE);
        loginBt.setVisibility(View.INVISIBLE);
        signInButton.setVisibility(View.INVISIBLE);
        loginResponseCall = FindArttService.getUserFromToken(accessToken);
        loginResponseCall.enqueue(new Callback<FindArttResponse<User>>() {

            @Override
            public void onResponse(Call<FindArttResponse<User>> call, Response<FindArttResponse<User>> response) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<User> arttResponse = response.body();
                    User user = arttResponse.getData();
                    goToDashboard(user);
                } else {
                    TempStorageUtils.removeSharedPreference(getContext(),ACCESS_TOKEN_STRING);
                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<User>> call, Throwable t) {
                loadingPb.setVisibility(View.INVISIBLE);
                loginBt.setVisibility(View.VISIBLE);
                signInButton.setVisibility(View.VISIBLE);
                ErrorUtils.handleInternetError(getContext(), clRoot);
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

    @OnClick(R.id.btn_login_google)
    void requestGoogleToken() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    void goToDashboard(User user){
        Intent intent = new Intent(getContext(), DashboardActivity.class);
        intent.putExtra(CURRENT_USER,user);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    void updateLogin(boolean withUi) {
        if (withUi) {
            userEmail = emailText.getText().toString();
            userPassword = passwordText.getText().toString();
            remember = rememberCheck.isChecked();
        }
        login.setEmail(userEmail);
        login.setPassword(userPassword);
    }

    void storeAccessToken(String accessToken) {
        TempStorageUtils.writeSharedPreferenceString(getContext(), Constants.ACCESS_TOKEN_STRING, accessToken);
    }

    void storeUserCredentials(UserLogin userLogin) {
        TempStorageUtils.storeUserLogin(getContext(), userLogin);
    }


    private void googleSignOut (){
        signOut();
        revokeAccess();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }



    void removeUserCredentials() {
        TempStorageUtils.removeUserLogin(getContext());
    }
}
