package com.demo.secondhand;

import com.demo.secondhand.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private MessageMapper messageMapper;

    @ModelAttribute
    public void addUnreadCount(HttpSession session, org.springframework.ui.Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId != null) {
            int count = messageMapper.countUnread(userId);
            model.addAttribute("unreadCount", count);
        }
    }
}