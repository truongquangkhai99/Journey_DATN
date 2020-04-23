package com.example.journey_datn.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
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

        Glide.with(getContext()).load(this.getStr()).into(imageView);
        return view;
    }
}

//public class FragmentImage extends Fragment implements View.OnTouchListener {
//    private ImageView imageView;
//    String str;
//
//    private ScaleGestureDetector scaleGestureDetector;
//    private float mScaleFactor = 1.0f;
//
//
//    public FragmentImage(String str) {
//        this.str = str;
//    }
//
//    public String getStr() {
//        return str;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_image, container, false);
//        imageView = view.findViewById(R.id.imageView);
//        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
//
//        Glide.with(getContext()).load(this.getStr()).into(imageView);
//        return view;
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        scaleGestureDetector.onTouchEvent(event);
//        return true;
//    }
//
//
//    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
//        @Override
//        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
//            mScaleFactor *= scaleGestureDetector.getScaleFactor();
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//            imageView.setScaleX(mScaleFactor);
//            imageView.setScaleY(mScaleFactor);
//            return true;
//        }
//    }
//}



