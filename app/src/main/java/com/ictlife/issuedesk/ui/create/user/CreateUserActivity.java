package com.ictlife.issuedesk.ui.create.user;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.ictlife.issuedesk.IssueDeskApplication;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.util.ConnectionDetector;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity {

    private static final String TAG = "CreateUserActivity";
    private TextInputEditText ti_customer_id;
    private CountryCodePicker country_code_picker;
    private EditText et_phone_number;
    private TextInputEditText ti_email;
    private TextInputEditText password_edit_text;
    private TextInputEditText confirm_password_edit_text;
    private MaterialButton next_button;
    private MaterialButton cancel_button;
    private String phoneNumber;
    private ConnectionDetector connectionDetector;
    private PrefManager prefManager;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Create User");

        connectionDetector = new ConnectionDetector(this);
        prefManager = new PrefManager(this);

        ti_customer_id = findViewById(R.id.ti_customer_id);
        country_code_picker = findViewById(R.id.country_code_picker);
        et_phone_number = findViewById(R.id.et_phone_number);
        ti_email = findViewById(R.id.ti_email);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);
        next_button = findViewById(R.id.next_button);
        cancel_button = findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUser();
            }
        });


    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createUser() {

        String full_name = ti_customer_id.getText().toString();
        String email = ti_email.getText().toString();
        String password = password_edit_text.getText().toString();
        String confirm_password = confirm_password_edit_text.getText().toString();

        if (full_name == null || full_name.trim().length() == 0) {
            showErrorDialog("Please enter user name");
            return;
        }

        if (!validatePhoneNumber()) {
            showErrorDialog("Invalid phone number");
            return;
        }

        if (!isEmailValid(email)) {
            showErrorDialog("Invalid email address");
            return;
        }

        if (password == null || password.trim().length() == 0) {
            showErrorDialog("Please enter password");
            return;
        }

        if (confirm_password == null || confirm_password.trim().length() == 0) {
            showErrorDialog("Please re-enter password");
            return;
        }

        if (!password.equals(confirm_password)) {
            showErrorDialog("Passwords do not match");
            return;
        }


        if (!connectionDetector.isConnectingToInternet()) {
            showErrorDialog("Can't connect to the internet. Please check your network settings.");
            return;
        }

        hideKeyboard();


        User user = new User(full_name, phoneNumber, email, password, 1);

        showLoading();

        String user_token = "Bearer " + prefManager.getUserToken();

        IssueDeskApplication.apiManager.createUser(user_token, user, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hidepDialog();

                if (!response.isSuccessful()) {
                    try {
                        Log.e(TAG, "error_message: " + response.toString());
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        Log.e(TAG, "error_message: " + jObjError.getString("error_message") + " response code: " + response.code());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    JsonElement responseUser = response.body();
                    Log.e(TAG, "onResponse: " + responseUser.toString());
                    if (responseUser != null) {

                        //showSuccess();
                        finish();

                    } else {
                        String err = String.format("Response is %s", String.valueOf(response.code()));
                        Log.e(TAG, "onFailure: " + err);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
            }

        });
    }

    private void showSuccess() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText("Issue created successfully");
        pDialog.setCancelable(true);
        pDialog.show();

    }

    private void hidepDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showErrorDialog(String string) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText(string);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void showLoading() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private boolean validatePhoneNumber() {

        String countryCodeNum = country_code_picker.getSelectedCountryCode();
        String countryCode = country_code_picker.getSelectedCountryNameCode();
        String phone = et_phone_number.getText().toString();

        if (phone != null)
            phone = phone.replaceAll(" ", "");

        phoneNumber = phone;

        if (countryCode.equals("KE")) {
            if (!phone.startsWith("+")) {
                phoneNumber = (phone.startsWith("0")) ? phone : "0" + phone;
            } else {
                phoneNumber = phone.replace("+254", "0");
            }
        }

        if (validatePhoneNumber(phoneNumber, countryCode, this)) {
            Log.e(TAG, "valid phone number: " + phoneNumber);

            return true;
        } else {
            Log.e(TAG, "invalid phone number: " + phoneNumber);
            return false;
        }
    }

    public boolean isKenyanPhoneNUmber(String phone) {
        try {
            Pattern kenyanPhoneNumberPattern = Pattern.compile("^(?:254|\\+254|0)?(7[0-9]{8})$");
            Matcher matcher = kenyanPhoneNumberPattern.matcher(phone);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }


    public Boolean validatePhoneNumber(String number, String countryCode, Context context) {

        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.createInstance(context);

        String validatedNumber;

        if (number.startsWith("+")) {
            validatedNumber = number;
        } else {
            validatedNumber = "+" + number;
        }

        try {
            if (countryCode.equals("KE")) {
                return isKenyanPhoneNUmber(number);
            }
            phoneNumberUtil.parse(validatedNumber, countryCode);
            return true;
        } catch (NumberParseException e) {
            Log.e(TAG, "error during parsing a number: " + validatedNumber + " of country code: " + countryCode);
            return false;
        }
    }
}
