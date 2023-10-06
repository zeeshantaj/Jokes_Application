package com.example.jokes_application.Login;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.jokes_application.FragmentUtils.FragmentUtils;
import com.example.jokes_application.Interfaces.LoginCallBack;
import com.example.jokes_application.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Stack;
import java.util.concurrent.TimeUnit;

public class Number_Verify_Fragment extends Fragment {


    public Number_Verify_Fragment() {
        // Required empty public constructor
    }

    private View view;
    private TextInputEditText numberEdit;
    private Button nextBtn;
    private FirebaseAuth auth;

    private LoginCallBack loginCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_number__verify_, container, false);
        auth = FirebaseAuth.getInstance();
        numberEdit = view.findViewById(R.id.phoneNumberEditText);
        nextBtn = view.findViewById(R.id.numNextBtn);
        nextBtn.setEnabled(true);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof LoginCallBack) {
            loginCallBack = (LoginCallBack) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement LoginCallback");
        }
        loginCallBack.onStepChanged(1);

        Log.e("MyApp","numberFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextBtn.setOnClickListener((v -> {
            String phoneNumber = numberEdit.getText().toString();
            if (phoneNumber.isEmpty()){
                numberEdit.setError("number is empty");
                return;
                }
            else {
                // Send a verification code to the provided phone number
                nextBtn.setEnabled(false);
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        phoneNumber,
                        120,             // Timeout duration
                        TimeUnit.SECONDS,
                        getActivity(),           // Activity (or context) for callback
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {

                                Log.e("MyApp", "credential" + credential.toString());
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                // Handle verification failure, e.g., display an error message.

                                Log.e("MyApp", "failed" + e.getLocalizedMessage());
                                Toast.makeText(getActivity(), "Failed "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number.
                                // You can use this verificationId to verify the code later.


                                SharedViewModel sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
                                sharedViewModel.setVerificationId(verificationId);

                                Log.e("MyApp", "verificationInNumberFragment  ->  " + verificationId);

                                FragmentManager manager= getActivity().getSupportFragmentManager();
                                FragmentUtils.setFragment(manager,R.id.login_frameLayout,new OTP_Verification_Fragment());


                            }
                        });

            }
        }));
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}