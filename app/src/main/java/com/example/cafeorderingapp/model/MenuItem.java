package com.example.cafeorderingapp.model;

public class MenuItem {
    String itemName, itemDes;
    Integer itemPrice, count;
    String itemId;

    String imageUrl;

    public MenuItem(String imageUrl, String itemDes, String itemName, Integer itemPrice, String itemId) {
        this.imageUrl = imageUrl;
        this.itemDes = itemDes;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemId = itemId;
        this.count = 0;
    }


    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public MenuItem() {
        this.count = 0;
    }

    public Integer getCount() {
        return count;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDes() {
        return itemDes;
    }

    public void setItemDes(String itemDes) {
        this.itemDes = itemDes;
    }

    public Integer getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Integer itemPrice) {
        this.itemPrice = itemPrice;
    }
}
