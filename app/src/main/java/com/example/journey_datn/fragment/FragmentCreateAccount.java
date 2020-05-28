package com.example.journey_datn.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.example.journey_datn.db.BranchDBFB;
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentCreateAccount extends Fragment {

    private EditText edtFirstName, edtLastName, edtUsername, edtPassword, edtConfirmPassword;
    private Button btnCreateAc;
    private ImageView imgShowPassword, imgShowConfirmPw;
    private FirebaseAuth auth;
    private FirebaseDB firebaseDB = new FirebaseDB();
    private String email, password, strFirstName, strLastName, strConfirmPW;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);
        init(view);

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


        auth = FirebaseAuth.getInstance();
        btnCreateAc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edtUsername.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
                strFirstName = edtFirstName.getText().toString().trim();
                strLastName = edtLastName.getText().toString().trim();
                strConfirmPW = edtConfirmPassword.getText().toString().trim();
                if (TextUtils.isEmpty(strFirstName)) {
                    edtFirstName.setError(getString(R.string.firstName));
                    return;
                }
                if (TextUtils.isEmpty(strLastName)) {
                    edtLastName.setError(getString(R.string.lastName));
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    edtUsername.setError(getString(R.string.enter_email));
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    edtPassword.setError(getString(R.string.enter_password));
                    return;
                }
                if (password.length() < 6) {
                    edtPassword.setError(getString(R.string.minimum_password));
                    return;
                }
                if (TextUtils.isEmpty(strConfirmPW)) {
                    edtConfirmPassword.setError(getString(R.string.confirm_password));
                    return;
                }
                if (!password.equals(strConfirmPW)) {
                    edtConfirmPassword.setError(getString(R.string.wrong_password));
                    return;
                }

                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(getContext(), getString(R.string.invalid_info), Toast.LENGTH_SHORT).show();
                                } else createAccount();
                            }
                        });
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

    /**
     * tạo tài khoản và chuyển ngay sang màn hình MainActivity
     */
    private void createAccount() {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = mDatabase.push().getKey();
        User user = new User(userId, strFirstName, strLastName, email);
        firebaseDB.insertUser(user);
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        getActivity().finish();
    }
}
