package com.commercial.tuds.earnandpay.Models;

public class BuyDiscountCardPojo {
    int price;
    int number_of_cards;



    BuyDiscountCardPojo(){

    }

    public BuyDiscountCardPojo(int price,int number_of_cards) {
        this.price = price;
        this.number_of_cards = number_of_cards;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getNumber_of_cards() {
        return number_of_cards;
    }

    public void setNumber_of_cards(int number_of_cards) {
        this.number_of_cards = number_of_cards;
    }

}
