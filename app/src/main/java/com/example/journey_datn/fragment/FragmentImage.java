package com.example.journey_datn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.journey_datn.R;

public class FragmentImage extends Fragment {
    private ImageView imageView;
    String str;

    public FragmentImage(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.imageView);

        String s = this.getStr();
        Glide.with(getContext()).load(this.getStr()).into(imageView);
        return view;
    }
}
