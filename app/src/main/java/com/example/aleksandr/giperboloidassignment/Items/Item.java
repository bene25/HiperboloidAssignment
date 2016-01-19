package com.example.aleksandr.giperboloidassignment.Items;

import java.util.ArrayList;

import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;

/**
 * Created by aleksandr on 21.09.15.
 */
public class Item {
    private String title;
    private double latitude;
    private double longitude;
    private String text;
    private String longText;
    private String locationText;
    private Long date;
    private BalloonItem balloonItem;
    private ArrayList <PriceItem> priceList;

    private Item(){
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getText() {
        return text;
    }

    public String getLongText() {
        return longText;
    }

    public String getLocationText() {
        return locationText;
    }

    public Long getDate() {
        return date;
    }

    public ArrayList <PriceItem> getPriceList() {
        return priceList;
    }

    public BalloonItem getBalloonItem() {
        return this.balloonItem;
    }

    public void setBalloonItem(BalloonItem balloonItem) {
        this.balloonItem = balloonItem;
    }

    public static Builder builder(){
        return new Item().new Builder();
    }

    public class Builder{
        private Builder(){
        }
        public Builder setTitle (String title){
            Item.this.title = title;
            return this;
        }
        public Builder setLatitude (double latitude){
            Item.this.latitude = latitude;
            return this;
        }

        public Builder setLongitude(double longitude) {
            Item.this.longitude = longitude;
            return this;
        }

        public Builder setText(String text) {
            Item.this.text = text;
            return this;
        }

        public Builder setLongText(String longText) {
            Item.this.longText = longText;
            return this;
        }

        public Builder setLocationText(String locationText) {
            Item.this.locationText = locationText;
            return this;
        }

        public Builder setDate(Long date) {
            Item.this.date = date;
            return this;
        }

        public Builder setPriceList(ArrayList <PriceItem> priceList) {
            Item.this.priceList = priceList;
            return this;
        }
        public Item build(){
            return Item.this;
        }

    }

}