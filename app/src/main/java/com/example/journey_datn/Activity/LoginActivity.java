package com.example.journey_datn.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentCreateAccount;
import com.example.journey_datn.fragment.FragmentLogin;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loadFragment(new FragmentLogin());
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_login, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
