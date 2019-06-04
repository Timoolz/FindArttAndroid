package com.olamide.findartt.ui.fragment;

import android.app.ProgressDialog;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.olamide.findartt.AppConstants;
import com.olamide.findartt.utils.NavigationUtils;
import com.olamide.findartt.viewmodels.SignUpViewModel;
import com.olamide.findartt.ViewModelFactory;

import com.olamide.findartt.ui.activity.DashboardActivity;
import com.olamide.findartt.enums.Gender;

import com.olamide.findartt.R;
import com.olamide.findartt.models.api.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.models.mvvm.MVResponse;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.ConnectionUtils;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import dagger.android.support.AndroidSupportInjection;
import timber.log.Timber;

import static com.olamide.findartt.AppConstants.RC_SIGN_IN;
import static com.olamide.findartt.AppConstants.TYPE_STRING;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    @Inject
    ViewModelFactory viewModelFactory;

    @Inject
    ConnectionUtils connectionUtils;


    private OnFragmentInteractionListener mListener;

    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;


    @BindView(R.id.et_email)
    EditText emailText;

    @BindView(R.id.et_fname)
    EditText fnameText;

    @BindView(R.id.et_lname)
    EditText lnameText;

    @BindView(R.id.et_phone)
    EditText phoneText;

    @BindView(R.id.sp_gender)
    Spinner genderSp;

    @BindView(R.id.et_country)
    EditText countryText;

    @BindView(R.id.et_password)
    EditText passwordText;

    @BindView(R.id.et_password_confirm)
    EditText passwordConfirmText;

    @BindView(R.id.pb_loading)
    ProgressBar loadingPb;

    @BindView(R.id.pb_loading_google)
    ProgressBar loadingPbGoogle;

    @BindView(R.id.btn_sign_up)
    Button signUpbt;

    @BindView(R.id.btn_signup_google)
    SignInButton signInButton;

    private final ObjectMapper objectMapper = new ObjectMapper();

    SignUpViewModel signUpViewModel;

    ProgressDialog progressDialog;
    ViewGroup dummyFrame;


    private UserSignup signup = new UserSignup();
    private String userEmail;
    private String fname;
    private String lname;
    private String phone;
    private Gender gender;
    private String country;
    private String confirmUserPassword;
    private String userPassword;


    private GoogleSignInClient mGoogleSignInClient;


    public SignUpFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, rootView);


        progressDialog = UiUtils.getProgressDialog(getContext(), getString(R.string.loading), false);
        dummyFrame = UiUtils.getDummyFrame(Objects.requireNonNull(getActivity()));


        signUpViewModel = ViewModelProviders.of(this, viewModelFactory).get(SignUpViewModel.class);
        signUpViewModel.getSignupResponse().observe(this, this::consumeResponse);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(AppConstants.GOOGLE_WEB_CLIENT_ID)
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null) {
            //call api google signup
            signUpGoogle(account);
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
        void onFragmentInteraction(Uri uri);
    }


    @OnClick(R.id.login_sug)
    void loadLogin() {
        NavOptions navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .setPopUpTo(R.id.login_dest, true)
                .build();
        Navigation.findNavController(Objects.requireNonNull(getActivity()), R.id.pre_auth_nav_host_fragment)
                .navigate(R.id.login_dest, null, navOptions);


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
            signUpGoogle(account);
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
                    storeUserCredentials(userResult);
                    UiUtils.showSuccessSnack("Successful SignUp. Welcome " + userResult.getUser().getName(), getContext(), clRoot);
                } catch (Exception e) {
                    Timber.e(e);
                    ErrorUtils.handleError(Objects.requireNonNull(getContext()), clRoot);

                }
                goToDashboard();
                break;

            case ERROR:
                progressDialog.dismiss();
                googleSignOut();
                ErrorUtils.handleThrowable(mVResponse.error, getContext(), clRoot);
                break;

            default:
                break;
        }
    }

    void storeUserCredentials(UserResult userResult) {
        TempStorageUtils.storeActiveUser(getContext(), userResult);

    }

    void goToDashboard() {
        NavigationUtils.goToDashboard(getActivity());

    }


    void signUpGoogle(GoogleSignInAccount account) {
        String idToken = account.getIdToken();
        if (connectionUtils.handleNoInternet(getActivity())) {
            signUpViewModel.signUpGoogle(new TokenInfo(idToken));
        }
    }


    @OnClick(R.id.btn_signup_google)
    void requestGoogleToken() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @OnClick(R.id.btn_sign_up)
    void signUp() {
        updateSignUp(true);
        if ((!signUpValid(signup, confirmUserPassword))) {
            ErrorUtils.handleUserError(getString(R.string.generic_form_validation), Objects.requireNonNull(getContext()), dummyFrame);
        } else if (!passwordValidated(signup.getPassword(), confirmUserPassword)) {
            ErrorUtils.handleUserError(getString(R.string.password_form_validation), Objects.requireNonNull(getContext()), dummyFrame);
        } else {
            if (connectionUtils.handleNoInternet(getActivity())) {
                signUpViewModel.signUp(signup);
            }

        }


    }


    void updateSignUp(boolean withUi) {
        if (withUi) {
            userEmail = emailText.getText().toString();
            fname = fnameText.getText().toString();
            lname = lnameText.getText().toString();
            phone = phoneText.getText().toString();
            gender = Gender.valueOf(genderSp.getSelectedItem().toString().toUpperCase());
            country = countryText.getText().toString();
            confirmUserPassword = passwordConfirmText.getText().toString();
            userPassword = passwordText.getText().toString();

        }
        signup.setEmail(userEmail);
        signup.setFirstName(fname);
        signup.setLastName(lname);
        signup.setPhone(phone);
        signup.setGender(gender);
        signup.setCountry(country);
        signup.setPassword(userPassword);
        //temporary
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");
//        signup.setDateOfBirth(simpleDateFormat.format(new Date()));
//        signup.setDateOfBirth("1900-01-01T09:04:21.798Z");
    }

    private boolean signUpValid(UserSignup signup1, String confirmUserPassword) {
        if (signup1.getEmail().trim().isEmpty()
                || signup1.getPassword().trim().isEmpty()
                || confirmUserPassword.trim().isEmpty()
                || signup1.getFirstName().trim().isEmpty()
                || signup1.getLastName().trim().isEmpty()
                || signup1.getPhone().trim().isEmpty()
                || signup1.getCountry().trim().isEmpty()

        ) {
            return false;
        }
        return true;
    }

    private boolean passwordValidated(String password, String confirmUserPassword) {
        if (!password.trim().equals(confirmUserPassword.trim())) {
            return false;
        }
        return true;
    }

    void storeActiveUser(UserResult userResult) {
        TempStorageUtils.storeActiveUser(getContext(), userResult);
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


}
