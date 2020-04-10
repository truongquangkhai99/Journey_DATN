package com.example.journey_datn.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.graphics.Paint;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.example.journey_datn.Model.bsimagepicker.BSImagePicker;
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

import org.w3c.dom.Text;

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

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterRcvAdd.OnItemClickListener, BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener, BSImagePicker.ImageLoaderDelegate, BSImagePicker.OnSelectImageCancelledListener {

    private ImageView img_mark,  img_tag_add, img_three_dots_add;
    private TextView txtDate;
    private EditText edt_content_add;
    private RecyclerView rcv_add;
    private AdapterRcvAdd adapterRcvAdd;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private int positionUpdate, id = -1, mDay, mMonth, mYear, mMinute, mHour;
    private Entity entityUpdate;
    private String position = "", srcImage = "", th, content, desWeather = "", textStyle = "N";
    private int temperature = 0, action = R.drawable.ic_action_black_24dp, mood = R.drawable.ic_mood_black_24dp,
            star = R.drawable.ic_star_border_black_24dp;
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

        getCalendar();
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateTimePicker();
            }
        });

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
                    desWeather = openWeatherModel.getWeather().get(0).getDescription();
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
        Date date = c.getTime();
        SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String today = format1.format(date);
        txtDate.setText(today);
        String arrToDay[] = today.split("-");
        mDay = Integer.parseInt(arrToDay[0]);
        mMonth = Integer.parseInt(arrToDay[1]);
        String strYear[] = arrToDay[2].split(" ");
        String strHour[] = strYear[1].split(":");
        mYear = Integer.parseInt(strYear[0]);
        mHour = Integer.parseInt(strHour[0]);
        mMinute = Integer.parseInt(strHour[1]);
        getDayofMonth(mDay, mYear, mMonth);
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
                mHour = hourOfDay;
                mMinute = minute;
                Calendar c = Calendar.getInstance();
                c.set(mYear, mMonth, mDay, mHour, mMinute);
                Date date = c.getTime();
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                txtDate.setText(format1.format(date));
                mMonth = mMonth + 1;
            }
        }, mHour, mMinute, false);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mDay = dayOfMonth;
                mMonth = monthOfYear;
                mYear = year;
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
        img_tag_add = findViewById(R.id.img_tag_add);
        img_three_dots_add = findViewById(R.id.img_three_dots_add);
        edt_content_add = findViewById(R.id.edt_content_add);
        txtDate = findViewById(R.id.txt_date_add);
        rcv_add = findViewById(R.id.rcv_add);
    }

    @Override
    public void onClick(View v) {
        content = edt_content_add.getText().toString();
        String strDate = txtDate.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "No content", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            Entity entity;
            if (id == -1)
                entity = new Entity(content, textStyle, action, position, temperature, strDate, mDay, mMonth, mYear, th, mood, star,srcImage, getLatitude(), getLongtitude(), MainActivity.userId);
            else
                entity = new Entity(id, content, textStyle, action, position, temperature, strDate, mDay, mMonth, mYear, th, mood, star, srcImage, getLatitude(), getLongtitude(), MainActivity.userId);
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
                clearFormat();
                break;
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSingleImageSelected(Uri uri, String tag) {
        Glide.with(this).load(uri).into(img_tag_add);
        srcImage = uri.toString();
        getLocationPhoto(uri);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getLocationPhoto(Uri uri){
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(getContentResolver().openInputStream(uri));
            float[] latLong = new float[2];
            boolean hasLatLong = exif.getLatLong(latLong);
            if (hasLatLong) {
                latitude = latLong[0];
                longtitude = latLong[1];
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(AddDataActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                    position = addresses.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        srcImage = "";
        for (int i = 0; i < uriList.size(); i++) {
            if (i >= 6) return;
            srcImage = srcImage + uriList.get(i).toString() + ";";
        }
        Glide.with(this).load(uriList.get(0)).into(img_tag_add);
        for (int i = uriList.size()- 1; i >= 0; i--)
            getLocationPhoto(uriList.get(i));
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {
        Glide.with(AddDataActivity.this).load(imageUri).into(ivImage);
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {
        Toast.makeText(this, "Selection is cancelled ", Toast.LENGTH_SHORT).show();
    }

    private void getDataFromDetail() {
        Intent intent = getIntent();
        int activity = intent.getIntExtra("activity", 0);
        if (activity == 2) {
            checkUpdate = true;
            entityUpdate = intent.getParcelableExtra("entityUpdate");
            positionUpdate = intent.getIntExtra("positionUpdate", 0);
            id = entityUpdate.getId();
            txtDate.setText(String.valueOf(entityUpdate.getStrDate()));
            edt_content_add.setText(entityUpdate.getContent());

            position = entityUpdate.getStrPosition();
            srcImage = entityUpdate.getSrcImage();
            temperature = entityUpdate.getTemperature();
            action = entityUpdate.getAction();
            mood = entityUpdate.getMood();
            star = entityUpdate.getStar();
            th = entityUpdate.getTh();
            latitude = entityUpdate.getLat();
            longtitude = entityUpdate.getLng();

            adapterRcvAdd.updateItem(3, action);
            adapterRcvAdd.updateItem(4, mood);
            String[] separated = srcImage.split(";");
            if (separated.length == 1) {
                Glide.with(this).load(srcImage).into(img_tag_add);
            } else {
                Glide.with(this).load(separated[0]).into(img_tag_add);
            }

            String strDate[] = entityUpdate.getStrDate().split("-");
            String strYear[] = strDate[2].split(" ");
            mDay = Integer.parseInt(strDate[0]);
            mMonth = Integer.parseInt(strDate[1]);
            mYear = Integer.parseInt(strYear[0]);

            if (entityUpdate.getTextStyle().equals("B"))
                edt_content_add.setTypeface(edt_content_add.getTypeface(), Typeface.BOLD);
            if (entityUpdate.getTextStyle().equals("N"))
                edt_content_add.setTypeface(Typeface.DEFAULT);
            if (entityUpdate.getTextStyle().equals("I"))
                edt_content_add.setTypeface(edt_content_add.getTypeface(), Typeface.ITALIC);
            if (entityUpdate.getTextStyle().equals("U"))
                edt_content_add.setPaintFlags(edt_content_add.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }
    }

    private void mediaClick() {

        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_media);
        dialog.setCancelable(true);

        ImageView imgGallery = dialog.findViewById(R.id.img_dl_gallery);
        ImageView imgFile = dialog.findViewById(R.id.img_dl_file);
        ImageView imgPhoto = dialog.findViewById(R.id.img_dl_photo);
        ImageView imgVideo = dialog.findViewById(R.id.img_dl_video);
        ImageView imgMicrophone = dialog.findViewById(R.id.img_dl_microphone);

        dialog.show();
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.journey_datn.provider")
                        .setMaximumDisplayingImages(Integer.MAX_VALUE)
                        .isMultiSelect()
                        .setMinimumMultiSelectCount(2)
                        .setMaximumMultiSelectCount(6)
                        .build();
                pickerDialog.show(getSupportFragmentManager(), "picker");
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
                BSImagePicker pickerDialog = new BSImagePicker.Builder("com.example.journey_datn.provider").build();
                pickerDialog.show(getSupportFragmentManager(), "picker");
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

    private void placeClick() {

        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_pick_place);
        dialog.setCancelable(true);

        EditText edtPickPlace = dialog.findViewById(R.id.edt_dl_place_pick_place);
        TextView txtPickPlace = dialog.findViewById(R.id.txt_dl_place_pick_place);
        ImageView imgPick = dialog.findViewById(R.id.img_dl_pick_place);
        ImageView imgRename = dialog.findViewById(R.id.img_dl_rename_place_pick_place);
        ImageView imgRemove = dialog.findViewById(R.id.img_dl_remove_place);
        ImageView imgSetup = dialog.findViewById(R.id.img_dl_setup_gps);
        TextView txtOk = dialog.findViewById(R.id.txt_dl_OK_rename_place);
        TextView txtCancel = dialog.findViewById(R.id.txt_dl_Cancel_rename_place);
        TextView txtPick = dialog.findViewById(R.id.txt_dl_pick_place);
        TextView txtRename = dialog.findViewById(R.id.txt_dl_rename_pick_place);
        TextView txtRemove = dialog.findViewById(R.id.txt_dl_remove_pick_place);
        TextView txtSetup = dialog.findViewById(R.id.txt_dl_setup_pick_place);
        txtPickPlace.setText(position);

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
                txtPickPlace.setVisibility(View.INVISIBLE);
                edtPickPlace.setVisibility(View.VISIBLE);
                edtPickPlace.setText(position);
                imgPick.setVisibility(View.INVISIBLE);
                imgRename.setVisibility(View.INVISIBLE);
                imgRemove.setVisibility(View.INVISIBLE);
                imgSetup.setVisibility(View.INVISIBLE);
                txtPick.setVisibility(View.INVISIBLE);
                txtRename.setVisibility(View.INVISIBLE);
                txtRemove.setVisibility(View.INVISIBLE);
                txtSetup.setVisibility(View.INVISIBLE);
                txtOk.setVisibility(View.VISIBLE);
                txtCancel.setVisibility(View.VISIBLE);

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        txtPickPlace.setVisibility(View.VISIBLE);
                        edtPickPlace.setVisibility(View.INVISIBLE);
                        imgPick.setVisibility(View.VISIBLE);
                        imgRename.setVisibility(View.VISIBLE);
                        imgRemove.setVisibility(View.VISIBLE);
                        imgSetup.setVisibility(View.VISIBLE);
                        txtPick.setVisibility(View.VISIBLE);
                        txtRename.setVisibility(View.VISIBLE);
                        txtRemove.setVisibility(View.VISIBLE);
                        txtSetup.setVisibility(View.VISIBLE);
                        txtOk.setVisibility(View.INVISIBLE);
                        txtCancel.setVisibility(View.INVISIBLE);

                    }
                });
                txtOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        position = edtPickPlace.getText().toString();
                        txtPickPlace.setText(position);
                        edtPickPlace.setVisibility(View.INVISIBLE);
                        txtPickPlace.setVisibility(View.VISIBLE);
                        imgPick.setVisibility(View.VISIBLE);
                        imgRename.setVisibility(View.VISIBLE);
                        imgRemove.setVisibility(View.VISIBLE);
                        imgSetup.setVisibility(View.VISIBLE);
                        txtPick.setVisibility(View.VISIBLE);
                        txtRename.setVisibility(View.VISIBLE);
                        txtRemove.setVisibility(View.VISIBLE);
                        txtSetup.setVisibility(View.VISIBLE);
                        txtOk.setVisibility(View.INVISIBLE);
                        txtCancel.setVisibility(View.INVISIBLE);

                    }
                });
            }
        });
        imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPickPlace.setText("");
                position = "";
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

        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_temperature);
        dialog.setCancelable(true);

        TextView txtDes = dialog.findViewById(R.id.txt_dl_describe_weather);
        TextView txtTemp = dialog.findViewById(R.id.txt_dl_temperature);
        TextView txtRemoveTemp = dialog.findViewById(R.id.txt_dl_remove_temperature);
        ImageView imgRemoveTemp = dialog.findViewById(R.id.img_dl_remove_temperature);
        TextView txtTempC = dialog.findViewById(R.id.txt_dl_temperature_c);
        txtRemoveTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              txtDes.setText("");
              txtTemp.setText("");
              txtTempC.setText("");
              temperature = 0;
            }
        });
        imgRemoveTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtDes.setText("");
                txtTemp.setText("");
                txtTempC.setText("");
                temperature = 0;
            }
        });

        txtDes.setText(desWeather + "");
        txtTemp.setText(temperature + "");

        dialog.show();
    }


    private void faceClick() {

        final Dialog dialog = new Dialog(this);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.dialog_face);
        dialog.setCancelable(true);

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
        edt_content_add.setPaintFlags(0);
        edt_content_add.setTypeface(edt_content_add.getTypeface(), Typeface.BOLD);
        textStyle = "B";
    }

    private void italicClick() {
        edt_content_add.setPaintFlags(0);
        edt_content_add.setTypeface(edt_content_add.getTypeface(), Typeface.ITALIC);
        textStyle = "I";
    }

    private void underlineClick() {
        edt_content_add.setTypeface(Typeface.DEFAULT);
        edt_content_add.setPaintFlags(edt_content_add.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        textStyle = "U";
    }

    private void clearFormat() {
        edt_content_add.setPaintFlags(0);
        edt_content_add.setTypeface(Typeface.DEFAULT);
        textStyle = "N";
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
