package com.example.miniproject.util;

import org.springframework.web.servlet.view.RedirectView;

public class RedirectUtil {
    public static RedirectView redirect(String url) {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}