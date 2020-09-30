package com.bobansavic.agility.mvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WelcomeController {

    @Value("${vaadin.servlet.urlPrefix}")
    private String urlPrefix;

    @RequestMapping(value = "/")
    public String welcomeRedirect() {
        return "redirect:" + getUiPrefix();
    }

    private String getUiPrefix() {
        return urlPrefix;
    }

}
