package com.ictlife.issuedesk.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public IssueAdapter(Context context, List<Issue> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
        prefManager = new PrefManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.issue_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        //  if (position != RecyclerView.NO_POSITION) {

        final Issue issue = mData.get(position);

        String channel = issue.getChannel_id();

        //  int boxColor = context.getResources().getColor(R.color.box_color);

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

        // }
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvIssueTitle, tvIssueDetail, tvCustomer, tvCreatedBy, tvChannel, tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvIssueTitle = itemView.findViewById(R.id.tvIssueTitle);
            tvIssueDetail = itemView.findViewById(R.id.tvIssueDetail);
            tvCustomer = itemView.findViewById(R.id.tvCustomer);
            tvCreatedBy = itemView.findViewById(R.id.tvCreatedBy);
            tvChannel = itemView.findViewById(R.id.tvChannel);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

}
