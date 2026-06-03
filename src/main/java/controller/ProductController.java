package com.demo.secondhand.controller;

import com.demo.secondhand.entity.User;
import com.demo.secondhand.mapper.UserMapper;
import com.demo.secondhand.entity.Comment;
import com.demo.secondhand.mapper.CommentMapper;
import com.demo.secondhand.entity.Product;
import com.demo.secondhand.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.demo.secondhand.entity.Order;
import com.demo.secondhand.mapper.OrderMapper;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class ProductController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    // 显示发布商品页面
    @GetMapping("/publish")
    public String publishPage() {
        return "publish";
    }

    // 处理商品发布（含图片上传）
    @PostMapping("/publish")
    public String publish(Product product, @RequestParam("file") MultipartFile file, HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        // 检查用户是否已通过校内认证
        User user = userMapper.findById(userId);
        if (user.getVerified() == null || user.getVerified() == 0) {
            model.addAttribute("error", "您尚未通过校内认证，暂不能发布商品。请联系管理员。");
            return "publish";
        }
        // 处理图片上传
        if (!file.isEmpty()) {
            try {
                // 获取项目运行目录下的 uploads 文件夹（绝对路径）
                String path = System.getProperty("user.dir") + "/uploads/";
                File dir = new File(path);
                if (!dir.exists()) dir.mkdirs();
                if (!dir.exists()) {
                    boolean created = dir.mkdirs();
                    System.out.println("创建 uploads 目录结果：" + created);
                }
                // 生成唯一文件名
                String originalFilename = file.getOriginalFilename();
                String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                String filename = UUID.randomUUID().toString() + ext;
                File dest = new File(path + filename);
                file.transferTo(dest);
                // 保存相对路径，用于前端显示
                product.setImagePath("/uploads/" + filename);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("error", "图片上传失败");
                return "publish";
            }
        }
        product.setUserId(userId);
        product.setStatus(1);
        product.setCreateTime(new Date());
        productMapper.insert(product);
        return "redirect:/";
    }

    // 首页：展示在售商品列表
    @GetMapping("/")
    public String index(Model model) {
        List<Product> products = productMapper.findAllOnSale();
        model.addAttribute("products", products);
        model.addAttribute("currentCategory", "all");
        return "index";
    }

    // 商品详情页
    // 修改 detail 方法以支持评论列表（需要修改原有的 detail 方法，不是新增）
    @GetMapping("/product/{id}")
    public String detail(@PathVariable Integer id,
                         @RequestParam(value = "error", required = false) String error,
                         Model model, HttpSession session) {
        Product product = productMapper.findById(id);
        List<Comment> comments = commentMapper.findByProductId(id);
        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUserId", session.getAttribute("userId"));
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "detail";
    }

    // 新增：处理评论提交
    @PostMapping("/comment")
    public String addComment(@RequestParam("productId") Integer productId,
                             @RequestParam("content") String content,
                             HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("username");
        Comment comment = new Comment();
        comment.setProductId(productId);
        comment.setUserId(userId);
        comment.setUsername(username);
        comment.setContent(content);
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
        return "redirect:/product/" + productId;
    }

    // 新增：删除评论（仅作者或管理员可删）
    @GetMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Integer id,
                                @RequestParam("productId") Integer productId,
                                HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        Comment comment = commentMapper.findById(id);
        if (comment != null && (comment.getUserId().equals(userId) || "admin".equals(role))) {
            commentMapper.deleteById(id);
        }
        return "redirect:/product/" + productId;
    }

    // 我的发布
    @GetMapping("/myProducts")
    public String myProducts(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) return "redirect:/login";
        List<Product> products = productMapper.findByUserId(userId);
        model.addAttribute("products", products);
        return "myProducts";
    }

    // 删除商品
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        productMapper.deleteById(id);
        return "redirect:/myProducts";
    }

    // 显示编辑页面
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Integer id, Model model) {
        Product product = productMapper.findById(id);
        model.addAttribute("product", product);
        return "edit";
    }

    // 处理编辑
    @PostMapping("/edit")
    public String edit(Product product) {
        productMapper.update(product);
        return "redirect:/myProducts";
    }

    // 搜索
    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        List<Product> products = productMapper.searchByTitle(keyword);
        model.addAttribute("products", products);
        return "index";
    }
    @GetMapping("/category/{category}")
    public String category(@PathVariable String category, Model model) {
        List<Product> products = productMapper.findByCategory(category);
        model.addAttribute("products", products);
        model.addAttribute("currentCategory", category); // 用于高亮当前分类
        return "index";
    }
    @PostMapping("/buy")
    public String buy(@RequestParam("productId") Integer productId, HttpSession session) {
        Integer buyerId = (Integer) session.getAttribute("userId");
        if (buyerId == null) {
            return "redirect:/login";
        }
        Product product = productMapper.findById(productId);
        if (product == null || product.getStatus() != 1) {
            String error = URLEncoder.encode("商品不存在或已下架", StandardCharsets.UTF_8);
            return "redirect:/product/" + productId + "?error=" + error;
        }
        if (product.getUserId().equals(buyerId)) {
            String error = URLEncoder.encode("不能购买自己发布的商品", StandardCharsets.UTF_8);
            return "redirect:/product/" + productId + "?error=" + error;
        }
        List<Order> activeOrders = orderMapper.findActiveByProductId(productId);
        if (!activeOrders.isEmpty()) {
            String error = URLEncoder.encode("该商品已有买家下单，暂不能购买", StandardCharsets.UTF_8);
            return "redirect:/product/" + productId + "?error=" + error;
        }
        // 创建订单对象
        Order order = new Order();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 20));
        order.setProductId(productId);
        order.setBuyerId(buyerId);
        order.setSellerId(product.getUserId());
        order.setPrice(product.getPrice());
        order.setStatus(0);
        order.setCreateTime(new Date());
        orderMapper.insert(order);
        productMapper.updateStatus(productId, 2);   // 2 表示已被购买/锁定
        return "redirect:/orders/buyer";
    }
}