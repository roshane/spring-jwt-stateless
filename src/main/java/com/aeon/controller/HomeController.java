package com.aeon.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.security.auth.UserPrincipal;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collection;

/**
 * Created by roshane on 2/25/17.
 */
@Controller
public class HomeController {


    @RequestMapping("/")
    public String redirectToHome() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String getHomePage(Model model) {
        return "index";
    }

    @RequestMapping("/admin")
    public String getAdminPage(Model model, Principal principal) {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities();
        System.out.println("user roles"+authorities);
        model.addAttribute("principal", principal);
        return "admin";
    }
}
