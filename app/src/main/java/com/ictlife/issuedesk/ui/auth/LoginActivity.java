package com.ictlife.issuedesk.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ictlife.issuedesk.MainActivity;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LoginActivity";
    private PrefManager prefManager;

    private String username;
    private String password;

    private TextInputLayout id_text_input_layout;
    private TextInputEditText id_text_input;

    private TextInputLayout password_text_input;
    private TextInputEditText password_edit_text;

    private MaterialButton next_button;
    private MaterialButton cancel_button;

    private Context context;

    private SweetAlertDialog pDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        prefManager = new PrefManager(this);

        context = this;

        password_text_input = findViewById(R.id.password_text_input);
        password_edit_text = findViewById(R.id.password_edit_text);

        id_text_input_layout = findViewById(R.id.id_text_input_layout);
        id_text_input = findViewById(R.id.id_text_input);

        next_button = findViewById(R.id.next_button);
        cancel_button = findViewById(R.id.cancel_button);

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Set an error if the password is less than 8 characters.
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = id_text_input.getText().toString();
                password = password_edit_text.getText().toString();

                if (username != null && username.trim().length() == 0) {
                    id_text_input_layout.setError("Enter username");
                    return;
                } else {
                    id_text_input_layout.setError(null);
                }

                if (password != null && password.trim().length() == 0) {
                    password_text_input.setError("Enter password");
                    return;
                } else {
                    password_text_input.setError(null);
                }
                submitData();
                //  new loginAsync(LoginActivity.this).execute();

            }
        });

        password_edit_text.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                password_text_input.setError(null);

                return false;
            }
        });

        id_text_input.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                id_text_input_layout.setError(null);

                return false;
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


    private interface ApiService {
        @GET("issue_tracking/authenticate")
        Call<JsonElement> loginData();
    }

    private class PostResponse {
        @SerializedName("token")
        @Expose
        private String token;

        @SerializedName("user")
        @Expose
        private String user;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    private static class ServiceGenerator {
        public static final String API_BASE_URL = "http://176.58.124.136:9010/";

        private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        private static Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        private static Retrofit retrofit = builder.build();

        public <S> S createService(Class<S> serviceClass) {
            return createService(serviceClass, null, null);
        }

        public static <S> S createService(Class<S> serviceClass, String username, String password) {
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                String authToken = Credentials.basic(username, password);
                return createService(serviceClass, authToken);
            }
            return createService(serviceClass, null);
        }

        public static <S> S createService(Class<S> serviceClass, final String authToken) {
            if (!TextUtils.isEmpty(authToken)) {
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
                if (!httpClient.interceptors().contains(interceptor)) {
                    httpClient.addInterceptor(interceptor);
                    httpClient.addInterceptor(loggingInterceptor);
                    builder.client(httpClient.build());
                    retrofit = builder.build();
                }
            }
            return retrofit.create(serviceClass);
        }
    }

    private static class AuthenticationInterceptor implements Interceptor {
        private String authToken;

        public AuthenticationInterceptor(String token) {
            this.authToken = token;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Request original = chain.request();
            okhttp3.Request.Builder builder = original.newBuilder()
                    .header("Authorization", authToken);
            okhttp3.Request request = builder.build();
            return chain.proceed(request);
        }
    }

    private void submitData() {

        hideKeyboard();

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText("Authenticating");
        pDialog.setCancelable(false);
        pDialog.show();


        //Defining retrofit api service
        ApiService loginService = ServiceGenerator.createService(ApiService.class, username, password);
        Call<JsonElement> call = loginService.loginData();
        //calling the api
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //hiding progress dialog
                pDialog.dismiss();
                if (response.isSuccessful()) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response.body().toString());

                        String userToken = jsonResponse.getString("token");

                        JSONObject user = jsonResponse.getJSONObject("user");
                        String user_name = user.getString("name");
                        String phone = user.getString("phone");
                        String email = user.getString("email");

                        prefManager.setLoggedIn(true);
                        prefManager.setUserEmail(email);
                        prefManager.setUserFullName(user_name);
                        prefManager.setUserToken(userToken);

                        //     Log.e(TAG, "userToken: " + userToken + " Name: " + user_name + " Phone: " + phone + " Email: " + email);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // prefManager.setLoggedIn(true);
                    // Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    prefManager.setLoggedIn(false);
                    password_text_input.setError("Wrong username and/or password");
                    // Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                pDialog.dismiss();
                Log.e(TAG, t.getMessage());
                // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
            }
        });
    }
}
