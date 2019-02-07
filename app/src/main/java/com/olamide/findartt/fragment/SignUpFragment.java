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
import android.widget.Spinner;

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
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.TokenInfo;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.utils.ErrorUtils;
import com.olamide.findartt.utils.TempStorageUtils;
import com.olamide.findartt.utils.UiUtils;
import com.olamide.findartt.utils.network.FindArttService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.olamide.findartt.Constants.CURRENT_USER;
import static com.olamide.findartt.Constants.RC_SIGN_IN;
import static com.olamide.findartt.Constants.TYPE_STRING;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private Call<FindArttResponse<UserResult>> responseCall;


    FragmentDataPasser dataPasser;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, rootView);


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
            //signUpGoogle(account);
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
        bundle.putString(TYPE_STRING, "SIGNUP");
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


    @OnClick(R.id.login_sug)
    void loadLogin() {
        FragmentManager fragmentManager = getFragmentManager();
        LogInFragment logInFragment = new LogInFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.sign_in_frame, logInFragment)
                .commit();

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
            ErrorUtils.handleError(getContext(), clRoot);
        }
    }




    void signUpGoogle(GoogleSignInAccount account) {
        account.getIdToken();
        loadingPbGoogle.setIndeterminate(true);
        loadingPbGoogle.getIndeterminateDrawable().setColorFilter(getContext().getColor(R.color.color_primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        loadingPbGoogle.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.INVISIBLE);

        String idToken = account.getIdToken();
        responseCall = FindArttService.signUpGoogle(new TokenInfo(account.getIdToken()));
        responseCall.enqueue(new Callback<FindArttResponse<UserResult>>() {

            @Override
            public void onResponse(Call<FindArttResponse<UserResult>> call, Response<FindArttResponse<UserResult>> response) {
                loadingPbGoogle.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<UserResult> arttResponse = response.body();
                    UserResult userResult = arttResponse.getData();
                    storeAccessToken(userResult.getTokenInfo().getAccessToken());
                    UiUtils.showSuccessSnack("Successful Sign Up. Welcome " + userResult.getUser().getName(), getContext(), clRoot);

                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    intent.putExtra(CURRENT_USER,userResult.getUser());
                    startActivity(intent);
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


    @OnClick(R.id.btn_signup_google)
    void requestGoogleToken() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @OnClick(R.id.btn_sign_up)
    void signUp() {

        updateSignUp(true);
        loadingPb.setVisibility(View.VISIBLE);
        signUpbt.setVisibility(View.INVISIBLE);
        responseCall = FindArttService.signUp(signup);
        responseCall.enqueue(new Callback<FindArttResponse<UserResult>>() {

            @Override
            public void onResponse(Call<FindArttResponse<UserResult>> call, Response<FindArttResponse<UserResult>> response) {
                loadingPb.setVisibility(View.INVISIBLE);
                signUpbt.setVisibility(View.VISIBLE);

                if (response.body() != null) {
                    FindArttResponse<UserResult> arttResponse = response.body();
                    UserResult userResult = arttResponse.getData();
                    storeAccessToken(userResult.getTokenInfo().getAccessToken());

                    UiUtils.showSuccessSnack("Successful Sign Up. Welcome " + userResult.getUser().getName(), getContext(), clRoot);

                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    intent.putExtra(CURRENT_USER,userResult.getUser());
                    startActivity(intent);
                } else {
                    ErrorUtils.handleApiError(response.errorBody(), getContext(), clRoot);
                }

            }

            @Override
            public void onFailure(Call<FindArttResponse<UserResult>> call, Throwable t) {
                loadingPb.setVisibility(View.INVISIBLE);
                signUpbt.setVisibility(View.VISIBLE);
                ErrorUtils.handleInternetError(getContext(), clRoot);
                Timber.e(t);
            }
        });


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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");
//        signup.setDateOfBirth(simpleDateFormat.format(new Date()));
        signup.setDateOfBirth("1900-01-01T09:04:21.798Z");
    }

    void storeAccessToken(String accessToken) {
        TempStorageUtils.writeSharedPreferenceString(getContext(), Constants.ACCESS_TOKEN_STRING, accessToken);
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


}
