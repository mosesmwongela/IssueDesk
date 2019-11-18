package com.ictlife.issuedesk.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ictlife.issuedesk.R;
import com.ictlife.issuedesk.ui.dashboard.entity.Issue;
import com.ictlife.issuedesk.util.PrefManager;

import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.ViewHolder> {

    private List<Issue> mData;
    private LayoutInflater mInflater;
    private String TAG = "IssueAdapter";
    private Context context;
    private PrefManager prefManager;
    private com.ictlife.issuedesk.ui.customer.onClickInterface onClickInterface;

    public IssueAdapter(Context context, List<Issue> data, com.ictlife.issuedesk.ui.customer.onClickInterface onClickInterface) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        prefManager = new PrefManager(context);
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.issue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final Issue issue = mData.get(position);

        String channel = issue.getChannel_id();


        if (channel.equalsIgnoreCase("1")) {
            holder.tvChannel.setText("chat");
        } else if (channel.equalsIgnoreCase("2")) {
            holder.tvChannel.setText("Email");
        } else if (channel.equalsIgnoreCase("3")) {
            holder.tvChannel.setText("Phone call");
        } else if (channel.equalsIgnoreCase("4")) {
            holder.tvChannel.setText("Social media");
        } else if (channel.equalsIgnoreCase("5")) {
            holder.tvChannel.setText("IssueDesk");
        } else if (channel.equalsIgnoreCase("6")) {
            holder.tvChannel.setText("Other");
        }

        holder.tvIssueTitle.setText(issue.getQuery_issue());
        holder.tvIssueDetail.setText(issue.getIssue_details());
        holder.tvCustomer.setText(issue.getCustomer_email());
        holder.tvCreatedBy.setText(issue.getCreated_by());
        holder.tvTime.setText(issue.getDate_created());

        holder.layout_issue_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setIssueClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIssueTitle, tvIssueDetail, tvCustomer, tvCreatedBy, tvChannel, tvTime;
        public ConstraintLayout layout_issue_item;

        ViewHolder(View itemView) {
            super(itemView);
            tvIssueTitle = itemView.findViewById(R.id.tvIssueTitle);
            tvIssueDetail = itemView.findViewById(R.id.tvIssueDetail);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvCreatedBy = itemView.findViewById(R.id.tvCreatedBy);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvTime = itemView.findViewById(R.id.tvTime);
            layout_issue_item = itemView.findViewById(R.id.layout_issue_item);
        }
    }

}
