package com.demo.secondhand.controller;

import com.demo.secondhand.entity.User;
import com.demo.secondhand.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;   // ← 注意这里是 jakarta，不是 javax
import java.util.Date;

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(User user, Model model) {
        // 检查用户名是否已存在
        if (userMapper.findByUsername(user.getUsername()) != null) {
            model.addAttribute("error", "用户名已存在");
            return "register";
        }
        // 注意：生产环境需要对密码加密，这里仅作演示
        user.setPassword(user.getPassword());
        user.setCreateTime(new Date());
        userMapper.insert(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, Model model) {
        User user = userMapper.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
        session.setAttribute("userId", user.getId());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        return "redirect:/";  // 跳转到首页（稍后实现）
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}