package com.task.login.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.task.login.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class RegistrationActivity extends AppCompatActivity {
    TextInputEditText name, mail, mobile, alternativeMobile, password, confirmPassword, modelNo, serialNo, contractDuration;
    TextInputEditText priority, plotno, street, postcode, landmark;
    ProgressDialog progressBar;
    TextView eLogin;
    List<String> getProductName = new ArrayList<>();
    List<Integer> getProductId = new ArrayList<>();
    List<String> getSubName = new ArrayList<>();
    List<Integer> getSubId = new ArrayList<>();
    List<String> getAmcName = new ArrayList<>();
    List<Integer> getAmcId = new ArrayList<>();
    List<String> getCountryName = new ArrayList<>();
    List<Integer> getCountryId = new ArrayList<>();
    List<String> getStateName = new ArrayList<>();
    List<Integer> getStateId = new ArrayList<>();
    List<String> getCity = new ArrayList<>();
    List<Integer> getCityId = new ArrayList<>();
    List<String> getLocation = new ArrayList<>();
    List<Integer> getLocationId = new ArrayList<>();
    int selectedSubproduct, selectedProductId, selectedAmcId, selectedCountryId, selectedStateId, selectedCityId, selectedLocationId;
    MaterialSpinner spinner, subproduct, amc, country, state, city, location;
    String spspinner, spsubproduct, spamc, spcountry, spstate, spcity, splocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();

        invokeApiProduct();
        invokeAMC();
        invokeCountry();

        name = findViewById(R.id.edt_name);
        mail = findViewById(R.id.edt_email);
        mobile = findViewById(R.id.edt_mobile);
        alternativeMobile = findViewById(R.id.edt_altmobile);
        password = findViewById(R.id.edt_password);
        confirmPassword = findViewById(R.id.edt_confirm_password);
        modelNo = findViewById(R.id.edt_modelno);
        serialNo = findViewById(R.id.serialno);
        contractDuration = findViewById(R.id.contract_duration);
        priority = findViewById(R.id.priority);
        plotno = findViewById(R.id.plotno);
        street = findViewById(R.id.street);
        postcode = findViewById(R.id.postcode);
        landmark = findViewById(R.id.landmark);
        spinner = findViewById(R.id.product);
        subproduct = findViewById(R.id.subproduct);
        amc = findViewById(R.id.amc);
        country = findViewById(R.id.country);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        location = findViewById(R.id.location_id);
        Button register = findViewById(R.id.btn_register);

        eLogin = findViewById(R.id.btn_login);
        eLogin.setOnClickListener(v -> {
            finish();
            Intent mIntent = new Intent(RegistrationActivity.this, MainActivity.class);
            startActivity(mIntent);
        });

        spinner.setHint("Select Product");
        spinner.setItems(getProductName);
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spspinner = "sn";
                selectedProductId = getProductId.get(position);
                invokeSubProductApi();
            }
        });
        spinner.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spspinner = "ns";
            }
        });

        //Subproduct Dropdown
        subproduct.setHint("Select Subproduct");
        subproduct.setItems(getSubName);
        subproduct.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spsubproduct = "sn";
                selectedSubproduct = getSubId.get(0);
            }
        });
        subproduct.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spsubproduct = "ns";
            }
        });

        //Amc Dropdown
        amc.setHint("Select Amc");
        amc.setItems(getAmcName);
        amc.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spamc = "sn";
                selectedAmcId = getAmcId.get(position);
            }
        });
        amc.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spamc = "ns";
            }
        });

        //Country Dropdown
        country.setHint("Select Country");
        country.setItems(getCountryName);
        country.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spcountry = "sn";
                selectedCountryId = getCountryId.get(position);
                invokeStateApi();
            }
        });
        country.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spcountry = "ns";
            }
        });

        //State Dropdown
        state.setHint("Select State");
        state.setItems(getStateName);
        state.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spstate = "sn";
                selectedStateId = getStateId.get(position);
                invokeCityApi();
            }
        });
        state.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spstate = "ns";
            }
        });

        //City Dropdown
        city.setHint("Select City");
        city.setItems(getCity);
        city.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                spcity = "sn";
                selectedCityId = getCityId.get(position);
                invokeLocationId();
            }
        });
        city.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                spcity = "ns";
            }
        });

        //Location Dropdown
        location.setHint("Select Location");
        location.setItems(getLocation);
        location.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                splocation = "sn";
                selectedLocationId = getLocationId.get(position);
            }
        });
        location.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override
            public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
                splocation = "ns";
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateEntry();
            }
        });

    }

    private void validateEntry() {
        name.setError(null);
        mail.setError(null);
        mobile.setError(null);
        alternativeMobile.setError(null);
        spinner.setError(null);
        subproduct.setError(null);
        modelNo.setError(null);
        serialNo.setError(null);
        amc.setError(null);
        contractDuration.setError(null);
        priority.setError(null);
        plotno.setError(null);
        street.setError(null);
        postcode.setError(null);
        country.setError(null);
        state.setError(null);
        city.setError(null);
        location.setError(null);
        landmark.setError(null);
        if (name.getText().toString().trim().isEmpty()) {
            name.setError("Please Enter Name");
        } else if (mail.getText().toString().trim().isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mail.getText().toString().trim()).matches()) {
            mail.setError("Please Enter Proper email");
        } else if (mobile.getText().toString().trim().isEmpty() || mobile.getText().toString().trim().length() < 10) {
            mobile.setError("Please Enter Proper Number");
        } else if (alternativeMobile.getText().toString().trim().isEmpty() || alternativeMobile.getText().toString().trim().length() < 10) {
            mobile.setError("Please Enter Proper Alternative Number");
        } else if (spspinner.equals("ns")) {
            spinner.setError("Not Selected");
        } else if (spsubproduct.equals("ns")) {
            subproduct.setError("Please Select Sub Product");
        } else if (modelNo.getText().toString().trim().isEmpty()) {
            modelNo.setError("Please Enter Model Number");
        } else if (serialNo.getText().toString().trim().isEmpty()) {
            serialNo.setError("Please Enter Serial Number");
        } else if (spamc.equals("ns")) {
            amc.setError("Please Select AMC");
        } else if (contractDuration.getText().toString().trim().isEmpty()) {
            contractDuration.setError("Please Enter Contract Duration");
        } else if (priority.getText().toString().trim().isEmpty()) {
            priority.setError("Please Enter Priorty");
        } else if (plotno.getText().toString().trim().isEmpty()) {
            plotno.setError("Please Enter Plot Number");
        } else if (street.getText().toString().trim().isEmpty()) {
            street.setError("Please Enter Street");
        } else if (postcode.getText().toString().trim().isEmpty() || postcode.getText().toString().length() > 15) {
            postcode.setError("Please Enter Valid Postcode");
        } else if (landmark.getText().toString().trim().isEmpty()) {
            landmark.setError("Please Enter Landmark");
        } else {
            registerUser();
        }
    }

    private void registerUser() {

        JSONObject orderData = new JSONObject();
        try {
            orderData.put("customer_name", name.getText().toString().trim());
            orderData.put("email_id", mail.getText().toString().trim());
            orderData.put("contact_number", mobile.getText().toString().trim());
            orderData.put("alternate_number", alternativeMobile.getText().toString().trim());
            orderData.put("product_id", selectedProductId);
            orderData.put("product_sub_id", selectedSubproduct);
            orderData.put("model_no", modelNo.getText().toString().trim());
            orderData.put("serial_no", serialNo.getText().toString().trim());
            orderData.put("amc_id", selectedAmcId);
            orderData.put("contract_duration", Integer.parseInt(contractDuration.getText().toString().trim()));
            orderData.put("priority", priority.getText().toString().trim());
            orderData.put("plot_number", plotno.getText().toString().trim());
            orderData.put("street", street.getText().toString().trim());
            orderData.put("post_code", Integer.parseInt(postcode.getText().toString().trim()));
            orderData.put("country_id", selectedCountryId);
            orderData.put("state_id", selectedStateId);
            orderData.put("city_id", selectedCityId);
            orderData.put("location_id", selectedLocationId);
            orderData.put("landmark", landmark.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(com.android.volley.Request.Method.POST, "http://dev.kaspontech.com/djadmin/customer_registration/",
                orderData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegistrationActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, HomeActivity.class);
                        intent.putExtra("name", name.getText().toString().trim());
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        Volley.newRequestQueue(this).add(req);
    }

    private void invokeLocationId() {
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();
        getLocation.clear();
        getLocationId.clear();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_location_details?city_id=" + selectedCityId)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    JSONObject jsonArray = null;
                    JSONObject api, jsonObject;
                    try {
                        api = new JSONObject(apiData);
                        JSONObject jr = api.getJSONObject("response");
                        JSONArray jsonArray1 = jr.getJSONArray("data");
                        if (jsonArray1.length() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectedLocationId = 0;
                                    location.setItems("No Data");
                                    splocation = "ns";
                                    Toast.makeText(RegistrationActivity.this, "No Location Present", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            splocation = "sn";
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                try {
                                    jsonObject = (JSONObject) jsonArray1.get(i);
                                    String ApproversValue = jsonObject.getString("location_name");
                                    String ApproversKeyValue = jsonObject.getString("location_id");
                                    getLocation.add(ApproversValue);
                                    getLocationId.add(Integer.parseInt(ApproversKeyValue));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressBar.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void invokeCityApi() {
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();
        getCity.clear();
        getCityId.clear();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_city?state_id=" + selectedStateId)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    JSONObject jsonArray = null;
                    JSONObject api, jsonObject;
                    try {
                        api = new JSONObject(apiData);
                        JSONObject jr = api.getJSONObject("response");
                        JSONArray jsonArray1 = jr.getJSONArray("data");
                        if (jsonArray1.length() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectedCityId = 0;
                                    selectedLocationId = 0;
                                    city.setItems("No Data");
                                    location.setItems("No Data");
                                    splocation = "ns";
                                    spcity = "ns";
                                    Toast.makeText(RegistrationActivity.this, "NO City Present", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            spcity = "sn";
                            splocation = "sn";
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                try {
                                    jsonObject = (JSONObject) jsonArray1.get(i);
                                    String ApproversValue = jsonObject.getString("city_name");
                                    String ApproversKeyValue = jsonObject.getString("city_id");
                                    getCity.add(ApproversValue);
                                    getCityId.add(Integer.parseInt(ApproversKeyValue));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressBar.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void invokeStateApi() {
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();
        getStateName.clear();
        getStateId.clear();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_state?country_id=" + selectedCountryId)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    if (apiData.isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "No State Available", Toast.LENGTH_SHORT).show();
                    }
                    JSONObject jsonArray = null;
                    JSONObject api, jsonObject;
                    try {
                        api = new JSONObject(apiData);
                        JSONObject jr = api.getJSONObject("response");
                        JSONArray jsonArray1 = jr.getJSONArray("data");
                        if (jsonArray1.length() == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    selectedStateId = 0;
                                    selectedCityId = 0;
                                    selectedLocationId = 0;
                                    state.setItems("No Data");
                                    city.setItems("No Data");
                                    location.setItems("No Data");
                                    spstate = "ns";
                                    spcity = "ns";
                                    splocation = "ns";
                                    Toast.makeText(RegistrationActivity.this, "No State Available", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            spstate = "sn";
                            spcity = "sn";
                            splocation = "sn";
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                try {
                                    jsonObject = (JSONObject) jsonArray1.get(i);
                                    String ApproversValue = jsonObject.getString("state_name");
                                    String ApproversKeyValue = jsonObject.getString("state_id");
                                    getStateName.add(ApproversValue);
                                    getStateId.add(Integer.parseInt(ApproversKeyValue));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        progressBar.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void invokeCountry() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_country")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    JSONObject jsonArray = null;
                    JSONObject api, jsonObject;
                    try {
                        api = new JSONObject(apiData);
                        JSONObject jr = api.getJSONObject("response");
                        JSONArray jsonArray1 = jr.getJSONArray("data");
                        for (int i = 0; i < jsonArray1.length(); i++) {
                            try {
                                jsonObject = (JSONObject) jsonArray1.get(i);
                                String ApproversValue = jsonObject.getString("country_name");
                                String ApproversKeyValue = jsonObject.getString("country_id");
                                getCountryName.add(ApproversValue);
                                getCountryId.add(Integer.parseInt(ApproversKeyValue));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        progressBar.dismiss();
    }

    private void invokeAMC() {
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_amc_details")
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    if (apiData.isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonArray = null;
                        JSONObject api, jsonObject;
                        try {
                            api = new JSONObject(apiData);
                            JSONObject jr = api.getJSONObject("response");
                            JSONArray jsonArray1 = jr.getJSONArray("data");

                            for (int i = 0; i < jsonArray1.length(); i++) {
                                try {
                                    jsonObject = (JSONObject) jsonArray1.get(i);
                                    String ApproversValue = jsonObject.getString("amc_type");
                                    String ApproversKeyValue = jsonObject.getString("amc_id");
                                    getAmcName.add(ApproversValue);
                                    getAmcId.add(Integer.parseInt(ApproversKeyValue));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void invokeSubProductApi() {
        progressBar.setCancelable(true);
        progressBar.setMessage("Loading");
        progressBar.show();
        getSubName.clear();
        getSubId.clear();
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://dev.kaspontech.com/djadmin/get_subproduct_details/?product_id=" + selectedProductId)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    if (apiData.isEmpty()) {
                        Toast.makeText(RegistrationActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject api, jsonObject;
                        try {
                            api = new JSONObject(apiData);
                            JSONObject jr = api.getJSONObject("response");
                            JSONArray jsonArray1 = jr.getJSONArray("data");
                            if (jsonArray1.length() == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        selectedSubproduct = 0;
                                        subproduct.setItems("No Data");
                                        spsubproduct = "ns";
                                        Toast.makeText(RegistrationActivity.this, "No SubProduct Present", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                spsubproduct = "sn";
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    try {
                                        jsonObject = (JSONObject) jsonArray1.get(i);
                                        String ApproversValue = jsonObject.getString("product_sub_name");
                                        String ApproversKeyValue = jsonObject.getString("product_sub_id");
                                        getSubName.add(ApproversValue);
                                        getSubId.add(Integer.parseInt(ApproversKeyValue));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            progressBar.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void invokeApiProduct() {
        String url = "http://dev.kaspontech.com/djadmin/get_product_details/";
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String apiData = response.body().string();
                    JSONObject jsonArray = null;
                    JSONObject api, jsonObject;
                    try {
                        api = new JSONObject(apiData);
                        JSONObject jr = api.getJSONObject("response");
                        JSONArray jsonArray1 = jr.getJSONArray("data");

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            try {
                                jsonObject = (JSONObject) jsonArray1.get(i);
                                String ApproversValue = jsonObject.getString("product_name");
                                String ApproversKeyValue = jsonObject.getString("product_id");
                                getProductName.add(ApproversValue);
                                getProductId.add(Integer.parseInt(ApproversKeyValue));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}