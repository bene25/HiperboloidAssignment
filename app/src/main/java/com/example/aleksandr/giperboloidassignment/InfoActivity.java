package com.example.aleksandr.giperboloidassignment;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.example.aleksandr.giperboloidassignment.Items.PriceItem;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by aleksandr on 21.09.15.
 */

public class InfoActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_info);

        TextView Text_tv = (TextView)findViewById(R.id.text);
        TextView LongText_tv = (TextView)findViewById(R.id.longtext);
        TextView Prices_tv = (TextView)findViewById(R.id.prices);
        TextView Date_tv = (TextView)findViewById(R.id.date);
        TextView LocationText_tv = (TextView)findViewById(R.id.locationtext);

        String Text = getIntent().getStringExtra(MainActivity.TEXT);
        String LongText = getIntent().getStringExtra(MainActivity.LONG_TEXT);
        Long Date = getIntent().getLongExtra(MainActivity.DATE, 0);
        String LocationText = getIntent().getStringExtra(MainActivity.LOCATION_TEXT);

        ArrayList <PriceItem> priceList = getIntent().getParcelableArrayListExtra(MainActivity.PRICE_LIST);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Date);
        String date = DateFormat.format("dd-MM-yyyy HH:mm", calendar).toString();

        String priceText = "";
        for (int i =0; i < priceList.size(); i++){
            PriceItem priceItem = priceList.get(i);
            String description = priceItem.getDescription();
            int price  = priceItem.getPrice();
            priceText += "-"+description+"; Price: "+ price+"\n";
        }

        if (Text!= null){
            Text_tv.setText(Text);}
        if (LongText!= null){
            LongText_tv.setText(LongText);}
        if (priceList!= null){
            Prices_tv.setText(priceText);}
        if (date!= null){
            Date_tv.setText(date);}
        if (LocationText!= null){
            LocationText_tv.setText(LocationText);}
    }
}
