package org.mangorage.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Core.class, args);
    }

    @RestController
    public static class Core {
        @RequestMapping("/redirect")
        public RedirectView redirectToDestination(@RequestParam("id") String id) {
            String destination = getDestination(id);
            return new RedirectView(destination);
        }

        @RequestMapping("/")
        public String helloWorld() {

            System.out.println("LOL Hello!");
            return "hello";
        }

        private String getDestination(String id) {
            if ("survey".equals(id)) {
                return "https://example.com/survey";
            } else if ("blog".equals(id)) {
                return "https://example.com/blog";
            } else {
                return "https://example.com/default";
            }
        }
    }
}
