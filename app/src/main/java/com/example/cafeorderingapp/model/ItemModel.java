package com.example.cafeorderingapp.model;

public class ItemModel {
    String itemName, itemDes;
    int count;
    Integer price;

    public ItemModel(String itemName, String itemDes, Integer price) {
        this.itemName = itemName;
        this.itemDes = itemDes;
        this.count = 0;
        this.price = price;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
