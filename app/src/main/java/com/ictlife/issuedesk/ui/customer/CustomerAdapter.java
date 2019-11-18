package com.ictlife.issuedesk.ui.customer;

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

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {

    private List<Customer> mData;
    private LayoutInflater mInflater;
    private String TAG = "IssueAdapter";
    private Context context;
    private PrefManager prefManager;
    private onClickInterface onClickInterface;

    public CustomerAdapter(Context context, List<Customer> data, onClickInterface onClickInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        prefManager = new PrefManager(context);
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.customer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Customer customer = mData.get(position);

        String customer_id = customer.getId();
        String customer_name = customer.getName();
        String customer_email = customer.getEmail();
        final String customer_phone = customer.getPhone();


        holder.tvCustomerName.setText(customer_name);
        holder.tvPhone.setText(customer_phone);
        holder.tvEmail.setText(customer_email);

        if (!isKenyanPhoneNUmber(customer_phone)) {
            holder.call_img.setVisibility(View.GONE);
        }

        holder.call_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(customer_phone);
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
        public TextView tvCustomerName, tvPhone, tvEmail;
        public ImageView call_img;

        ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            call_img = itemView.findViewById(R.id.call_img);
        }
    }

}
