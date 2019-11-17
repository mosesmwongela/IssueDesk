package com.ictlife.issuedesk.ui.create.user;

public class User {

    String name;
    String phone;
    String email;
    String password;
    int type;

    public User(String name, String phone, String email, String password, int type) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
