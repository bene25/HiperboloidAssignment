package com.example.aleksandr.giperboloidassignment;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.aleksandr.giperboloidassignment.Items.Item;
import com.example.aleksandr.giperboloidassignment.Items.PriceItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aleksandr on 11.10.15.
 */
public class GetDataFragment extends Fragment {
    private Callbacks mCallbacks;
    private ArrayList<Item> mItemsArray;
    private MainActivity mActivity;
    public interface Callbacks {
        void onPostExecute(ArrayList<Item> items);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            mActivity = (MainActivity) context;
        }
        mCallbacks = mActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getData();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public void getData(){
        if (mActivity.isNetworkConnected()) {
            AsyncHttpUrlConnection asyncHttpUrlConnection = new AsyncHttpUrlConnection();
            asyncHttpUrlConnection.execute();
        } else {
            Toast.makeText(getActivity(), "There is no internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncHttpUrlConnection extends AsyncTask<String, Void, ArrayList<Item>> {

        final String Tasks = "tasks";
        final String Title = "title";
        final String Prices = "prices";
        final String Description = "description";
        final String Price = "price";
        final String Location = "location";
        final String Latitude = "lat";
        final String Longitude = "lon";
        final String Text = "text";
        final String LongText = "longText";
        final String LocationText = "locationText";
        final String Date = "date";

        @Override
        protected ArrayList<Item>  doInBackground(String... params) {
            try {
                URL Url = new URL("http://test.boloid.com:9000/tasks");
                mItemsArray = new ArrayList<Item>();
                HttpURLConnection httpURLConnection = (HttpURLConnection) Url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                String result = "";
                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                if(null!=inputStream){
                    inputStream.close();
                }

                try {
                    JSONObject jsonObjResp = new JSONObject(result);
                    JSONArray jsonArrayResp = jsonObjResp.getJSONArray(Tasks);
                    for(int i=0;i < jsonArrayResp.length(); i++){
                        JSONObject tasks = jsonArrayResp.getJSONObject(i);
                        String title = tasks.optString(Title);
                        JSONArray jsonArrayPrices = tasks.getJSONArray(Prices);
                        ArrayList <PriceItem>priceItem = new ArrayList<>();
                        for (int k=0;k < jsonArrayPrices.length(); k++){
                            JSONObject jsonObjectPrice = jsonArrayPrices.getJSONObject(k);
                            priceItem.add(PriceItem.builder()
                                            .setDescription(jsonObjectPrice.optString(Description))
                                            .setPrice(jsonObjectPrice.optInt(Price))
                                            .build()
                            );
                        }
                        JSONObject coordinates = tasks.getJSONObject(Location);
                        Double latitude = coordinates.getDouble(Latitude);
                        Double longitude = coordinates.getDouble(Longitude);
                        String text = tasks.optString(Text);
                        String longText = tasks.optString(LongText);
                        String locationText = tasks.optString(LocationText);
                        Long date = tasks.optLong(Date);
                        mItemsArray.add(Item.builder()
                                .setTitle(title)
                                .setLatitude(latitude)
                                .setLongitude(longitude)
                                .setText(text)
                                .setLongText(longText)
                                .setLocationText(locationText)
                                .setDate(date)
                                .setPriceList(priceItem)
                                .build());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return mItemsArray;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Item> items) {
            super.onPostExecute(items);
            if (mCallbacks != null) {
                mCallbacks.onPostExecute(items);
            }
        }
    }

    public ArrayList<Item>  getItemsArray() {
        return mItemsArray;
    }

}
