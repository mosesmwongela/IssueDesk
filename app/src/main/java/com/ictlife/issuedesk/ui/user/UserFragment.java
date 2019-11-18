package com.ictlife.issuedesk.ui.user;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonElement;
import com.ictlife.issuedesk.IssueDeskApplication;
import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.ui.customer.onClickInterface;
import com.ictlife.issuedesk.util.PrefManager;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ictlife.issuedesk.util.Util.formatISODate;
import static com.ictlife.issuedesk.util.Util.formatISOTime;
import static com.ictlife.issuedesk.util.Util.generalFormatDateTime;

public class UserFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SweetAlertDialog pDialog;
    private List<User> users = new ArrayList<>();
    private PrefManager prefManager;
    private String TAG = "UserFragment";

    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView user_rv;
    private RecyclerView.Adapter userAdapter;

    private onClickInterface onclickInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        prefManager = new PrefManager(getContext());

        swipe_refresh_layout = root.findViewById(R.id.swipe_refresh_layout);
        swipe_refresh_layout.setOnRefreshListener(this);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(String phoneNumber) {
                actionCall(phoneNumber);
            }

            @Override
            public void setIssueClick(int position) {
                
            }
        };

        userAdapter = new UserAdapter(getContext(), users, onclickInterface);

        user_rv = root.findViewById(R.id.user_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        user_rv.setLayoutManager(layoutManager);
        user_rv.setItemAnimator(new DefaultItemAnimator());
        user_rv.setHasFixedSize(true);
        user_rv.setItemViewCacheSize(300);
        user_rv.setDrawingCacheEnabled(true);
        user_rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        user_rv.setNestedScrollingEnabled(false);

        user_rv.setAdapter(userAdapter);

        fetchUser();

        return root;
    }

    private void actionCall(String phoneNumber) {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
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


    private void fetchUser() {

        users.clear();

        showDialog();

        String user_token = "Bearer " + prefManager.getUserToken();

        IssueDeskApplication.apiManager.getUsers(user_token, new Callback<JsonElement>() {
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

                            userAdapter.notifyDataSetChanged();


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
        pDialog.setCancelable(true);
        pDialog.show();
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        fetchUser();
//    }

    @Override
    public void onRefresh() {
        swipe_refresh_layout.setRefreshing(false);
    }
}