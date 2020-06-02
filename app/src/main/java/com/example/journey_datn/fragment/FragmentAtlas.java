package com.example.journey_datn.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.journey_datn.Activity.MainActivity;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.FirebaseDB;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentAtlas extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private int PERMISSION_ID = 44;
    private double latitude, longitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private ArrayList<Entity> lstEntity;
    private String knownName = "", roadName = "";
    private FloatingActionButton fabZoom, fabZoomIn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        lstEntity = MainActivity.entityList;
        View view = inflater.inflate(R.layout.fragment_atlas, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabZoom = view.findViewById(R.id.fab_enlarge);
        fabZoomIn = view.findViewById(R.id.fab_zoom_in);

        fabZoom.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(6), 2000, null);
                fabZoom.setVisibility(View.INVISIBLE);
                fabZoomIn.setVisibility(View.VISIBLE);
            }
        });

        fabZoomIn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
                fabZoom.setVisibility(View.VISIBLE);
                fabZoomIn.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        getLastLocation();
        getAllPosition();
    }

    /**
     * lấy ra tọa độ vị trí hiện tại
     */
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    getLatLng(latitude, longitude);
                                    LatLng currentLocation = new LatLng(latitude, longitude);
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
                                    mMap.addMarker(new MarkerOptions()
                                            .title(knownName + ", " + roadName)
                                            .position(currentLocation));
                                }
                            }
                        }
                );
            } else {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void getLatLng(double lat, double lng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.get(0).getFeatureName() != null && addresses.get(0).getThoroughfare() != null){
                knownName = addresses.get(0).getFeatureName();
                roadName = addresses.get(0).getThoroughfare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * lấy ra tất cả các vị trí trong danh sách phần tử
     */
    private void getAllPosition() {
        if (lstEntity.size() > 0) {
            List<LatLng> locations = new ArrayList<>();
            Set<String> listCountryName = new HashSet<>();
            int count;
            for (Entity entity : lstEntity) {  //lấy ra list tên quốc ra
                if (!entity.getStrPosition().equals("")) {
                    String[] pos = entity.getStrPosition().split(",");
                    String name = pos[pos.length - 1];
                    listCountryName.add(name);
                }
            }

            for (String item : listCountryName) { // gộp những item có cùng 1 quốc gia lại thành 1 list riêng
                count = 0;
                List<Entity> listCountry = new ArrayList<>();
                for (Entity entity : lstEntity) {
                    if (!entity.getStrPosition().equals("") && !entity.getSrcImage().equals("")) {
                        String[] pos = entity.getStrPosition().split(",");
                        String name = pos[pos.length - 1];
                        if (name.equalsIgnoreCase(item)) {
                            listCountry.add(entity);
                        }
                    }
                }

                Collections.sort(listCountry, new Comparator<Entity>() { // sắp xếp list quốc gia theo thời gian
                    @Override
                    public int compare(Entity o1, Entity o2) {
                        Calendar c1 = Calendar.getInstance();
                        Calendar c2 = Calendar.getInstance();

                        String strDate[] = o1.getStrDate().split("-");
                        String strYear[] = strDate[2].split(" ");
                        String strHour[] = strYear[1].split(":");
                        int hour = Integer.parseInt(strHour[0]);
                        int minute = Integer.parseInt(strHour[1]);

                        String strDate2[] = o2.getStrDate().split("-");
                        String strYear2[] = strDate2[2].split(" ");
                        String strHour2[] = strYear2[1].split(":");
                        int hour2 = Integer.parseInt(strHour2[0]);
                        int minute2 = Integer.parseInt(strHour2[1]);

                        c1.set(o1.getYear(), o1.getMonth() - 1, o1.getDay(), hour, minute);
                        c2.set(o2.getYear(), o2.getMonth() - 1, o2.getDay(), hour2, minute2);

                        return (int) (c1.getTimeInMillis()  - c2.getTimeInMillis());
                    }
                });

                for (Entity entity : listCountry){
                    count = count + 1;
                    getLatLng(entity.getLat(), entity.getLng());
                    LatLng location = new LatLng(entity.getLat(), entity.getLng());
                    locations.add(location);

                    String arrSrc = entity.getSrcImage();
                    String[] separated = arrSrc.split(";");
                    mMap.addMarker(new MarkerOptions()
                            .title(knownName + ", " + roadName)
                            .position(location)
                            .icon(BitmapDescriptorFactory.fromBitmap(createMaker(getContext(), separated[0], count))));
                }
            }
            if (locations.size() > 0) {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(locations.get(0)); //point A
                builder.include(locations.get(locations.size() - 1)); //point B
                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
            }
        }
    }

    /**
     * tạo Maker dạng CircleImageView
     *
     * @param context
     * @param resource
     * @return
     */
    public static Bitmap createMaker(Context context, String resource, int num) {
        View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_marker_layout, null);
        CircleImageView markerImage = marker.findViewById(R.id.user_dp);
        TextView txtNum = marker.findViewById(R.id.number);
        txtNum.setText(num + "");
        markerImage.setImageURI(Uri.parse(resource));
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);

        return bitmap;
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

}
