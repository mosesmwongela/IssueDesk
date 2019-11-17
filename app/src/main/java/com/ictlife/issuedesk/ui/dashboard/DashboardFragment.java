package com.ictlife.issuedesk.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonElement;
import com.ictlife.issuedesk.IssueDeskApplication;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private static final String TAG = "DashboardFragment";
    private DashboardViewModel dashboardViewModel;
    private GridLayout gridLayout;
    private PrefManager prefManager;
    private SweetAlertDialog pDialog;
    private TextView tv_resolved;
    private TextView tv_open;
    private TextView tv_ongoing;
    private TextView tv_follow;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //final TextView textView = root.findViewById(R.id.text_home);

        tv_resolved = root.findViewById(R.id.tv_resolved);
        tv_open = root.findViewById(R.id.tv_open);
        tv_follow = root.findViewById(R.id.tv_follow);
        tv_ongoing = root.findViewById(R.id.tv_ongoing);

        prefManager = new PrefManager(getContext());

        gridLayout = (GridLayout) root.findViewById(R.id.mainGrid);

        setSingleEvent(gridLayout);

        getDashboardData();


//        dashboardViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }


    private void setSingleEvent(GridLayout gridLayout) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            CardView cardView = (CardView) gridLayout.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getContext(), "Clicked at index " + finalI,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getDashboardData() {

        showDialog();

        String user_token = "Bearer " + prefManager.getUserToken();

        IssueDeskApplication.apiManager.getDashBoardData(user_token, new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {

                Log.e(TAG, "onResponse: " + response.toString());
                hidepDialog();
                if (!response.isSuccessful()) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Log.e(TAG, "error_message: " + jObjError.getString("error_message") + " response code: " + response.code());

                        Toast.makeText(getContext(), "Server error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    JsonElement responseUser = response.body();
                    Log.e(TAG, "onResponse: " + responseUser.toString());

                    if (responseUser != null) {

                        try {
                            JSONObject jsonResponse = new JSONObject(response.body().toString());

                            JSONObject issuesReport = jsonResponse.getJSONObject("issues_report");

                            String totalOpen = issuesReport.getString("total_open");
                            String totalOngoing = issuesReport.getString("total_ongoing");
                            String totalResolved = issuesReport.getString("total_resolved");
                            String totalFollowUp = issuesReport.getString("total_follow_up_required");
                            String grandTotal = issuesReport.getString("grand_total");

                            tv_resolved.setText(totalResolved + " Resolved");
                            tv_open.setText(totalOpen + " Open");
                            tv_follow.setText(totalFollowUp + " Follow Up");
                            tv_ongoing.setText(totalOngoing + " Ongoing");


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

        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#002fbf"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }
}