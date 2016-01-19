package com.example.aleksandr.giperboloidassignment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.aleksandr.giperboloidassignment.Items.Item;

import java.util.ArrayList;
import java.util.List;

import ru.yandex.yandexmapkit.MapController;
import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.overlay.Overlay;
import ru.yandex.yandexmapkit.overlay.OverlayItem;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.overlay.balloon.OnBalloonListener;
import ru.yandex.yandexmapkit.utils.GeoPoint;

/**
 * Created by aleksandr on 21.09.15.
 */
public class MainActivity extends AppCompatActivity implements OnBalloonListener, GetDataFragment.Callbacks {
    private MapController mMapController;
    private OverlayManager mOverlayManager;
    private MapView mMapView;
    private Overlay mOverlay = null;
    private ArrayList<Item> mItems = null;
    private int mCount = 0;
    private GetDataFragment mGetDataFragment;

    public static final String GET_DATA_FRAGMENT = "GetDataFragment";
    public static final String MAP_COORDINATES_LAT = "MapCoordinatesLat";
    public static final String MAP_COORDINATES_LON = "MapCoordinatesLon";
    public static final String MAP_ZOOM = "MapZoom";
    public static final String CURRENT_BALLOON_ID = "CurrentBalloonId";

    public static final String TEXT = "Text";
    public static final String LONG_TEXT = "LongText";
    public static final String DATE = "Date";
    public static final String LOCATION_TEXT = "LocationText";
    public static final String PRICE_LIST = "PriceList";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button show_all = (Button) findViewById(R.id.showAll);
        Button refresh = (Button) findViewById(R.id.refresh);
        Button show_next = (Button) findViewById(R.id.showNext);
        if (isNetworkConnected()){
            show_all.setEnabled(true);
            refresh.setEnabled(true);
            show_next.setEnabled(true);
        } else {
            show_all.setEnabled(false);
            refresh.setEnabled(false);
            show_next.setEnabled(false);
        }

        mMapView = (MapView) findViewById(R.id.map);
        mMapView.showZoomButtons(true);
        mMapController = mMapView.getMapController();
        mOverlayManager = mMapController.getOverlayManager();
        mOverlayManager.getMyLocation().setEnabled(false);
        if (savedInstanceState != null){
            mMapController.setPositionAnimationTo(new GeoPoint
                    (savedInstanceState.getDouble(MAP_COORDINATES_LAT), savedInstanceState.getDouble(MAP_COORDINATES_LON)),
                    savedInstanceState.getFloat(MAP_ZOOM));
            mCount = savedInstanceState.getInt(CURRENT_BALLOON_ID);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mGetDataFragment = (GetDataFragment) fragmentManager.findFragmentByTag(GET_DATA_FRAGMENT);
        if (mGetDataFragment == null){
            mGetDataFragment = new GetDataFragment();
            fragmentManager.beginTransaction().add(mGetDataFragment, GET_DATA_FRAGMENT).commit();

        } else if (isNetworkConnected()){
                addMapBalloon(mGetDataFragment.getItemsArray());
        }

        //Show objects on map
        show_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setZoomSpan(mOverlay.getOverlayItems().size());
            }
        });
        //Refresh balloons
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOverlay.getOverlayItems().size()> 0){
                    mOverlay.clearOverlayItems();
                }
                mGetDataFragment.getData();
            }
        });
        //Show next balloon
        show_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCount >= mItems.size()) {
                    mCount = 0;
                }
                Item item = mItems.get(mCount);
                mMapController.setZoomToSpan(item.getLatitude(), item.getLongitude());
                mMapController.setPositionAnimationTo(new GeoPoint(item.getLatitude(), item.getLongitude()), 12);
                mCount++;
            }
        });
    }


    @Override
    public void onBalloonViewClick(BalloonItem balloonItem, View view) {
        Intent intent = new Intent(this, InfoActivity.class);
        for(int i=0;i < mItems.size(); i++) {
            Item item = mItems.get(i);
            if (item.getBalloonItem() == balloonItem)
            {
                intent.putExtra(TEXT, item.getText());
                intent.putExtra(LONG_TEXT, item.getLongText());
                intent.putExtra(DATE, item.getDate());
                intent.putExtra(LOCATION_TEXT, item.getLocationText());
                intent.putExtra(PRICE_LIST, item.getPriceList());
            }
        }
        startActivity(intent);
    }

    @Override
    public void onBalloonShow(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonHide(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationStart(BalloonItem balloonItem) {

    }

    @Override
    public void onBalloonAnimationEnd(BalloonItem balloonItem) {

    }

    private void setZoomSpan(int count){
        List<OverlayItem> list = mOverlay.getOverlayItems();
        double maxLat, minLat, maxLon, minLon;
        maxLat = maxLon = Double.MIN_VALUE;
        minLat = minLon = Double.MAX_VALUE;
        for (int i = 0; i < count; i++){
            GeoPoint geoPoint = list.get(i).getGeoPoint();
            double lat = geoPoint.getLat();
            double lon = geoPoint.getLon();

            maxLat = Math.max(lat, maxLat);
            minLat = Math.min(lat, minLat);
            maxLon = Math.max(lon, maxLon);
            minLon = Math.min(lon, minLon);
        }
        mMapController.setZoomToSpan(maxLat - minLat, maxLon - minLon);
        mMapController.setPositionAnimationTo(new GeoPoint((maxLat + minLat) / 2, (maxLon + minLon) / 2));
    }


     @Override
    public void onPostExecute(ArrayList<Item> items) {
        addMapBalloon(items);
    }

    //Add map balloon
    private void addMapBalloon (ArrayList<Item> items){
        mItems = items;
        mOverlay = new Overlay(mMapController);
        for(int i=0;i < items.size(); i++){
            Item item = items.get(i);
            final OverlayItem overlayItem = new OverlayItem(new GeoPoint(item.getLatitude(),
                    item.getLongitude()), ContextCompat.getDrawable(this, R.drawable.a));
            BalloonItem balloonitem = new BalloonItem(this,overlayItem.getGeoPoint());
            balloonitem.setOnBalloonListener(this);
            balloonitem.setText(item.getTitle());

            item.setBalloonItem(balloonitem);
            overlayItem.setBalloonItem(balloonitem);
            mOverlay.addOverlayItem(overlayItem);
        }
        mOverlayManager.addOverlay(mOverlay);
        mMapController.notifyRepaint();
    }
    //Check internet connection
    public boolean isNetworkConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null){
            return false;
        }
        if (!networkInfo.isConnected()){
            return false;
        }
        if (!networkInfo.isAvailable()){
            return false;
        }
        return true;
    }

    //Save Lat, Lng and zoom
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble(MAP_COORDINATES_LAT, mMapController.getMapCenter().getLat());
        outState.putDouble(MAP_COORDINATES_LON, mMapController.getMapCenter().getLon());
        outState.putFloat(MAP_ZOOM, mMapController.getZoomCurrent());
        outState.putInt(CURRENT_BALLOON_ID, mCount);
    }

}

