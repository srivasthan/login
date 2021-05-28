package com.task.login.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.task.login.R;
import com.task.login.Retrofit.Api;
import com.task.login.Retrofit.ApiClient;
import com.task.login.SessionManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sessionManager = new SessionManager(this);
        Button logout = findViewById(R.id.logout);
        TextView name = findViewById(R.id.txt_name);
        String getname = getIntent().getStringExtra("name");
        Api apiInterface = ApiClient.getApiClient().create(Api.class);
        name.setText("Hi  " + getname);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressBar = new ProgressDialog(HomeActivity.this);
                progressBar.setCancelable(true);
                progressBar.setMessage("Loading");
                progressBar.show();
                Call<Void> call = apiInterface.logout();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        progressBar.dismiss();
                        sessionManager.setLogin(false);
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        progressBar.dismiss();
                        Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}