package org.mangorage.web.pages.chat;

/**
 *
 * @param username -> the user who sent it
 * @param message -> the message
 * @param target -> null by default, means its for everyone. If not null, will resemble the Auth Token for said user. Server Sided only.
 */
public record ChatMessage(String username, String message, String target) {
}
