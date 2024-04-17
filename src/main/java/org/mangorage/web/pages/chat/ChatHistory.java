package org.mangorage.web.pages.chat;

import java.util.List;

public record ChatHistory(List<ChatMessage> messages) {
    public String getChatLogForUser(String authToken) {
        StringBuilder builder = new StringBuilder();

        for (ChatMessage message : messages) {
            if (authToken != null && message.target() != null) {
                if (message.target().equals(authToken)) {
                    builder.append("[%s]: %s \n".formatted(message.username(), message.message()));
                }
            } else {
                if (!message.username().equals("System"))
                    builder.append("[%s]: %s \n".formatted(message.username(), message.message()));
            }
        }

        return builder.toString();
    }

    public void addMessage(String useranme, String message) {
        messages.add(new ChatMessage(useranme, message, null));
    }

    public void addSystemMessage(String authToken, String message) {
        messages.add(new ChatMessage("System", message, authToken));
    }
}
