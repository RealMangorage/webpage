package org.mangorage.web.pages.redirect;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RedirectPage {
    public static final Map<String, String> DIRECTS = new HashMap<>();

    static {
        DIRECTS.put("mc", "https://minecraft.net/");
        DIRECTS.put("mcforge", "https://minecraftforge.net/");
    }

    @RequestMapping("/r")
    public RedirectView redirectToDestination(@RequestParam("id") String id) {
        String destination = getDestination(id);
        return new RedirectView(destination);
    }

    private String getDestination(String id) {
        return DIRECTS.getOrDefault(id, "mangorage.org/home");
    }
}
