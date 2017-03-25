package com.apptree.newspot;

public class SelectedOrderPojo {
    private String orderCat;
    private String orderDate;
    private String orderDesc;
    private String orderId;
    private String orderName;
    private String orderPath;
    private String orderPrice;
    private String qty;
    private String total;

    public SelectedOrderPojo(String orderId, String orderName, String orderDesc, String orderPrice, String orderCat, String orderPath, String orderDate, String total) {
        this.orderId = orderId;
        this.orderName = orderName;
        this.orderDesc = orderDesc;
        this.orderPrice = orderPrice;
        this.orderCat = orderCat;
        this.orderPath = orderPath;
        this.orderDate = orderDate;
        this.total = total;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return this.orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderDesc() {
        return this.orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderPrice() {
        return this.orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderCat() {
        return this.orderCat;
    }

    public void setOrderCat(String orderCat) {
        this.orderCat = orderCat;
    }

    public String getOrderPath() {
        return this.orderPath;
    }

    public void setOrderPath(String orderPath) {
        this.orderPath = orderPath;
    }

    public String getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotal() {
        return this.total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getQty() {
        return this.qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
