package com.example.youtube_viewer.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.youtube_viewer.entity.Playlist;
import com.example.youtube_viewer.entity.User;
import com.example.youtube_viewer.service.PlaylistService;
import com.example.youtube_viewer.service.UserService;



@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @Autowired
    private PlaylistService playlistService;

    @GetMapping("/")
    public String index(Model model, Principal principal) {
        model.addAttribute("title", "Trang chủ");
        if (principal != null) {
            model.addAttribute("username", principal.getName());
        }
        return "index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("title", "Tìm kiếm");
        model.addAttribute("query", q != null ? q : "");
        return "search";
    }

    @GetMapping("/login")
    public String login(Model model, Principal principal) {
        // Nếu đã đăng nhập rồi thì redirect về trang chủ
        if (principal != null) {
            return "redirect:/";
        }
        
        model.addAttribute("title", "Đăng nhập");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, Principal principal) {
        // Nếu đã đăng nhập rồi thì redirect về trang chủ
        if (principal != null) {
            return "redirect:/";
        }
        
        model.addAttribute("title", "Đăng ký");
        return "register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String username,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String confirmPassword,
                              Model model,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {
        
        // Nếu đã đăng nhập rồi thì redirect về trang chủ
        if (principal != null) {
            return "redirect:/";
        }
        
        try {
            // Validate input
            if (username == null || username.trim().isEmpty()) {
                model.addAttribute("error", "Tên đăng nhập không được để trống");
                return "register";
            }
            
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("error", "Email không được để trống");
                return "register";
            }
            
            if (password == null || password.length() < 6) {
                model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
                return "register";
            }

            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Mật khẩu xác nhận không khớp");
                return "register";
            }

            // Create user
            userService.createUser(username.trim(), email.trim(), password);
            
            // Success - redirect to login with success message
            redirectAttributes.addFlashAttribute("success", 
                "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login";
            
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra. Vui lòng thử lại.");
            return "register";
        }
    }

    @GetMapping("/history")
    public String history(Model model, Principal principal) {
        model.addAttribute("title", "Lịch sử xem");
        model.addAttribute("username", principal.getName());
        return "history";
    }

    // @GetMapping("/playlists")
    // public String playlists(Model model, Principal principal) {
    //     model.addAttribute("title", "Danh sách phát");
    //     model.addAttribute("username", principal.getName());
    //     return "playlists";
    // }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        model.addAttribute("title", "Hồ sơ cá nhân");
        model.addAttribute("username", principal.getName());
        User user = userService.findByUsername(principal.getName()).orElse(null);
        model.addAttribute("user", user);
        return "profile";
    }

    // Helper method để check authentication
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && 
               authentication.isAuthenticated() && 
               !authentication.getName().equals("anonymousUser");
    }

    @GetMapping("/playlists")
    public String playlists(Model model, Principal principal) {
        if (!isAuthenticated()) {
            return "redirect:/login";
        }
        User user = userService.findByUsername(principal.getName()).orElse(null);
        // List<Playlist> playlists = PlaylistService.getUserPlaylists(user.getId());
        List<Playlist> playlists = playlistService.getUserPlaylists(user.getId());    
        model.addAttribute("playlists", playlists);
        // model.addAttribute("PlaylistDto", new PlaylistDto());
        model.addAttribute("title", "Danh sách phát");
        model.addAttribute("username", principal.getName());
        return "playlists";
    }
   
    @PostMapping("create-playlist")
    public String createPlaylist(@RequestParam String name,
                                 @RequestParam String description,
                                 @RequestParam(value = "isPublic", required = false) Boolean isPublic,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 Principal principal) {
        if (!isAuthenticated()) {
            return "redirect:/login";
        }
        
        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại");
            return "redirect:/playlists";
        }
        
        try {
            if (name == null || name.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Tên danh sách phát không được để trống");
                return "redirect:/playlists";
            }
            if (description == null) {
                description = ""; // Default to empty string if not provided
            }
            if (isPublic == null) {
                isPublic = false; // Default to false if not provided
            }
            Playlist playlist = playlistService.createPlaylist(user.getId(), name, description, isPublic);
            redirectAttributes.addFlashAttribute("success", "Tạo danh sách phát thành công: " + playlist.getName());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo danh sách phát: " + e.getMessage());
        }
        
        return "redirect:/playlists";
    }
    
    // @PostMapping("/create-playlist")
    // public String createPlaylist(@ModelAttribute PlaylistDto playlistDto,
    //                              Model model,
    //                              RedirectAttributes redirectAttributes, 
    //                                 Principal principal) {
    //     if (!isAuthenticated()) {
    //         return "redirect:/login";   
        
    //     }
    //     User user = userService.findByUsername(principal.getName()).orElse(null);
    //     if (user == null) { 
    //         redirectAttributes.addFlashAttribute("error", "Người dùng không tồn tại");
    //         return "redirect:/playlists";
    //     }   
    //     try {
    //         if (playlistDto.getName() == null || playlistDto.getName().trim().isEmpty()) {
    //             redirectAttributes.addFlashAttribute("error", "Tên danh sách phát không được để trống");
    //             return "redirect:/playlists";
    //         }
    //         if (playlistDto.getDescription() == null) {
    //             playlistDto.setDescription(""); // Default to empty string if not provided
    //         }
    //         if (!playlistDto.isPublic()) {
    //             playlistDto.setPublic(false); // Default to false if not provided
    //         }
    //         Playlist playlist = playlistService.createPlaylistDto(playlistDto);
    //         redirectAttributes.addFlashAttribute("success", "Tạo danh sách phát thành công: " + playlist.getName());
    //     } catch (Exception e) {
    //         redirectAttributes.addFlashAttribute("error", "Lỗi khi tạo danh sách phát: " + e.getMessage());
    //     }
    //     return "redirect:/playlists";
                                    
    // }

    @GetMapping("/playlist/{id}")
    public String playlistDetailString(@PathVariable Long id, 
                                       Model model, 
                                       Principal principal) {
        if (!isAuthenticated()) {
            return "redirect:/login";
        }

        User user = userService.findByUsername(principal.getName()).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        Playlist playlist = playlistService.findById(id).orElse(null);
        if (playlist == null || !playlist.getUserId().equals(user.getId())) {
            return "redirect:/playlists";
        }

        model.addAttribute("playlist", playlist);
        return "playlist-detail";
    
    }
}