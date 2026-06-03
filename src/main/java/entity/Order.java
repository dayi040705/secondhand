package com.demo.secondhand.entity;

import java.math.BigDecimal;
import java.util.Date;

public class Order {
    private Integer id;
    private String orderNo;          // 订单号
    private Integer productId;       // 商品ID
    private Integer buyerId;         // 买家ID
    private Integer sellerId;        // 卖家ID
    private BigDecimal price;        // 成交价格快照
    private Integer status;          // 0待确认,1待收货,2已完成,3已取消
    private Date createTime;         // 下单时间
    private Date confirmTime;        // 确认/完成时间

    // 所有 getter 和 setter（使用 IDEA 快捷键 Alt+Insert 自动生成）
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public Integer getBuyerId() { return buyerId; }
    public void setBuyerId(Integer buyerId) { this.buyerId = buyerId; }
    public Integer getSellerId() { return sellerId; }
    public void setSellerId(Integer sellerId) { this.sellerId = sellerId; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getConfirmTime() { return confirmTime; }
    public void setConfirmTime(Date confirmTime) { this.confirmTime = confirmTime; }
}