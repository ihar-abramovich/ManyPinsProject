package com.forhabr.manypins;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.Overlay;

public class ManyPinsProjectActivity extends MapActivity implements LocationListener, IOnZoomListener {

    private static final int         DEFAULT_ZOOM    = 15;

    private MyMapView                mapView         = null;
    private Drawable                 myCurrentMarker = null;
    private Drawable                 placeMarker     = null;

    private List<Overlay>            mapOverlays;

    private PlaceOverlay             placeOverlay;
    private MyCurrentLocationOverlay myCurrentLocationOverlay;

    double                           currentLatitude, currentLongitude;

    private MapController            mapController;

    private LocationManager          locationManager;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mapView = (MyMapView) findViewById(R.id.mapview);

        myCurrentMarker = this.getResources().getDrawable(R.drawable.my_pin_red);
        placeMarker = this.getResources().getDrawable(R.drawable.my_pin);
        
        myCurrentLocationOverlay = new MyCurrentLocationOverlay(myCurrentMarker, mapView);
        placeOverlay = new PlaceOverlay(placeMarker, mapView);
        mapOverlays = mapView.getOverlays();
        mapController = mapView.getController();
        
        mapView.setBuiltInZoomControls(true);
        mapView.setOnZoomListener(this);

    }


    private void animateToPlaceOnMap(final GeoPoint geopoint) {
        mapView.post(new Runnable() {

            @Override
            public void run() {
                mapView.invalidate();
                mapController.animateTo(geopoint);
                mapController.setZoom(DEFAULT_ZOOM);
            }
        });
    }

    private void setCurrentGeopoint(double myLatitude, double myLongitude) {
        
        currentLatitude = myLatitude;
        currentLongitude = myLongitude;

        final GeoPoint myCurrentGeoPoint = new GeoPoint((int) (myLatitude * 1E6), (int) (myLongitude * 1E6));

        MyOverlayItem myCurrentItem = new MyOverlayItem(myCurrentGeoPoint, "Current Location");
        myCurrentLocationOverlay.addOverlay(myCurrentItem);
        mapOverlays.add(myCurrentLocationOverlay);

        animateToPlaceOnMap(myCurrentGeoPoint);
    }


    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);

    }


    @Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 100, this);

    }
    
    private ArrayList<PlaceInfo> generatePlaces(){
    	
    	Random random = new Random();
    	
    	int x, y;
    	
    	ArrayList<PlaceInfo> places = new ArrayList<PlaceInfo>();
        PlaceInfo p;
        
        for(int i = 0; i < 100; i++){
        	x = random.nextInt(2000);
        	y = random.nextInt(2000);
        	p = new PlaceInfo();
            p.setLatitude(currentLatitude + x/100000f);
            p.setLongitude(currentLongitude - y/100000f);
            p.setName("Place Ü " + i);
            places.add(p);
        }
        
        return places;
    }

    private void displayPlacesOnMap() {

        ArrayList<PlaceInfo> places = generatePlaces();
        
        mapOverlays.remove(placeOverlay);

        GeoPoint point = null;
        MyOverlayItem overlayitem = null;
        placeOverlay.clear();

        for (PlaceInfo place : places) {
            point = new GeoPoint((int) (place.getLatitude() * 1E6), (int) (place.getLongitude() * 1E6));
            overlayitem = new MyOverlayItem(point, place.getName());
            placeOverlay.addOverlay(overlayitem);
        }

        placeOverlay.calculateItems();

        placeOverlay.doPopulate();

        if (placeOverlay.size() > 0) {
            mapOverlays.add(placeOverlay);
            mapView.postInvalidate();
        }

    }


    @Override
    public void onLocationChanged(Location location) {

        locationManager.removeUpdates(this);

        double myLatitude = location.getLatitude();
        double myLongitude = location.getLongitude();

        setCurrentGeopoint(myLatitude, myLongitude);

        displayPlacesOnMap();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public void onZoomChanged() {
		if (placeOverlay != null) {
			placeOverlay.calculateItems();
		}

	}

}
