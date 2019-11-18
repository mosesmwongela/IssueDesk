package com.ictlife.issuedesk.ui.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonElement;
import com.ictlife.issuedesk.IssueDeskApplication;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.ui.dashboard.entity.Issue;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ictlife.issuedesk.util.Util.formatISODate;
import static com.ictlife.issuedesk.util.Util.formatISOTime;
import static com.ictlife.issuedesk.util.Util.generalFormatDateTime;

public class IssuesActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "IssuesActivity";
    private SweetAlertDialog pDialog;
    private PrefManager prefManager;
    private SwipeRefreshLayout swipe_refresh_layout;
    private List<Issue> issues = new ArrayList<>();
    private RecyclerView issues_rv;
    private RecyclerView.Adapter issueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);

        prefManager = new PrefManager(this);

        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(this);

        issueAdapter = new IssueAdapter(this, issues);

        issues_rv = findViewById(R.id.issues_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        issues_rv.setLayoutManager(layoutManager);
        issues_rv.setItemAnimator(new DefaultItemAnimator());
        issues_rv.setHasFixedSize(true);
        issues_rv.setItemViewCacheSize(300);
        issues_rv.setDrawingCacheEnabled(true);
        issues_rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        issues_rv.setNestedScrollingEnabled(false);

        issues_rv.setAdapter(issueAdapter);

        Intent intent = getIntent();

        try {
            String type = intent.getExtras().getString("type");
            String issue_id = intent.getExtras().getString("issue_id");

            Log.e(TAG, "ISSUE ID: " + issue_id);

            //get issue data
            if (type.equalsIgnoreCase("issues")) {
                String issue_title = intent.getExtras().getString("issue_title");

                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                toolbar.setTitle(issue_title);

                fetchIssues(issue_id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fetchIssues(String issue_id) {

        issues.clear();

        showDialog();

        String user_token = "Bearer " + prefManager.getUserToken();

        Map<String, String> params = new HashMap<String, String>();
        params.put("status", issue_id);


        IssueDeskApplication.apiManager.getIssues(user_token, params, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e(TAG, "onResponse: " + response.toString());
                hidepDialog();
                if (!response.isSuccessful()) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e(TAG, "error_message: " + jObjError.getString("error_message") + " response code: " + response.code());

                        Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_LONG).show();
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
                                JSONObject issue = jsonArray.getJSONObject(i);

                                String id = issue.getString("id");
                                String date_created = issue.getString("date_created");
                                String date_updated = issue.getString("date_updated");
                                String customer_id = issue.getString("customer_id");
                                String channel_id = issue.getString("channel_id");
                                String query_issue = issue.getString("query_issue");
                                String issue_details = issue.getString("issue_details");
                                String assigned_to = issue.getString("assigned_to");
                                String status_id = issue.getString("status_id");
                                String action = issue.getString("action");
                                String created_by = issue.getString("created_by");
                                String customer_email = issue.getString("customer_email");

                                String fomartedTime = generalFormatDateTime(formatISOTime(date_created), formatISODate(date_created));

                                Issue is = new Issue(id, fomartedTime, date_updated, customer_id, channel_id, query_issue, issue_details, assigned_to, status_id, action, created_by, customer_email);

                                issues.add(is);

                                Log.e(TAG, "query_issue: " + query_issue + " issue_details: " + issue_details + " action: " + action);
                            }

                            issueAdapter.notifyDataSetChanged();

//                            JSONObject issuesReport = jsonResponse.getJSONObject("issues_report");

//                            String totalOpen = issuesReport.getString("total_open");
//                            String totalOngoing = issuesReport.getString("total_ongoing");
//                            String totalResolved = issuesReport.getString("total_resolved");
//                            String totalFollowUp = issuesReport.getString("total_follow_up_required");
//                            String grandTotal = issuesReport.getString("grand_total");
//

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        hidepDialog();
                        String err = String.format("Response is %s", String.valueOf(response.code()));
                        Log.e(TAG, "Getting trans onFailure: " + err);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hidepDialog();
                Log.e(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void hidepDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    private void showDialog() {

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(false);
    }
}
