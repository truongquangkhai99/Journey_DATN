package com.example.journey_datn.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.MyOptionsPickerView;
import com.bumptech.glide.Glide;
import com.example.journey_datn.Adapter.AdapterRcvAdd;
import com.example.journey_datn.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener, AdapterRcvAdd.OnItemClickListener {

    private ImageView img_mark, img_calendar_add, img_tag_add, img_three_dots_add;
    private TextView txt_day_add, txt_month_add, txt_year_add, txt_hour_add, txt_minute_add;
    private EditText edt_contain_add;
    private RecyclerView rcv_add;
    private AdapterRcvAdd adapterRcvAdd;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String th;
    private String position = "",srcImage = "";
    private int temperature = 0, action = R.drawable.icons8_collect_50, mood = R.drawable.icons8_happy_52;
    private  String contain, day, month, year, hour, minute;
    private static final int MY_CAMERA_PERMISSION_CODE = 100, CAMERA_CODE = 0, GALLERY_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        init();
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

    }

    private void dateTimePicker(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                txt_hour_add.setText(hourOfDay + "");
                txt_minute_add.setText(minute + "");
            }
        }, mHour, mMinute, false);
        timePickerDialog.show();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_day_add.setText(dayOfMonth + "");
                        int month = monthOfYear + 1;
                        txt_month_add.setText(month + "");
                        txt_year_add.setText(year + "");

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                        dayOfMonth = dayOfMonth - 1;
                        Date date = new Date(year, monthOfYear, dayOfMonth);
                        th = sdf.format(date);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private void showDialog(){
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

    private void init(){
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
      day = txt_day_add.getText().toString();
      month = txt_month_add.getText().toString();
      year = txt_year_add.getText().toString();
      hour = txt_hour_add.getText().toString();
      minute = txt_minute_add.getText().toString();

        if (TextUtils.isEmpty(contain)) {
            Toast.makeText(this, "No contain", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();

            bundle.putString("contain", contain);
            bundle.putString("day", day);
            bundle.putString("month", month);
            bundle.putString("year", year);
            bundle.putString("hour", hour);
            bundle.putString("minute", minute);
            bundle.putInt("action", action);
            bundle.putString("position", position);
            bundle.putInt("mood", mood);
            bundle.putString("srcImage", srcImage);
            bundle.putInt("temperature", temperature);
            bundle.putString("th", th);

            intent.putExtra("data", bundle);
            setResult(113, intent);
            finish();
        }
    }

    @Override
    public void OnItemClick(int position) {
                if (position == 0)
                    mediaClick();
                if (position == 1)
                    placeClick();
                if (position == 2)
                    temperatureClick();
                if (position == 3)
//                    activityClick();
                    pickerView();
                if (position == 4)
                    faceClick();
                if (position == 5)
                    boldClick();
                if (position == 6)
                    italicClick();
                if (position == 7)
                    underlineClick();
                if (position == 8)
                    backClick();
                if (position == 9)
                    forwardClick();
    }

    private void mediaClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_media,null);
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
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , GALLERY_CODE);
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
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent m_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                    Uri uri = FileProvider.getUriForFile(AddDataActivity.this, AddDataActivity.this.getApplicationContext().getPackageName() + ".provider", file);
                    m_intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(m_intent, CAMERA_CODE);
                }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_CODE);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case CAMERA_CODE:
                File file = new File(Environment.getExternalStorageDirectory(), "MyPhoto.jpg");
                Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                Glide.with(this).load(uri).into(img_tag_add);
                srcImage = uri.toString();
                break;
            case GALLERY_CODE:
                Uri selectedImage = data.getData();
                Glide.with(this).load(selectedImage).into(img_tag_add);
                srcImage = selectedImage.toString();
                break;
        }
    }

    private void placeClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_pick_place,null);
        final Dialog dialog = new Dialog(this);
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

    private void pickerView(){
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
                if (items.get(options1) == items.get(0))
                    action = R.drawable.icons8_collect_50;
                if (items.get(options1) == items.get(1))
                    action = R.drawable.ic_accessibility_black_24dp;
                if (items.get(options1) == items.get(2))
                    action = R.drawable.ic_restaurant_menu_black_24dp;
                if (items.get(options1) == items.get(3))
                    action = R.drawable.ic_directions_walk_black_24dp;
                if (items.get(options1) == items.get(4))
                    action = R.drawable.ic_directions_run_black_24dp;
                if (items.get(options1) == items.get(5))
                    action = R.drawable.ic_directions_bike_black_24dp;
                if (items.get(options1) == items.get(6))
                    action = R.drawable.ic_directions_car_black_24dp;
                if (items.get(options1) == items.get(7))
                    action = R.drawable.ic_airplanemode_active_black_24dp;
            }
        });

        singlePicker.show();
    }

    private void temperatureClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_temperature,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);

        dialog.show();
    }

    private void activityClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_activity,null);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(dialogView);
        dialog.show();
    }

    private void faceClick(){
        final View dialogView = View.inflate(this,R.layout.dialog_face,null);
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
                mood = R.drawable.icons8_heart_48;
                dialog.dismiss();
            }
        });

        imgHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.icons8_happy_52;
                dialog.dismiss();
            }
        });

        imgGrinning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.icons8_grinning_face_64;
                dialog.dismiss();
            }
        });

        imgSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.icons8_sad_48;
                dialog.dismiss();
            }
        });

        imgNeutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood = R.drawable.icons8_neutral_48;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void boldClick(){
        Toast.makeText(AddDataActivity.this, "click bold", Toast.LENGTH_SHORT).show();
    }

    private void italicClick(){
        Toast.makeText(AddDataActivity.this, "click italick", Toast.LENGTH_SHORT).show();
    }

    private void underlineClick(){
        Toast.makeText(AddDataActivity.this, "click underline", Toast.LENGTH_SHORT).show();
    }

    private void backClick(){
        Toast.makeText(AddDataActivity.this, "click back", Toast.LENGTH_SHORT).show();
    }

    private void forwardClick(){
        Toast.makeText(AddDataActivity.this, "click forward", Toast.LENGTH_SHORT).show();
    }
}
