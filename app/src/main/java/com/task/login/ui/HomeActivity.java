package com.task.login.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.task.login.R;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        TextView name = findViewById(R.id.txt_name);
        String getname = getIntent().getStringExtra("name");

        name.setText("Hi " + getname);

    }
}