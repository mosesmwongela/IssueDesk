package com.ictlife.issuedesk.ui.create.issue;

public class Issue {
//
//    {
//        "channel_id": 3,
//            "query_issue": "TOP UP - 008",
//            "issue_details": "Request for topup of KES 28000",
//            "customer_email":"customer@gmail.com",
//            "assigned_to": 2,
//            "status_id": 0,
//            "action": "Customer advised accordingly.",
//            "created_by": "CX_TEAM"
//    }

    int channel_id;
    String query_issue;
    String issue_details;
    String customer_email;
    int assigned_to;
    int status_id;
    String action;
    String created_by;

    public Issue() {
    }

    public Issue(int channel_id, String query_issue, String issue_details, String customer_email, int assigned_to, int status_id, String action, String created_by) {
        this.channel_id = channel_id;
        this.query_issue = query_issue;
        this.issue_details = issue_details;
        this.customer_email = customer_email;
        this.assigned_to = assigned_to;
        this.status_id = status_id;
        this.action = action;
        this.created_by = created_by;
    }

    public int getAssigned_to() {
        return assigned_to;
    }

    public int getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(int channel_id) {
        this.channel_id = channel_id;
    }

    public void setAssigned_to(int assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getQuery_issue() {
        return query_issue;
    }

    public void setQuery_issue(String query_issue) {
        this.query_issue = query_issue;
    }

    public String getIssue_details() {
        return issue_details;
    }

    public void setIssue_details(String issue_details) {
        this.issue_details = issue_details;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }


    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }
}
