package com.ub_hackers.mockinterview.singletons;

import com.ub_hackers.mockinterview.models.User;

public class LoginedUser {
    private static LoginedUser loginedUser;
    private User user;
    String userPk;
    public static LoginedUser getInstance(){
        if(loginedUser==null)
            loginedUser = new LoginedUser();
        return loginedUser;
    }

    public void signIn(String userPk, User user){
        this.userPk=userPk;
        this.user=user;
    }

    public void signOut(){
        this.user=null;
        userPk=null;
    }

    public String getUserPk() {
        return userPk;
    }

    public User getUser() {
        return user;
    }
}
