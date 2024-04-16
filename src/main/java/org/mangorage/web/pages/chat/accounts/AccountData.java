package org.mangorage.web.pages.chat.accounts;

public class AccountData {
    private String authToken;
    private String username;
    private String password;
    private boolean scheduledForDeletion = false;

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void delete() {
        this.scheduledForDeletion = true;
    }

    public boolean isDeleted() {
        return scheduledForDeletion;
    }
}
