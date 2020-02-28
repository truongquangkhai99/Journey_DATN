package com.example.journey_datn.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.journey_datn.Adapter.AdapterRcvAdd;
import com.example.journey_datn.R;

import java.util.Calendar;

public class AddDataActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView img_mark, img_calendar_add, img_tag_add, img_three_dots_add;
    private TextView txt_day_add, txt_month_add, txt_year_add, txt_hour_add, txt_minute_add;
    private EditText edt_contain_add;
    private RecyclerView rcv_add;
    private AdapterRcvAdd adapterRcvAdd;
    private LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
    private int mYear, mMonth, mDay, mHour, mMinute;

    private String action = "", position = "", mood = "", srcImage = "";
    private int temperature = 0, th = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        init();
        adapterRcvAdd = new AdapterRcvAdd(this);
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
                        txt_month_add.setText(monthOfYear + "");
                        txt_year_add.setText(year + "");
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
     String contain = edt_contain_add.getText().toString();
     String day = txt_day_add.getText().toString();
     String month = txt_month_add.getText().toString();
     String year = txt_year_add.getText().toString();
     String hour = txt_hour_add.getText().toString();
     String minute = txt_minute_add.getText().toString();

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
            bundle.putString("action", action);
            bundle.putString("position", position);
            bundle.putString("mood", mood);
            bundle.putString("srcImage", srcImage);
            bundle.putInt("temperature", temperature);
            bundle.putInt("th", th);

            intent.putExtra("data", bundle);
            setResult(113, intent);
            finish();
        }
    }
}
