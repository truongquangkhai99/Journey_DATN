package com.example.journey_datn.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.journey_datn.Adapter.SectionsPagerAdapter;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentJourney;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private BottomNavigationView navigationView;
    private ImageView img_menu, img_search, img_cloud;
    private int READ_EXTERNAL_STORAGE_CODE = 1, MY_CAMERA_PERMISSION_CODE = 2;

    private DrawerLayout mDrawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        init();
        fragmentList.add(new FragmentJourney());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_journey:
                        mViewPager.setCurrentItem(0);
                        return  true;
                    case  R.id.menu_calendar:
                        mViewPager.setCurrentItem(1);
                        return  true;
                    case  R.id.menu_media:
                        mViewPager.setCurrentItem(2);
                        return  true;
                    case  R.id.menu_atlas:
                        mViewPager.setCurrentItem(3);
                        return  true;
                    case  R.id.menu_today:
                        mViewPager.setCurrentItem(4);
                        return  true;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private  void permissions(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_EXTERNAL_STORAGE_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }
    }

    private void init(){
        mViewPager =  findViewById(R.id.viewPager_contain);
        navigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        img_search = findViewById(R.id.img_search);
        img_cloud = findViewById(R.id.img_cloud);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

