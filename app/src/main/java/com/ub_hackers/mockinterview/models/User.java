package com.ub_hackers.mockinterview.models;
public class User {
    public String username;
    public String email;

    public User(String email, String username){
        this.username = username;
        this.email = email;
    }
    public String getUsername(){
        return username;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
