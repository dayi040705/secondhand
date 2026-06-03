package com.demo.secondhand.controller;

import com.demo.secondhand.mapper.UserMapper;
import com.demo.secondhand.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");
        if (role == null || !"admin".equals(role)) {
            return "redirect:/";
        }
        model.addAttribute("userCount", userMapper.countAll());
        model.addAttribute("productCount", productMapper.countAll());
        model.addAttribute("products", productMapper.findAll());
        model.addAttribute("users", userMapper.findAll());
        return "admin/dashboard";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Integer id) {
        userMapper.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productMapper.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/verifyUser/{id}")
    public String verifyUser(@PathVariable Integer id) {
        userMapper.updateVerified(id, 1);
        return "redirect:/admin/dashboard";
    }
}