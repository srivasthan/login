package com.task.login.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.task.login.R;
import com.task.login.Retrofit.Api;
import com.task.login.Retrofit.ApiClient;
import com.task.login.SessionManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText name, password;
    private ProgressDialog progressBar;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        Button login = findViewById(R.id.login);
        name = findViewById(R.id.edt_login);
        password = findViewById(R.id.edt_password);
        TextView eRegister = findViewById(R.id.btn_register);
        eRegister.setOnClickListener(v -> {
            finish();
            Intent mIntent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(mIntent);
        });

        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            intent.putExtra("name", "Welcome Again");
            startActivity(intent);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntry();
            }
        });

    }

    private void validateEntry() {
        name.setError(null);
        password.setError(null);
        if (name.getText().toString().trim().isEmpty() && password.getText().toString().trim().isEmpty()) {
            name.setError("Name Cannot be empty");
            password.setError("Password Cannot be empty");
        } else if (name.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(name.getText().toString().trim()).matches()) {
            name.setError("Enter valid email");
        } else if (password.getText().toString().trim().isEmpty() || password.getText().toString().trim().length() < 6) {
            password.setError("Enter correct Password");
        } else {
            invokeLoginApi();
        }
    }

    private void invokeLoginApi() {
        Api apiInterface = ApiClient.getApiClient().create(Api.class);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", name.getText().toString().trim());
            jsonObject.put("password", password.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Call<Void> call = apiInterface.userLogin(jsonObject);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                sessionManager.setLogin(true);
                Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("name", name.getText().toString().trim());
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                progressBar.dismiss();
                name.setError("User Credentials Wrong");
                password.setError("User Credentials Wrong");
            }
        });

    }

//    private void invokeLoginApi() {
//
//        progressBar = new ProgressDialog(this);
//        progressBar.setCancelable(true);
//        progressBar.setMessage("Loading");
//        progressBar.show();
//
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("username", name.getText().toString().trim());
//            jsonObject.put("password", password.getText().toString().trim());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, "http://dev.kaspontech.com/djadmin/customer_login/",
//                jsonObject,
//                response -> {
//                    Toast.makeText(MainActivity.this, "Logged successfully", Toast.LENGTH_SHORT).show();
//                    progressBar.dismiss();
//                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//                    intent.putExtra("name", name.getText().toString().trim());
//                    startActivity(intent);
//                    finish();
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.e("Error: ", error.getMessage());
//                name.setError("User Credentials Wrong");
//                password.setError("User Credentials Wrong");
//            }
//        });
//
//        Volley.newRequestQueue(this).add(req);
//    }

}