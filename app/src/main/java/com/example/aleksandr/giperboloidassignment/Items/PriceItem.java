package com.example.aleksandr.giperboloidassignment.Items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by aleksandr on 21.09.15.
 */

public class PriceItem implements Parcelable {
    private String description;
    private int price;

    private PriceItem(){
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public static Builder builder(){
        return new PriceItem().new Builder();
    }

    public class Builder{
        private Builder(){
        }

        public Builder setDescription(String description) {
            PriceItem.this.description = description;
            return this;
        }

        public Builder setPrice(int price) {
            PriceItem.this.price = price;
            return this;
        }

        public PriceItem build(){
            return PriceItem.this;
        }
    }

    public PriceItem (Parcel in){
        description = in.readString();
        price = in.readInt();
    }

    public static final Parcelable.Creator<PriceItem> CREATOR = new Parcelable.Creator<PriceItem>(){
        public PriceItem createFromParcel(Parcel in) {
            return new PriceItem(in);
        }
        public PriceItem[] newArray (int size) {
            return new PriceItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString (description);
        dest.writeInt(price);
    }
}
