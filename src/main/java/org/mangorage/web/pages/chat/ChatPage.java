package org.mangorage.web.pages.chat;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mangorage.web.pages.chat.accounts.AccountData;
import org.mangorage.web.pages.chat.accounts.Accounts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@RestController
public class ChatPage {

    private static StringBuilder builder = new StringBuilder();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/submitchat")
    @SendTo("/topic/chat")
    public String handleFormSubmit(@Payload String msg) {
        // Create ObjectMapper instance
        // Parse JSON string to JSONObject
        String jsonString = msg;

        // Remove curly braces and split the string by comma
        String[] keyValuePairs = jsonString.replaceAll("[{}]", "").split(",");

        // Create variables to store auth and message
        String auth = null;
        String message = null;

        // Loop through the key-value pairs
        for (String pair : keyValuePairs) {
            // Split each pair into key and value
            String[] entry = pair.split(":");

            // Remove leading and trailing whitespace and quotes
            String key = entry[0].trim().replaceAll("\"", "");
            String value = entry[1].trim().replaceAll("\"", "");

            // Assign values to variables based on key
            if (key.equals("auth")) {
                auth = value;
            } else if (key.equals("message")) {
                message = value;
            }
        }

        // Print the parsed fields
        System.out.println("AuthID: " + auth);
        System.out.println("Message: " + message);


        AccountData data = Accounts.findAccount(auth);
        if (data != null) {
            builder.append(String.format("[%s]: %s \n", data.getUsername(), message));
            messagingTemplate.convertAndSend("/topic/chat", builder.toString());
        }

        // Access fields of the Java object
        return builder.toString();
    }

    private Cookie findCookie(String name, Cookie[] cookies) {
        if (cookies == null) return null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }
        return null;
    }

    @RequestMapping("/chat")
    public String chatForm(HttpServletRequest request, HttpServletResponse response, StompHeaderAccessor accessor) {


        var existingCookie =  findCookie("authToken", request.getCookies());

        if (existingCookie == null || Accounts.findAccount(existingCookie.getValue()) == null) {
            var token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("authToken", token);
            cookie.setMaxAge(3600);
            response.addCookie(cookie);

            Accounts.createAccount(token, accessor);
            System.out.println("Created new Account!");
        } else {
            System.out.println(existingCookie.getValue() + "< ----");
        }

        // Pass the chat log to the Thymeleaf template
        String chatLog = builder.toString();
        String htmlContent = "<html><body>"
                + "<h2>Chat Log:</h2>"
                + "<textarea id='chatLog' name='chatLog' rows='10' cols='50' readonly>" + chatLog + "</textarea><br><br>"
                + "<html><body>"
                + "<h2>Enter Message:</h2>"
                + "<form id='chatForm' onsubmit='sendMessage(event)'>" // Form submission handled by JavaScript function
                + "  <label for='message'>Message:</label>"
                + "  <input type='text' id='message' name='message'><br><br>"
                + "  <input type='submit' value='Send'>"
                + "</form>"
                + "<script>"
                + """
                    function getCookie(cookieName) {
                        // Split cookies into an array
                        var cookies = document.cookie.split(';');
                                    
                        // Loop through the cookies array
                        for (var i = 0; i < cookies.length; i++) {
                            var cookie = cookies[i].trim();
                                    
                            // Check if the cookie starts with the specified name
                            if (cookie.startsWith(cookieName + '=')) {
                                // Return the value of the cookie (substring after the '=')
                                return cookie.substring(cookieName.length + 1);
                            }
                        }
                                    
                        // If cookie not found, return null
                        return null;
                    }
                """
                + "function sendMessage(event) {"
                + "    event.preventDefault();" // Prevent default form submission behavior
                + "    var authToken = getCookie('authToken');"
                + "    var message = document.getElementById('message').value;"
                + "    var formData = {auth: authToken, message: message};"
                + "    stompClient.send('/app/submitchat', {}, JSON.stringify(formData));" // Send form data via WebSocket
                + "}"
                + "</script>"
                + "</body></html>" + getWebSocketScript();


        return htmlContent;
    }

    private String getWebSocketScript() {
        return  "<script src=\"https://cdn.jsdelivr.net/npm/sockjs-client@1.5.1/dist/sockjs.min.js\"></script>" +
                "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js\"></script>" +
                "<script>" +
                "var socket = new SockJS('/chat');" +
                "var stompClient = Stomp.over(socket);" +
                "stompClient.connect({}, function(frame) {" +
                "    console.log('Connected: ' + frame);" +
                "    stompClient.subscribe('/topic/chat', function(message) {" +
                "        var messageBody = message.body;" + // Get the message body
                "        var startIndex = messageBody.indexOf('\\n\\n') + 1;" + // Find the index of the first blank line
                "        var messageContent = messageBody.substring(startIndex).trim();" + // Extract content after the first blank line
                "        document.getElementById('chatLog').value = messageContent;" // Set textarea value
                + "var chatLogTextArea = document.getElementById('chatLog');"
                + "chatLogTextArea.scrollTop = chatLogTextArea.scrollHeight;" + // Set scroll position to bottom
                "    });" +
                "});" +
                "</script>";
    }
}