package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("form")
    public String showForm() {
        return "form";
    }

    @PostMapping("submit")
    public String submitForm(@RequestParam String name, @RequestParam String email, Model model) {
        userService.saveUser(new User(name, email));
        model.addAttribute("message", "User saved successfully");
        return "form";
    }
}