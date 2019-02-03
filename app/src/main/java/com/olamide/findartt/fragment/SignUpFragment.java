package com.olamide.findartt.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
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

import com.olamide.findartt.Constants;
import com.olamide.findartt.enums.Gender;
import com.olamide.findartt.interfaces.FragmentDataPasser;
import com.olamide.findartt.R;
import com.olamide.findartt.models.FindArttResponse;
import com.olamide.findartt.models.UserLogin;
import com.olamide.findartt.models.UserResult;
import com.olamide.findartt.models.UserSignup;
import com.olamide.findartt.utils.TempStorageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

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

    @BindView(R.id.btn_sign_up)
    Button signUpbt;


    private UserSignup signup = new UserSignup();
    private String userEmail;
    private String fname;
    private String lname;
    private String phone;
    private Gender gender;
    private String country;
    private String confirmUserPassword;
    private String userPassword;


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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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


    void updateSignUp(boolean withUi) {
        if (withUi) {
            userEmail = emailText.getText().toString();
            fname = fnameText.getText().toString();
            lname = lnameText.getText().toString();
            phone = phoneText.getText().toString();
            gender = Gender.valueOf(genderSp.getSelectedItem().toString());
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
    }

    void storeAccessToken(String accessToken) {
        TempStorageUtils.writeSharedPreferenceString(getContext(), Constants.ACCESS_TOKEN_STRING, accessToken);
    }


}
