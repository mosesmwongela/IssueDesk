package com.ictlife.issuedesk.ui.create.issue;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonElement;
import com.ictlife.issuedesk.IssueDeskApplication;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.ui.user.User;
import com.ictlife.issuedesk.util.ConnectionDetector;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ictlife.issuedesk.util.Util.formatISODate;
import static com.ictlife.issuedesk.util.Util.formatISOTime;
import static com.ictlife.issuedesk.util.Util.generalFormatDateTime;

public class CreateIssueActivity extends AppCompatActivity {

    private static final String TAG = "CreateIssueActivity";
    private Spinner channelSpinner;
    private Spinner assignToSpinner;
    private Spinner issueStatusSpinner;
    private String[] CHANNEL_DATA = {"Select Channel", "Call", "Chat", "Email", "IssueDesk", "Social_Media", "Other"};
    private String[] ISSUE_STATUS = {"Select Status", "Open", "Ongoing", "Resolved", "Follow up required"};
    private List<User> users = new ArrayList<>();
    private PrefManager prefManager;

    private TextInputEditText ti_customer_id;
    private TextInputEditText ti_issue_title;
    private TextInputEditText ti_issue_detail;
    private TextInputEditText ti_action;
    private ConnectionDetector connectionDetector;

    private SweetAlertDialog pDialog;
    private MaterialButton next_button;
    private MaterialButton cancel_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_issue);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Create Issue");

        prefManager = new PrefManager(this);
        connectionDetector = new ConnectionDetector(this);

        channelSpinner = findViewById(R.id.channelSpinner);
        assignToSpinner = findViewById(R.id.assignToSpinner);

        CustomArrayAdapter adapter = new CustomArrayAdapter(this,
                R.layout.spinner_item, CHANNEL_DATA);
        channelSpinner.setAdapter(adapter);

        issueStatusSpinner = findViewById(R.id.issueStatusSpinner);
        adapter = new CustomArrayAdapter(this,
                R.layout.spinner_item, ISSUE_STATUS);
        issueStatusSpinner.setAdapter(adapter);

        fetchUser();

        ti_customer_id = findViewById(R.id.ti_customer_id);
        ti_issue_title = findViewById(R.id.ti_issue_title);
        ti_issue_detail = findViewById(R.id.ti_issue_detail);
        ti_action = findViewById(R.id.ti_action);
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
                createIssue();
            }
        });

        //channelSpinner.getText().toString();
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


    private void createIssue() {

        String customer_email = ti_customer_id.getText().toString();
        String issue_title = ti_issue_title.getText().toString();
        String issue_detail = ti_issue_detail.getText().toString();
        String issue_action = ti_issue_detail.getText().toString();
        String channel = channelSpinner.getSelectedItem().toString();
        String issue_status = issueStatusSpinner.getSelectedItem().toString();
        String issue_assign_to = assignToSpinner.getSelectedItem().toString();
        String created_by = "CX_TEAM";

        if (!connectionDetector.isConnectingToInternet()) {
            showErrorDialog("Can't connect to the internet. Please check your network settings.");
            return;
        }

        if (customer_email == null || customer_email.trim().length() == 0) {
            showErrorDialog("Please enter the customer email");
            return;
        }

        if (issue_title == null || issue_title.trim().length() == 0) {
            showErrorDialog("Please enter the issue title");
            return;
        }

        if (issue_detail == null || issue_detail.trim().length() == 0) {
            showErrorDialog("Please enter the issue details");
            return;
        }

        if (channel == null || channel.trim().length() == 0 || channel.equalsIgnoreCase("Select channel")) {
            showErrorDialog("Please enter issue channel");
            return;
        }
        int channel_id = 0;

        switch (channel) {
            case "Call":
                channel_id = 3;
                break;
            case "Chat":
                channel_id = 1;
                break;
            case "Email":
                channel_id = 2;
                break;
            case "IssueDesk":
                channel_id = 5;
                break;
            case "Social_Media":
                channel_id = 4;
                break;
            case "Other":
                channel_id = 6;
                break;
        }

        if (issue_status == null || issue_status.trim().length() == 0 || issue_status.equalsIgnoreCase("Select Status")) {
            showErrorDialog("Please enter issue status");
            return;
        }

        int status_id = 0;

        switch (issue_status) {
            case "Open":
                status_id = 0;
                break;
            case "Ongoing":
                status_id = 1;
                break;
            case "Resolved":
                status_id = 2;
                break;
            case "Follow up required":
                status_id = 3;
                break;
        }

        if (status_id == 2 && (issue_action == null || issue_action.trim().length() == 0)) {
            showErrorDialog("Please enter issue resolution action");
            return;
        }

        if (issue_assign_to == null || issue_assign_to.trim().length() == 0 || issue_assign_to.equalsIgnoreCase("Assign to")) {
            showErrorDialog("Please assign issue");
            return;
        }

        int user_id = 0;
        for (int i = 0; i <= users.size() - 1; i++) {
            String user_name = users.get(i).getName();
            if (user_name.equalsIgnoreCase(issue_assign_to)) {
                user_id = Integer.parseInt(users.get(i).getId());
            }
        }

        hideKeyboard();


        Issue issue = new Issue(channel_id, issue_title, issue_detail,
                customer_email, user_id, status_id, issue_action, created_by);

        showLoading();

        String user_token = "Bearer " + prefManager.getUserToken();

        IssueDeskApplication.apiManager.createIssue(user_token, issue, new Callback<JsonElement>() {
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

                        showSuccess();
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
        pDialog.setTitleText("User created successfully");
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


    private void fetchUser() {

        users.clear();

        final String user_token = "Bearer " + prefManager.getUserToken();

        IssueDeskApplication.apiManager.getUsers(user_token, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e(TAG, "onResponse: " + response.toString());

                if (!response.isSuccessful()) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e(TAG, "error_message: " + jObjError.getString("error_message") + " response code: " + response.code());

                        // Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    JsonElement responseUser = response.body();
                    Log.e(TAG, "onResponse: " + responseUser.toString());

                    if (responseUser != null) {

                        try {
                            JSONArray jsonArray = new JSONArray(response.body().toString());

                            for (int i = 0; i <= jsonArray.length() - 1; i++) {
                                JSONObject c = jsonArray.getJSONObject(i);

                                String id = c.getString("id");
                                String date_created = c.getString("date_created");
                                String name = c.getString("name");
                                String phone = c.getString("phone");
                                String email = c.getString("email");

                                String fomartedTime = generalFormatDateTime(formatISOTime(date_created), formatISODate(date_created));

                                User user = new User(id, fomartedTime, name, phone, email);

                                users.add(user);
                            }

                            // userAdapter.notifyDataSetChanged();

                            String[] assignToData = new String[users.size() + 1];

                            assignToData[0] = "Assign to";

                            for (int i = 1; i <= users.size() - 1; i++) {
                                assignToData[i] = users.get(i).getName();
                            }

                            CustomArrayAdapter adapter = new CustomArrayAdapter(CreateIssueActivity.this,
                                    R.layout.spinner_item, assignToData);
                            assignToSpinner.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //hidepDialog();
                        String err = String.format("Response is %s", String.valueOf(response.code()));
                        Log.e(TAG, "Getting trans onFailure: " + err);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                //hidepDialog();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


}
