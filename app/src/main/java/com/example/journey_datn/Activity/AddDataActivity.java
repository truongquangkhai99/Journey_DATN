package com.example.journey_datn.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.MyOptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Adapter.AdapterRcvAdd;
import com.example.journey_datn.Model.Entity;
import com.example.journey_datn.R;
import com.example.journey_datn.db.EntityRepository;
import com.example.journey_datn.fragment.Weather.interfaces.IWeatherApi;
import com.example.journey_datn.fragment.Weather.models.OpenWeatherModel;
import com.example.journey_datn.fragment.Weather.utils.ApiService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.journey_datn.fragment.Weather.constants.ProjectConstants.BASE_URL_OPEN_WEATHER;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterRcvAdd.OnItemClickListener {

    private ImageView img_mark, img_calendar_add, img_tag_add, img_three_dots_add;
    private TextView txt_day_add, txt_month_add, txt_year_add, txt_hour_add, txt_minute_add;
    private EditText edt_contain_add;
    private RecyclerView rcv_add;
    private AdapterRcvAdd adapterRcvAdd;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private int mYear, mMonth, mDay, mHour, mMinute, positionUpdate, id = -1;
    private Entity entityUpdate;
    private String position = "", srcImage = "", th, contain, urri_random;
    private int day, month, year, hour, minute, temperature = 0, action = R.drawable.ic_action_black_24dp, mood = R.drawable.ic_mood_black_24dp;
    private static final int CAMERA_CODE = 0, GALLERY_CODE = 1;
    private int mposition;
    public static int RESULT_CODE = 113;
    private int PERMISSION_ID = 44;
    private double latitude, longtitude;
    private FusedLocationProviderClient mFusedLocationClient;
    private String OPEN_WEATHER_APP_ID = "b317aca2e83ad16e219ff2283ca837d5";
    private static IWeatherApi mWeatherApi;
    private boolean checkUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        init();
        checkUpdate = false;
        adapterRcvAdd = new AdapterRcvAdd(this);
        adapterRcvAdd.setListener(this);
        rcv_add.setLayoutManager(linearLayoutManager);
        rcv_add.setAdapter(adapterRcvAdd);
        img_mark.setOnClickListener(this);
        img_three_dots_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        img_calendar_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker();
            }
        });

        getCalendar();
        txt_day_add.setText(mDay + "");
        txt_month_add.setText(mMonth + "");
        txt_year_add.setText(mYear + "");
        txt_hour_add.setText(mHour + "");
        txt_minute_add.setText(mMinute + "");
        urri_random = UUID.randomUUID() + ".jpg";

        getDataFromDetail();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (!checkUpdate) {
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
                                        longtitude = location.getLongitude();
                                        setLatitude(latitude);
                                        setLongtitude(longtitude);
                                        Geocoder geocoder;
                                        List<Address> addresses;
                                        geocoder = new Geocoder(AddDataActivity.this, Locale.getDefault());
                                        try {
                                            String address, state;
                                            addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                                            address = addresses.get(0).getAddressLine(0);
                                            state = addresses.get(0).getAdminArea();
                                            position = address;
                                            String tp = removeAccent(state);
                                            getOpenWeatherData(tp, OPEN_WEATHER_APP_ID);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                    );
                } else {
                    Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                requestPermissions();
            }
        }
    }

    public void getOpenWeatherData(String city, String appId) {
        mWeatherApi = ApiService.getRetrofitInstance(BASE_URL_OPEN_WEATHER).create(IWeatherApi.class);
        Call<OpenWeatherModel> resForgotPasswordCall = mWeatherApi.getOpenWeatherData(appId, city);
        resForgotPasswordCall.enqueue(new Callback<OpenWeatherModel>() {
            @Override
            public void onResponse(Call<OpenWeatherModel> call, Response<OpenWeatherModel> response) {
                if (response.body() != null) {
                    OpenWeatherModel openWeatherModel = response.body();
                    temperature = (int) openWeatherModel.getMain().getTemp();
                }
            }

            @Override
            public void onFailure(Call<OpenWeatherModel> call, Throwable t) {

            }
        });
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
            longtitude = mLastLocation.getLongitude();
            setLatitude(mLastLocation.getLatitude());
            setLongtitude(mLastLocation.getLongitude());
        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

    private void getCalendar() {
        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        getDayofMonth(mDay, mMonth, mYear);
        mMonth = mMonth + 1;
    }

    private void getDayofMonth(int day, int month, int year) {
        day = day - 1;
        Date date = new Date(year, month, day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                th = "Sunday";
                break;
            case Calendar.MONDAY:
                th = "Monday";
                break;
            case Calendar.TUESDAY:
                th = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                th = "Wednesday";
                break;
            case Calendar.THURSDAY:
                th = "Thursday";
                break;
            case Calendar.FRIDAY:
                th = "Friday";
                break;
            case Calendar.SATURDAY:
                th = "Saturday";
                break;
        }
    }

    private void dateTimePicker() {
        getCalendar();
        mMonth = mMonth - 1;
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txt_hour_add.setText(hourOfDay + "");
                txt_minute_add.setText(minute + "");
            }
        }, mHour, mMinute, false);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                txt_day_add.setText(dayOfMonth + "");
                int month = monthOfYear + 1;
                txt_month_add.setText(month + "");
                txt_year_add.setText(year + "");

                getDayofMonth(dayOfMonth, monthOfYear, year);
                timePickerDialog.show();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard");
        builder.setMessage("Do you want to discard the changes?");
        builder.setCancelable(false);
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init() {
        img_mark = findViewById(R.id.img_mark);
        img_calendar_add = findViewById(R.id.img_calendar_add);
        img_tag_add = findViewById(R.id.img_tag_add);
        img_three_dots_add = findViewById(R.id.img_three_dots_add);
        edt_contain_add = findViewById(R.id.edt_contain_add);
        txt_day_add = findViewById(R.id.txt_day_add);
        txt_month_add = findViewById(R.id.txt_month_add);
        txt_year_add = findViewById(R.id.txt_year_add);
        txt_hour_add = findViewById(R.id.txt_hour_add);
        txt_minute_add = findViewById(R.id.txt_minute_add);
        rcv_add = findViewById(R.id.rcv_add);
    }

    @Override
    public void onClick(View v) {
        contain = edt_contain_add.getText().toString();
        day = Integer.parseInt(txt_day_add.getText().toString());
        month = Integer.parseInt(txt_month_add.getText().toString());
        year = Integer.parseInt(txt_year_add.getText().toString());
        hour = Integer.parseInt(txt_hour_add.getText().toString());
        minute = Integer.parseInt(txt_minute_add.getText().toString());

        if (TextUtils.isEmpty(contain)) {
            Toast.makeText(this, "No contain", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            Entity entity;
            if (id == -1)
                entity = new Entity(contain, action, position, temperature, year, month, day, th, hour, minute, mood, srcImage, getLatitude(), getLongtitude());
            else
                entity = new Entity(id, contain, action, position, temperature, year, month, day, th, hour, minute, mood, srcImage, getLatitude(), getLongtitude());
            intent.putExtra("entity", entity);
            setResult(RESULT_CODE, intent);
            finish();
        }
    }

    @Override
    public void OnItemClick(int position) {
        mposition = position;
        switch (position) {
            case 0:
                mediaClick();
                break;
            case 1:
                placeClick();
                break;
            case 2:
                temperatureClick();
                break;
            case 3:
                pickerView();
                break;
            case 4:
                faceClick();
                break;
            case 5:
                boldClick();
                break;
            case 6:
                italicClick();
                break;
            case 7:
                underlineClick();
                break;
            case 8:
                backClick();
                break;
            case 9:
                forwardClick();
                break;
        }
    }

    private void mediaClick() {
        final View dialogView = View.inflate(this, R.layout.dialog_media, null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        ImageView imgGallery = dialog.findViewById(R.id.img_dl_gallery);
        ImageView imgFile = dialog.findViewById(R.id.img_dl_file);
        ImageView imgPhoto = dialog.findViewById(R.id.img_dl_photo);
        ImageView imgVideo = dialog.findViewById(R.id.img_dl_video);
        ImageView imgMicrophone = dialog.findViewById(R.id.img_dl_microphone);

        dialog.show();
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_CODE);
                dialog.dismiss();
            }
        });

        imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose file", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = new File(Environment.getExternalStorageDirectory(), urri_random);
                Uri uri = FileProvider.getUriForFile(AddDataActivity.this, AddDataActivity.this.getApplicationContext().getPackageName() + ".provider", file);
                m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(m_intent, CAMERA_CODE);
                dialog.dismiss();
            }
        });
        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose video", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgMicrophone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose microphone", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_CODE:
                File file = new File(Environment.getExternalStorageDirectory(), urri_random);
                Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                Glide.with(this).load(uri).into(img_tag_add);
                srcImage = uri.toString();
                break;
            case GALLERY_CODE:
                try {
                    if (null != data) {
                        srcImage = "";
                        if (data.getData() != null) {

                            Uri mImageUri = data.getData();
                            ArrayList<Uri> mArrayUri = new ArrayList<>();
                            mArrayUri.add(mImageUri);
                            for (Uri uri1 : mArrayUri) {
                                srcImage = srcImage + uri1.toString();
                            }
                            Glide.with(this).load(srcImage).into(img_tag_add);

                        } else {
                            if (data.getClipData() != null) {
                                ClipData mClipData = data.getClipData();
                                ArrayList<Uri> mArrayUri = new ArrayList<>();
                                for (int i = 0; i < mClipData.getItemCount(); i++) {
                                    ClipData.Item item = mClipData.getItemAt(i);
                                    Uri uri2 = item.getUri();
                                    mArrayUri.add(uri2);
                                }
                                for (Uri uri1 : mArrayUri) {
                                    srcImage = srcImage + uri1.toString() + ";";
                                }
                                String[] separated = srcImage.split(";");
                                Glide.with(this).load(separated[0]).into(img_tag_add);
                            }
                        }
                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                            .show();
                }
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    private void getDataFromDetail() {
        Intent intent = getIntent();
        int activity = intent.getIntExtra("activity", 0);
        if (activity == 2) {
            checkUpdate = true;
            entityUpdate = intent.getParcelableExtra("entityUpdate");
            positionUpdate = intent.getIntExtra("positionUpdate", 0);
            id = entityUpdate.getId();

            txt_day_add.setText(String.valueOf(entityUpdate.getDay()));
            txt_month_add.setText(String.valueOf(entityUpdate.getMonth()));
            txt_year_add.setText(String.valueOf(entityUpdate.getYear()));
            txt_hour_add.setText(String.valueOf(entityUpdate.getHour()));
            txt_minute_add.setText(String.valueOf(entityUpdate.getMinute()));
            edt_contain_add.setText(entityUpdate.getContent());

            position = entityUpdate.getStrPosition();
            srcImage = entityUpdate.getSrcImage();
            temperature = entityUpdate.getTemperature();
            action = entityUpdate.getAction();
            mood = entityUpdate.getMood();
            th = entityUpdate.getTh();

            adapterRcvAdd.updateItem(3, action);
            adapterRcvAdd.updateItem(4, mood);
            String[] separated = srcImage.split(";");
            if (separated.length == 1){
                Glide.with(this).load(srcImage).into(img_tag_add);
            }else {
                Glide.with(this).load(separated[0]).into(img_tag_add);
            }
        }
    }

    private void placeClick() {
        final View dialogView = View.inflate(this, R.layout.dialog_pick_place, null);
        final Dialog dialog = new Dialog(this);
        TextView txtPlace = dialogView.findViewById(R.id.txt_dl_place_pick_place);
        txtPlace.setText(position);
        dialog.setContentView(dialogView);

        ImageView imgPick = dialog.findViewById(R.id.img_dl_pick_place);
        ImageView imgRename = dialog.findViewById(R.id.img_dl_rename_place_pick_place);
        ImageView imgRemove = dialog.findViewById(R.id.img_dl_remove_place);
        ImageView imgSetup = dialog.findViewById(R.id.img_dl_setup_gps);

        imgPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose pick", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose rename", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose remove", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        imgSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AddDataActivity.this, "choose setup", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void pickerView() {
        MyOptionsPickerView singlePicker = new MyOptionsPickerView(AddDataActivity.this);
        final ArrayList<String> items = new ArrayList<>();
        items.add("None");
        items.add("Stationary");
        items.add("Eating");
        items.add("Walking");
        items.add("Running");
        items.add("Biking");
        items.add("Automotive");
        items.add("Flying");
        singlePicker.setPicker(items);
        singlePicker.setTitle("Activity");
        singlePicker.setCyclic(false);
        singlePicker.setSelectOptions(0);
        singlePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                if (items.get(options1) == items.get(0)) {
                    action = R.drawable.ic_action_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(1)) {
                    action = R.drawable.ic_accessibility_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(2)) {
                    action = R.drawable.ic_restaurant_menu_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(3)) {
                    action = R.drawable.ic_directions_walk_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(4)) {
                    action = R.drawable.ic_directions_run_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(5)) {
                    action = R.drawable.ic_directions_bike_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(6)) {
                    action = R.drawable.ic_directions_car_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
                if (items.get(options1) == items.get(7)) {
                    action = R.drawable.ic_airplanemode_active_black_24dp;
                    adapterRcvAdd.updateItem(mposition, action);
                }
            }
        });

        singlePicker.show();
    }

    private void temperatureClick() {
        final View dialogView = View.inflate(this, R.layout.dialog_temperature, null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void faceClick() {
        final View dialogView = View.inflate(this, R.layout.dialog_face, null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        ImageView imgHeart = dialog.findViewById(R.id.img_dl_heart);
        ImageView imgHappy = dialog.findViewById(R.id.img_dl_happy);
        ImageView imgGrinning = dialog.findViewById(R.id.img_dl_grinning);
        ImageView imgSad = dialog.findViewById(R.id.img_dl_sad);
        ImageView imgNeutral = dialog.findViewById(R.id.img_dl_neutral);

        imgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_favorite_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_happy_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgGrinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_mood_emoticon_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_sad_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.ic_neutral_red_24dp;
                adapterRcvAdd.updateItem(mposition, mood);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void boldClick() {
        Toast.makeText(AddDataActivity.this, "click bold", Toast.LENGTH_SHORT).show();
    }

    private void italicClick() {
        Toast.makeText(AddDataActivity.this, "click italick", Toast.LENGTH_SHORT).show();
    }

    private void underlineClick() {
        Toast.makeText(AddDataActivity.this, "click underline", Toast.LENGTH_SHORT).show();
    }

    private void backClick() {
        Toast.makeText(AddDataActivity.this, "click back", Toast.LENGTH_SHORT).show();
    }

    private void forwardClick() {
        Toast.makeText(AddDataActivity.this, "click forward", Toast.LENGTH_SHORT).show();
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
