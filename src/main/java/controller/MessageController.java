package com.demo.secondhand.controller;

import com.demo.secondhand.entity.Message;
import com.demo.secondhand.mapper.MessageMapper;
import com.demo.secondhand.mapper.ProductMapper;
import com.demo.secondhand.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import com.demo.secondhand.entity.Product;

@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ProductMapper productMapper;

    // 发送消息表单页面（商品详情页调用）
    @GetMapping("/send")
    public String sendPage(@RequestParam("toUserId") Integer toUserId,
                           @RequestParam(value = "productId", required = false) Integer productId,
                           Model model) {
        model.addAttribute("toUserId", toUserId);
        model.addAttribute("productId", productId);
        // 可选：获取接收者用户名
        model.addAttribute("toUsername", userMapper.findById(toUserId).getUsername());
        if (productId != null) {
            Product product = productMapper.findById(productId);
            model.addAttribute("productTitle", product.getTitle());
        }
        return "message_send";
    }

    // 处理发送消息
    @PostMapping("/send")
    public String send(@RequestParam("toUserId") Integer toUserId,
                       @RequestParam(value = "productId", required = false) Integer productId,
                       @RequestParam("content") String content,
                       HttpSession session) {
        Integer fromUserId = (Integer) session.getAttribute("userId");
        if (fromUserId == null) {
            return "redirect:/login";
        }
        if (fromUserId.equals(toUserId)) {
            return "redirect:/message/send?toUserId=" + toUserId + "&error=不能给自己发消息";
        }
        Message msg = new Message();
        msg.setFromUserId(fromUserId);
        msg.setToUserId(toUserId);
        msg.setProductId(productId);
        msg.setContent(content);
        msg.setSendTime(new Date());
        msg.setIsRead(0);
        messageMapper.insert(msg);
        return "redirect:/message/inbox";
    }

    // 收件箱
    @GetMapping("/inbox")
    public String inbox(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        List<Message> messages = messageMapper.findByToUserId(userId);
        model.addAttribute("messages", messages);
        return "message_inbox";
    }

    // 发件箱
    @GetMapping("/outbox")
    public String outbox(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        List<Message> messages = messageMapper.findByFromUserId(userId);
        model.addAttribute("messages", messages);
        return "message_outbox";
    }

    // 查看消息详情（标记为已读）
    @GetMapping("/view/{id}")
    public String view(@PathVariable Integer id, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        Message msg = messageMapper.findById(id); // 需要 MessageMapper 中添加 findById
        if (msg == null) return "redirect:/message/inbox";
        // 只有收件人才能标记已读
        if (msg.getToUserId().equals(userId) && msg.getIsRead() == 0) {
            messageMapper.markAsRead(id);
        }
        model.addAttribute("msg", msg);
        // 可选：关联的商品信息
        if (msg.getProductId() != null) {
            model.addAttribute("product", productMapper.findById(msg.getProductId()));
        }
        return "message_view";
    }

    // 快捷回复：跳转到发送消息页面，自动填充接收者
    @GetMapping("/reply")
    public String reply(@RequestParam("toUserId") Integer toUserId,
                        @RequestParam(value = "productId", required = false) Integer productId,
                        Model model) {
        model.addAttribute("toUserId", toUserId);
        model.addAttribute("productId", productId);
        model.addAttribute("toUsername", userMapper.findById(toUserId).getUsername());
        return "message_send";
    }
}