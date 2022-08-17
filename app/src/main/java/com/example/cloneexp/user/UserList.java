package com.example.cloneexp.user;

public class UserList {
    private String name, number, uid;

    public UserList(String name, String number, String uid) {
        this.name = name;
        this.uid = uid;
        this.number = number;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public void setName(String name) {
        this.name = name;
    }
}
