package org.mangorage.web.pages.chat.accounts;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Accounts {
    private static final List<AccountData> DATA = new ArrayList<>();

    public static AccountData findAccount(String authToken) {
        var optional = DATA.stream()
                .filter(a -> !a.isDeleted()) // Dont bother with deleted accounts!
                .filter(a -> a.getAuthToken().equals(authToken))
                .findFirst();
        return optional.orElse(null);
    }

    public static boolean isUsernameTaken(String username) {
        var optional = DATA.stream()
                .filter(a -> !a.isDeleted()) // Dont bother with deleted accounts!
                .filter(a -> a.getUsername().equals(username))
                .findFirst();

        return optional.isPresent();
    }

    public static String generateRandomUser() {
        // Define characters for the random part of the user
        String characters = "abcdefghijklmnopqrstuvwxyz0123456789";

        // Initialize a StringBuilder to build the random part of the user
        StringBuilder randomPart = new StringBuilder();

        // Create an instance of Random class
        Random random = new Random();

        // Generate 5 random characters for the random part of the user
        for (int i = 0; i < 5; i++) {
            randomPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        // Combine "user-" with the random part to form the complete user
        String user = "user-" + randomPart.toString();

        return user;
    }

    public static List<AccountData> getData() {
        return DATA;
    }

    public static AccountData createAccount(String authToken, StompHeaderAccessor accessor) {
        AccountData data = new AccountData();
        data.setAuthToken(authToken);
        data.setUsername(generateRandomUser());
        data.setAccessor(accessor);
        DATA.add(data);
        return data;
    }
}
