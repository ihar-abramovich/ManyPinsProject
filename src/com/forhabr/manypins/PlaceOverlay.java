package com.forhabr.manypins;

import java.util.ArrayList;

import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;

public class PlaceOverlay extends ItemizedOverlay<MyOverlayItem> {

	private static final int KOEFF = 20;
	private ArrayList<MyOverlayItem> myOverlaysAll = new ArrayList<MyOverlayItem>();
	private ArrayList<MyOverlayItem> myOverlays = new ArrayList<MyOverlayItem>();
	private MapView mapView;

	public PlaceOverlay(Drawable defaultMarker, MapView mapView) {
		super(boundCenterBottom(defaultMarker));
		this.mapView = mapView;
		populate();
	}

	public void addOverlay(MyOverlayItem overlay) {
		myOverlaysAll.add(overlay);
		myOverlays.add(overlay);
		

	}

	public void doPopulate() {
		populate();
		setLastFocusedIndex(-1);

	}

	@Override
	protected MyOverlayItem createItem(int i) {
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		return myOverlays.size();
	}

	private boolean isImposition(MyOverlayItem item1, MyOverlayItem item2) {

		int latspan = mapView.getLatitudeSpan();
		int delta = latspan / KOEFF;

		int dx = item1.getPoint().getLatitudeE6() - item2.getPoint().getLatitudeE6();
		int dy = item1.getPoint().getLongitudeE6() - item2.getPoint().getLongitudeE6();

		double dist = Math.sqrt(dx * dx + dy * dy);

		if (dist < delta) {
			return true;
		} else {
			return false;
		}

	}

	public void clear() {
		myOverlaysAll.clear();
		myOverlays.clear();
	}


	public void calculateItems() {
		
		myOverlaysClear();

		boolean isImposition;

		for (MyOverlayItem itemFromAll : myOverlaysAll) {
			isImposition = false;
			for (MyOverlayItem item : myOverlays) {
				if (itemFromAll == item) {
					isImposition = true;
					break;

				}
				if (isImposition(itemFromAll, item)) {
					item.addList(itemFromAll);
					isImposition = true;
					break;
				}
			}

			if (!isImposition) {
				myOverlays.add(itemFromAll);
			}
		}

		doPopulate();

	}

	private void myOverlaysClear() {
		for (MyOverlayItem item : myOverlaysAll) {
			item.getList().clear();

		}
		myOverlays.clear();

	}
	
	@Override
	protected boolean onTap(int index) {
		Toast.makeText(mapView.getContext(), myOverlays.get(index).getName(), Toast.LENGTH_SHORT).show();
		return true;
	}

}
