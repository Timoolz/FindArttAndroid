package com.olamide.findartt.fragment;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.olamide.findartt.Constants;
import com.olamide.findartt.FindArttApplication;
import com.olamide.findartt.LoginViewModel;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.ViewModelFactory;
//import com.olamide.findartt.activity.DashboardActivity;
import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.User;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import timber.log.Timber;

import static com.olamide.findartt.Constants.ACCESS_TOKEN_STRING;
import static com.olamide.findartt.Constants.RC_SIGN_IN;
import static com.olamide.findartt.Constants.TYPE_STRING;
import static com.olamide.findartt.Constants.USEREMAIL_STRING;
import static com.olamide.findartt.Constants.USERPASSWORD_STRING;


public class LogInFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    FragmentDataPasser dataPasser;

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
    private final ObjectMapper objectMapper = new ObjectMapper();

    LoginViewModel loginViewModel;

    ProgressDialog progressDialog;
    ViewGroup dummyFrame;


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

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_log_in, container, false);
        ButterKnife.bind(this, rootView);

        progressDialog = UiUtils.getProgressDialog(getContext(), getString(R.string.loading),false);
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(getActivity()));

        ((FindArttApplication) getActivity().getApplication()).getAppComponent().doInjection(this);

        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel.class);
        loginViewModel.getLoginResponse().observe(this, this::consumeResponse);

        accessToken = TempStorageUtils.readSharedPreferenceString(getContext(), ACCESS_TOKEN_STRING);
        if (accessToken != null && !accessToken.isEmpty()) {
            loginFromFromToken(accessToken);
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
            Timber.w("signInResult:failed code=%s", e.getStatusCode());
            ErrorUtils.handleError(Objects.requireNonNull(getContext()), clRoot);
        }
    }


    /*
     * method to handle response
     * */
    private void consumeResponse(MVResponse mVResponse) {

        switch (mVResponse.status) {

            case LOADING:

                progressDialog.show();
                break;

            case SUCCESS:

                progressDialog.dismiss();
                FindArttResponse arttResponse = new FindArttResponse();
                try {
                    arttResponse = objectMapper.convertValue(mVResponse.data, FindArttResponse.class);
                    UserResult userResult = objectMapper.convertValue(arttResponse.getData(), UserResult.class);
                    storeUserCredentials(userResult, login, remember);
                    UiUtils.showSuccessSnack("Successful Login. Welcome " + userResult.getUser().getName(), getContext(), clRoot);
                } catch (Exception e) {
                    try {
                        //User is already logged in
                        arttResponse = objectMapper.convertValue(mVResponse.data, FindArttResponse.class);
                        User user = objectMapper.convertValue(arttResponse.getData(), User.class);

                        UiUtils.showSuccessSnack(" Welcome " + user.getName(), getContext(), clRoot);
                    } catch (Exception ee) {
                        Timber.e(e);
                        ErrorUtils.handleError(Objects.requireNonNull(getContext()), clRoot);
                    }

                }
                //renderSuccessResponse(apiMVResponse.data);
                break;

            case ERROR:

                progressDialog.dismiss();
                removeUserCredentials();
                googleSignOut();
                ErrorUtils.handleThrowable(mVResponse.error, getContext(), clRoot);
                break;

            default:
                break;
        }
    }


    void loginGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        loginViewModel.hitGoogleLogin(new TokenInfo(idToken));

    }

    @OnClick(R.id.btn_login)
    void loginClick() {
        updateLogin(true);
        login(login);
    }

    void login(final UserLogin logins) {
        login = logins;
        if (loginValid(login)) {
            loginViewModel.hitLogin(login);
        } else {
            ErrorUtils.handleUserError(getString(R.string.generic_form_validation), Objects.requireNonNull(getContext()), dummyFrame);
        }


    }


    void loginFromFromToken(String accessToken) {
        loginViewModel.getUserFromToken(accessToken);
    }


    @OnClick(R.id.sign_up_sug)
    void loadSignUp() {
        FragmentManager fragmentManager = getFragmentManager();
        SignUpFragment signUpFragment = new SignUpFragment();
        Objects.requireNonNull(fragmentManager).beginTransaction()
                .replace(R.id.sign_in_frame, signUpFragment)
                .commit();

    }

    @OnClick(R.id.btn_login_google)
    void requestGoogleToken() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    //    void goToDashboard(User user){
//        Intent intent = new Intent(getContext(), DashboardActivity.class);
//        intent.putExtra(CURRENT_USER,user);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }
    void updateLogin(boolean withUi) {
        if (withUi) {
            userEmail = emailText.getText().toString();
            userPassword = passwordText.getText().toString();
            remember = rememberCheck.isChecked();
        }
        login.setEmail(userEmail);
        login.setPassword(userPassword);
    }


    void storeUserCredentials(UserResult userResult, UserLogin userLogin) {
        storeUserCredentials(userResult, userLogin, false);
    }

    void storeUserCredentials(UserResult userResult, UserLogin userLogin, boolean remember) {
        TempStorageUtils.storeActiveUser(getContext(), userResult);
        if (remember && loginValid(login)) {
            TempStorageUtils.storeUserLogin(getContext(), userLogin);
        }
    }

    void removeUserCredentials() {
        TempStorageUtils.removeActiveUser(getContext());
        TempStorageUtils.removeUserLogin(getContext());
    }


    private void googleSignOut() {
        signOut();
        revokeAccess();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
    }


    private boolean loginValid(UserLogin logins) {

        if (logins.getEmail().trim().isEmpty()||logins.getPassword().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
