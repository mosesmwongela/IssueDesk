package com.ictlife.issuedesk.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.util.PrefManager;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private String TAG = "IssueAdapter";
    private Context context;
    private PrefManager prefManager;

    public UserAdapter(Context context, List<User> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final User user = mData.get(position);

        String user_id = user.getId();
        String user_name = user.getName();
        String user_email = user.getEmail();
        String user_phone = user.getPhone();


        holder.tvUserName.setText(user_name);
        holder.tvPhone.setText(user_phone);
        holder.tvEmail.setText(user_email);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvPhone, tvEmail;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
        }
    }

}
