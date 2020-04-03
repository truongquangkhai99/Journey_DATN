package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Model.User;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;
import com.example.journey_datn.db.UserRepository;

import java.util.List;

public class FragmentCreateAccount extends Fragment {
    private EditText edtFirstName, edtLastName, edtUsername, edtPassword, edtConfirmPassword;
    private Button btnCreateAc;
    private ImageView imgShowPassword, imgShowConfirmPw;
    private UserRepository userRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        init(view);

        userRepository = new UserRepository(getContext());
        btnCreateAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

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

        imgShowConfirmPw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edtConfirmPassword.length() > 0) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            return true;
                        case MotionEvent.ACTION_UP:
                            edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    private void init(View view) {
        edtFirstName = view.findViewById(R.id.edt_first_nameAc);
        edtLastName = view.findViewById(R.id.edt_last_nameAc);
        edtUsername = view.findViewById(R.id.edt_usernameAc);
        edtPassword = view.findViewById(R.id.edt_passwordAc);
        edtConfirmPassword = view.findViewById(R.id.edt_confirm_passwordAc);
        btnCreateAc = view.findViewById(R.id.button_create_account);
        imgShowPassword = view.findViewById(R.id.img_show_password);
        imgShowConfirmPw = view.findViewById(R.id.img_show_confirm_password);
    }

    private void createAccount() {
        String strFirstName = edtFirstName.getText().toString();
        String strLastName = edtLastName.getText().toString();
        String strUserName = edtUsername.getText().toString();
        String strPassWord = edtPassword.getText().toString();
        String strConfirmPW = edtConfirmPassword.getText().toString();
        boolean checkAccount = false;

        List<User> userList = userRepository.getUser();
        if (!strFirstName.equals("") && !strLastName.equals("") && !strUserName.equals("") && !strPassWord.equals("") && !strConfirmPW.equals("")) {
            if (strPassWord.equals(strConfirmPW)) {
                for (User userItem : userList) {
                    if (strUserName.equals(userItem.getUsername())) {
                        Toast.makeText(getContext(), "Account already exists", Toast.LENGTH_SHORT).show();
                        checkAccount = true;
                        break;
                    }
                }
                if (!checkAccount) {
                    User user = new User(strFirstName, strLastName, strUserName, strPassWord);
                    userRepository.insertUser(user);
                    userList = userRepository.getUser();
                    for (User userItem : userList)
                        if (strUserName.equals(userItem.getUsername()) && strPassWord.equals(userItem.getPassword())) {
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.putExtra("userId", userItem.getId());
                            intent.putExtra("firstName", userItem.getFirstName());
                            intent.putExtra("lastName", userItem.getLastName());
                            startActivity(intent);
                            getActivity().finish();
                            break;
                        }
                }

            }else Toast.makeText(getContext(), "Confirm password is wrong", Toast.LENGTH_SHORT).show();
        }else  Toast.makeText(getContext(), "You must enter all fields", Toast.LENGTH_SHORT).show();
    }
}
