package com.martin.training.disabledetector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class IssuesActivity extends AppCompatActivity {

    RadioGroup rgCondition;
    RadioButton rbChoices;
    EditText etAddress;
    EditText etDate;
    Button btnDate, btnAddress, btnContinue;
    FusedLocationProviderClient fLPC;
    IssuesDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        rgCondition = findViewById(R.id.RGcondition);
        etAddress = findViewById(R.id.ETTPAadress);
        etDate = findViewById(R.id.ETDdate);
        btnDate = findViewById(R.id.BTNdate);
        btnAddress = findViewById(R.id.BTNadress);
        btnContinue = findViewById(R.id.BTNsubmitIssues);
        fLPC = LocationServices.getFusedLocationProviderClient(IssuesActivity.this);
        db = new IssuesDB(this);


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etAddress.getText().toString().isEmpty() || etDate.getText().toString().isEmpty()){
                    Toast.makeText(IssuesActivity.this, "Please Fill in all information", Toast.LENGTH_SHORT).show();
                } else {
                    addData();
                    startActivity(new Intent(IssuesActivity.this, ListOfAllActivity.class));
                }
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
                etDate.setText(currentDate);
            }
        });

        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ActivityCompat.checkSelfPermission(IssuesActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    getLocation();
                } else{
                    ActivityCompat.requestPermissions(IssuesActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        rgCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkButton(v);
                System.out.println(rbChoices.getText().toString());
            }
        });

    }

    public void getLocation(){
        fLPC.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
              Location loc = task.getResult();
              if(loc != null){
                  try{
                      Geocoder geocoder = new Geocoder(IssuesActivity.this, Locale.getDefault());
                      List<Address> addressList = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                      etAddress.setText(addressList.get(0).getAddressLine(0));
                  } catch (IOException e){
                      e.printStackTrace();
                  }

              }
            }
        });
    }

    public void checkButton(View v){
        int radioId = rgCondition.getCheckedRadioButtonId();
        rbChoices = findViewById(radioId);
    }

    public void addData(){
        String Loc = etAddress.getText().toString().trim();
        String Date = etDate.getText().toString().trim();
        String cond = rbChoices.getText().toString().trim();
        String uri = getIntent().getStringExtra("uri");

        db.open();
        long insert = db.addInfo( cond,  Loc,  Date,  uri);
        db.close();
        System.out.println(insert);
    }
}