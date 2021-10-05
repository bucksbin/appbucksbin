package com.commercial.tuds.earnandpay.Models;

public class DiscountCardPojo {


    String customer_name;
    String customer_mobile;
    String destination_id;
    String discount_percentage;
    String shop_address;
    String shop_name;
    String source_id;

    DiscountCardPojo()
    {

    }

    public DiscountCardPojo(String customer_name, String customer_mobile, String destination_id, String discount_percentage, String shop_address, String shop_name, String source_id) {
        this.customer_name = customer_name;
        this.customer_mobile = customer_mobile;
        this.destination_id = destination_id;
        this.discount_percentage = discount_percentage;
        this.shop_address = shop_address;
        this.shop_name = shop_name;
        this.source_id = source_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_address() {
        return shop_address;
    }

    public void setShop_address(String shop_address) {
        this.shop_address = shop_address;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_mobile() {
        return customer_mobile;
    }

    public void setCustomer_mobile(String customer_mobile) {
        this.customer_mobile = customer_mobile;
    }
    public String getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(String discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }



}