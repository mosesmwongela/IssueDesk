package com.ictlife.issuedesk.ui.dashboard.entity;

public class Issue {


    String id;
    String date_created;
    String date_updated;
    String customer_id;
    String channel_id;
    String query_issue;
    String issue_details;
    String assigned_to;
    String status_id;
    String action;
    String created_by;

    public Issue() {
    }

    public Issue(String id, String date_created, String date_updated, String customer_id, String channel_id, String query_issue, String issue_details, String assigned_to, String status_id, String action, String created_by) {
        this.id = id;
        this.date_created = date_created;
        this.date_updated = date_updated;
        this.customer_id = customer_id;
        this.channel_id = channel_id;
        this.query_issue = query_issue;
        this.issue_details = issue_details;
        this.assigned_to = assigned_to;
        this.status_id = status_id;
        this.action = action;
        this.created_by = created_by;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getDate_updated() {
        return date_updated;
    }

    public void setDate_updated(String date_updated) {
        this.date_updated = date_updated;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
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

    public String getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(String assigned_to) {
        this.assigned_to = assigned_to;
    }

    public String getStatus_id() {
        return status_id;
    }

    public void setStatus_id(String status_id) {
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
