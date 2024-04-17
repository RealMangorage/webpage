package org.mangorage.web.pages.chat.accounts;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class AccountData {
    private String authToken;
    private String username;
    private String password;
    private boolean scheduledForDeletion = false;
    private StompHeaderAccessor accessor;

    public String getAuthToken() {
        return authToken;
    }

    public String getUsername() {
        return username;
    }

    public StompHeaderAccessor getAccessor() {
        return accessor;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAccessor(StompHeaderAccessor accessor) {
        this.accessor = accessor;
    }

    public void delete() {
        this.scheduledForDeletion = true;
    }

    public boolean isDeleted() {
        return scheduledForDeletion;
    }
}
