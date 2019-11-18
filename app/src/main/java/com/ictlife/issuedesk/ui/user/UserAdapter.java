package com.ictlife.issuedesk.ui.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.util.PrefManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> mData;
    private LayoutInflater mInflater;
    private String TAG = "IssueAdapter";
    private Context context;
    private PrefManager prefManager;
    private com.ictlife.issuedesk.ui.customer.onClickInterface onClickInterface;

    public UserAdapter(Context context, List<User> data, com.ictlife.issuedesk.ui.customer.onClickInterface onClickInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        prefManager = new PrefManager(context);
        this.onClickInterface = onClickInterface;
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
        final String user_phone = user.getPhone();


        holder.tvUserName.setText(user_name);
        holder.tvPhone.setText(user_phone);
        holder.tvEmail.setText(user_email);

        if (!isKenyanPhoneNUmber(user_phone)) {
            holder.call_img.setVisibility(View.GONE);
        }

        holder.call_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(user_phone);
            }
        });
    }

    public boolean isKenyanPhoneNUmber(String phone) {
        try {
            phone.replaceAll(" ", "");
            Pattern kenyanPhoneNumberPattern = Pattern.compile("^(?:254|\\+254|0)?(7[0-9]{8})$");
            Matcher matcher = kenyanPhoneNumberPattern.matcher(phone);
            return matcher.matches();
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvUserName, tvPhone, tvEmail;
        public ImageView call_img;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            call_img = itemView.findViewById(R.id.call_img);
        }
    }
}
