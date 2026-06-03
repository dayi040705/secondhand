package com.demo.secondhand.controller;

import com.demo.secondhand.entity.Order;
import com.demo.secondhand.entity.Product;
import com.demo.secondhand.mapper.OrderMapper;
import com.demo.secondhand.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ProductMapper productMapper;

    // 我的购买（买家视角）
    @GetMapping("/buyer")
    public String buyerOrders(HttpSession session, Model model) {
        Integer buyerId = (Integer) session.getAttribute("userId");
        if (buyerId == null) return "redirect:/login";
        model.addAttribute("orders", orderMapper.findByBuyerId(buyerId));
        return "orders_buyer";
    }

    // 我卖出的（卖家视角）
    @GetMapping("/seller")
    public String sellerOrders(HttpSession session, Model model) {
        Integer sellerId = (Integer) session.getAttribute("userId");
        if (sellerId == null) return "redirect:/login";
        model.addAttribute("orders", orderMapper.findBySellerId(sellerId));
        return "orders_seller";
    }

    // 卖家确认订单（待确认 → 待收货）
    @GetMapping("/confirm/{orderNo}")
    public String confirmOrder(@PathVariable String orderNo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order != null && order.getSellerId().equals(userId) && order.getStatus() == 0) {
            order.setStatus(1);
            order.setConfirmTime(new Date());
            orderMapper.updateStatus(order);
        }
        return "redirect:/orders/seller";
    }

    // 买家确认收货（待收货 → 已完成）
    @GetMapping("/complete/{orderNo}")
    public String completeOrder(@PathVariable String orderNo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order != null && order.getBuyerId().equals(userId) && order.getStatus() == 1) {
            order.setStatus(2);
            order.setConfirmTime(new Date());
            orderMapper.updateStatus(order);
            // 可选：将商品状态改为已下架（0）
            Product product = productMapper.findById(order.getProductId());
            if (product != null && product.getStatus() == 1) {
                product.setStatus(0);
                productMapper.update(product);
            }
        }
        return "redirect:/orders/buyer";
    }

    // 取消订单（仅待确认状态可取消，买家和卖家均可取消）
    @GetMapping("/cancel/{orderNo}")
    public String cancelOrder(@PathVariable String orderNo, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        Order order = orderMapper.findByOrderNo(orderNo);
        if (order != null && order.getStatus() == 0) {
            if (order.getBuyerId().equals(userId) || order.getSellerId().equals(userId)) {
                order.setStatus(3);
                order.setConfirmTime(new Date());
                orderMapper.updateStatus(order);
            }
        }
        // 取消后跳转回买家或卖家列表，根据当前用户角色决定
        String referer = (String) session.getAttribute("lastOrderPage");
        if (order != null && order.getBuyerId().equals(userId)) {
            return "redirect:/orders/buyer";
        } else {
            return "redirect:/orders/seller";
        }
    }
}