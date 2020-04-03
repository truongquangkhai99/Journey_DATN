package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.journey_datn.db.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FragmentLogin extends Fragment {
    private EditText edtUsername, edtPassword;
    private TextView txtWrongUserName, txtWrongPassword, txtCreateAccount;
    private ImageView imgShowPasswWord;
    private Button btnLogin;
    private UserRepository userRepository;
    private List<User> userList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        init(view);

        userRepository = new UserRepository(getContext());

        imgShowPasswWord.setOnTouchListener(new View.OnTouchListener() {
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

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();
                if (!username.equals("") && !password.equals("")){
                    userList = userRepository.getUser();
                    for (User user : userList)
                        if (username.equals(user.getUsername()) && password.equals(user.getPassword())){
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("userId", user.getId());
                            intent.putExtra("firstName", user.getFirstName());
                            intent.putExtra("lastName", user.getLastName());
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        }else Toast.makeText(getContext(), "Account does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new FragmentCreateAccount());
            }
        });

        return view;
    }

    private void init(View view){
        edtUsername = view.findViewById(R.id.txt_username_login);
        edtPassword = view.findViewById(R.id.txt_password_login);
        txtWrongUserName = view.findViewById(R.id.txt_wrong_username);
        txtWrongPassword = view.findViewById(R.id.txt_wrong_password);
        txtCreateAccount = view.findViewById(R.id.txt_create_account);
        imgShowPasswWord = view.findViewById(R.id.img_show_password_login);
        btnLogin = view.findViewById(R.id.button_login);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_login, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
