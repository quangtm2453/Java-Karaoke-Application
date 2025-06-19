package com.example.youtube_viewer.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.debug("Showing registration form");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult result, Model model) {
        logger.debug("Processing registration for username: {}", user.getUsername());

        // Manual validation
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            result.addError(new FieldError("user", "password", "Password is required"));
        } else if (user.getPassword().length() < 6) {
            result.addError(new FieldError("user", "password", "Password must be at least 6 characters"));
        }

        // Check for existing username
        if (userService.existsByUsername(user.getUsername())) {
            result.addError(new FieldError("user", "username", "Username is already taken"));
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "auth/register";
        }

        try {
            User registeredUser = userService.registerUser(user);
            logger.info("Successfully registered user: {}", registeredUser.getUsername());
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            logger.error("Registration failed", e);
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", user);
            return "auth/register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, 
                       @RequestParam String password, 
                       HttpSession session,
                       Model model) {
        Optional<User> userOpt = userService.findByUsername(username);
        
        if (userOpt.isPresent() && userService.verifyPassword(password, userOpt.get().getPasswordHash())) {
            User user = userOpt.get();
            // Store user info in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            // Update last login time
            userService.updateLastLogin(user.getId());
            logger.info("User logged in successfully: {}", username);
            return "redirect:/";
        }
        
        model.addAttribute("error", "Invalid username or password");
        return "auth/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }
}
