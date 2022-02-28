package com.example.lejweleputswasewageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Progress_Report extends AppCompatActivity {
    TextView tvStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_report);
        tvStatus=findViewById(R.id.tvStatus);






        Person person=new Person();
        person.setStatus("Sent");






        tvStatus.setText("Status:"+person.getStatus());





    }
}
