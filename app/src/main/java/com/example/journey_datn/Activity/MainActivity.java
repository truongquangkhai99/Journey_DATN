package com.example.journey_datn.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.journey_datn.R;
import com.example.journey_datn.fragment.FragmentAtlas;
import com.example.journey_datn.fragment.FragmentCalendar;
import com.example.journey_datn.fragment.FragmentJourney;
import com.example.journey_datn.fragment.FragmentMedia;
import com.example.journey_datn.fragment.Weather.FragmentWeather;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import static com.example.journey_datn.Activity.SearchActivity.RESULT_CODE;

public class MainActivity extends AppCompatActivity{

    private BottomNavigationView navigationView;
    private ImageView img_search, img_cloud;
    private int WRITE_EXTERNAL_STORAGE_CODE = 1, MY_CAMERA_PERMISSION_CODE = 2, READ_EXTERNAL_STORAGE_CODE = 3;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private int idClick, idFragment;
    public static int userId;
    public static String firstName, lastName;
    private NavigationView navi;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissions();
        init();
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        loadFragment(new FragmentJourney());
        idFragment = 1;
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.menu_journey:
                        fragment = new FragmentJourney();
                        loadFragment(fragment);
                        idFragment = 1;
                        return true;
                    case R.id.menu_calendar:
                        fragment = new FragmentCalendar();
                        loadFragment(fragment);
                        idFragment = 2;
                        return true;
                    case R.id.menu_media:
                        fragment = new FragmentMedia();
                        loadFragment(fragment);
                        idFragment = 3;
                        return true;
                    case R.id.menu_atlas:
                        fragment = new FragmentAtlas();
                        loadFragment(fragment);
                        idFragment = 4;
                        return true;
                    case R.id.menu_weather:
                        fragment = new FragmentWeather();
                        loadFragment(fragment);
                        idFragment = 5;
                        return true;
                }
                return false;
            }
        });

        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_diary:
                        Intent intent = new Intent(MainActivity.this, DairyActivity.class);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idClick = idFragment;
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivityForResult(intent, 10);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_CODE){
            boolean check = data.getBooleanExtra("checkUpdate", false);
            if (check){
                Fragment fragment;
                switch (idClick){
                    case 1:
                        fragment = new FragmentJourney();
                        loadFragment(fragment);
                        return;
                    case 2:
                        fragment = new FragmentCalendar();
                        loadFragment(fragment);
                        return;
                    case 3:
                        fragment = new FragmentMedia();
                        loadFragment(fragment);
                        return;
                    case 4:
                        fragment = new FragmentAtlas();
                        loadFragment(fragment);
                        return;
                    case 5:
                       fragment = new FragmentWeather();
                       loadFragment(fragment);
                       return;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        loadFragment(new FragmentJourney());
        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setVisibility(View.VISIBLE);
        navigationView.setVisibility(View.VISIBLE);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout_contain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permissions() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
        }
        if (requestCode == READ_EXTERNAL_STORAGE_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "external permission granted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "external permission denied", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        navigationView = findViewById(R.id.navigation);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        img_search = findViewById(R.id.img_search);
        img_cloud = findViewById(R.id.img_cloud);
        toolbar = findViewById(R.id.toolbar);
        navi = findViewById(R.id.nav_view);
        Intent intent = getIntent();
        userId = intent.getIntExtra("userId", -1);
        firstName = intent.getStringExtra("firstName") + "";
        lastName = intent.getStringExtra("lastName") + "";
        if (firstName.equals("null")) firstName = "";
        if (lastName.equals("null")) lastName = "";
        navi.getMenu().getItem(0).setTitle(lastName + " " + firstName);
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

