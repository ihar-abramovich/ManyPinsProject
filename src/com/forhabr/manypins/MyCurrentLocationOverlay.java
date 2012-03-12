package com.forhabr.manypins;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class MyCurrentLocationOverlay extends ItemizedOverlay<MyOverlayItem> {
	
	MyOverlayItem myOverlayItem;
	
	public MyCurrentLocationOverlay(Drawable defaultMarker, MapView mapView) {
		 super(boundCenterBottom(defaultMarker));
			 populate(); // Add this
	}

	public void addOverlay(MyOverlayItem overlay) {
	   myOverlayItem = overlay;
	    populate();
	}
	
	public MyOverlayItem getMyItem(){
		return myOverlayItem;
	}
	
	@Override
	protected MyOverlayItem createItem(int i) {
		return myOverlayItem;
	}
	
	@Override
	public int size() {
		if(myOverlayItem != null){
			return 1;
		} else {
			return 0;
		}
	}
	
	@Override
	public boolean onTap(GeoPoint p, MapView mapView) {
		Toast.makeText(mapView.getContext(), myOverlayItem.getName(), Toast.LENGTH_SHORT).show();
		return true;
	}

}
