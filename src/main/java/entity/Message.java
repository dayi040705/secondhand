package com.demo.secondhand.entity;

import java.util.Date;

public class Message {
    private Integer id;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer productId;
    private String content;
    private Date sendTime;
    private Integer isRead;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFromUserId() { return fromUserId; }
    public void setFromUserId(Integer fromUserId) { this.fromUserId = fromUserId; }
    public Integer getToUserId() { return toUserId; }
    public void setToUserId(Integer toUserId) { this.toUserId = toUserId; }
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getSendTime() { return sendTime; }
    public void setSendTime(Date sendTime) { this.sendTime = sendTime; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
}