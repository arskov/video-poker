package com.arsenyko.service;

import com.google.appengine.api.users.User;
/**
 * 
 * @author Arseny Kovalchuk
 *
 */
public class UserWrapper implements UserInfo {
    
    private String userId;
    private String email;
    
    public UserWrapper(com.google.appengine.api.users.User user) {
        this.setUserId(user.getUserId());
        this.setEmail(user.getEmail());
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public static UserInfo wrap(User user) {
        return new UserWrapper(user);
    }

}
