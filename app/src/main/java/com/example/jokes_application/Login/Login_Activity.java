package com.example.jokes_application.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.window.OnBackInvokedDispatcher;

import com.example.jokes_application.FragmentUtils.FragmentUtils;
import com.example.jokes_application.Interfaces.LoginCallBack;
import com.example.jokes_application.MainActivity;
import com.example.jokes_application.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {
    private SharedViewModel sharedViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            FragmentManager manager = getSupportFragmentManager();
            FragmentUtils.setFragment(manager, R.id.login_frameLayout, new Number_Verify_Fragment());
        }

    }

}