package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Model.User;
import com.example.journey_datn.R;
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentLogin extends Fragment {
    private EditText edtUsername, edtPassword;
    private TextView txtCreateAccount, txtForgot;
    private ImageView imgShowPassword;
    private Button btnLogin;
    private FirebaseAuth auth;
    private String email, password;
    private FirebaseDB firebaseDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);

        firebaseDB = new FirebaseDB();

        imgShowPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edtPassword.length() > 0) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return true;
                        case MotionEvent.ACTION_UP:
                            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                    }
                }
                return false;
            }
        });

        auth = FirebaseAuth.getInstance();

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentCreateAccount());
            }
        });

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentForgot());
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtUsername.getText().toString();
                password = edtPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    edtUsername.setError(getString(R.string.enter_email));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError(getString(R.string.enter_password));
                    return;
                }

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        edtPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    List<User> userList = firebaseDB.getAllUser();
                                    for (User user : userList) {
                                        if (email.equals(user.getUsername())) {
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            intent.putExtra("user", user);
                                            startActivity(intent);
                                            getActivity().finish();
                                            break;
                                        }
                                    }
                                }
                            }
                        });
            }
        });

        return view;
    }

    private void init(View view) {
        edtUsername = view.findViewById(R.id.txt_username_login);
        edtPassword = view.findViewById(R.id.txt_password_login);
        txtCreateAccount = view.findViewById(R.id.txt_create_account);
        imgShowPassword = view.findViewById(R.id.img_show_password_login);
        btnLogin = view.findViewById(R.id.button_login);
        txtForgot = view.findViewById(R.id.txt_forgot);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_login, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
