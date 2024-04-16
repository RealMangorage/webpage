package org.mangorage.web.pages.home;

import org.mangorage.web.pages.redirect.RedirectPage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
public class HomePage {
    @RequestMapping("/home")
    public String helloWorld() {

        // Create the initial string
        String preData = "<html><body>"
                + "<h2>Enter Data:</h2>"
                + "<form action='/submit' method='post'>"
                + "  <label for='data1'>Vanity Url:</label>"
                + "  <input type='text' id='data1' name='data1'><br><br>"
                + "  <label for='data2'>Destination:</label>"
                + "  <input type='text' id='data2' name='data2'><br><br>"
                + "  <input type='submit' value='Submit'>"
                + "</form>"
                + "</body></html>";


        String data = preData + "Home Page!<br><br>" + "URL Redirects (mangorage.org/r?id=value): <br><br>";

        // Iterate over the map entries and append them to the data string
        for (Map.Entry<String, String> entry : RedirectPage.DIRECTS.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            data += k + " -> " + v + "<br>";
        }

        // Return the final data string
        return data;
    }

    @RequestMapping("/")
    public RedirectView redirectToHome() {
        return new RedirectView("home");
    }

    @PostMapping("/submit")
    public RedirectView handleFormSubmit(@RequestParam("data1") String vanity, @RequestParam("data2") String dest) {
        if (!RedirectPage.DIRECTS.containsKey(vanity))
            RedirectPage.DIRECTS.put(vanity, dest);
        return new RedirectView("home");
    }

}
