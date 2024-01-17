package com.oriensolutions.newtech.model;

public class Order {
    private String order_id;
    private int qty;
    private double price;
    private String user;
    private String item;
    private String name;
    private String image;

    public Order() {
    }

    public Order(String order_id, int qty, double price, String user, String item, String name, String image) {
        this.order_id = order_id;
        this.qty = qty;
        this.price = price;
        this.user = user;
        this.item = item;
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
