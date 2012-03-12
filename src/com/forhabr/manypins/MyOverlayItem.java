package com.forhabr.manypins;

import java.util.ArrayList;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class MyOverlayItem extends OverlayItem {

	private String name;

	private ArrayList<MyOverlayItem> list = new ArrayList<MyOverlayItem>();

	public MyOverlayItem(GeoPoint point, String name) {
		super(point, "", "");
		this.name = name;
	}

	public String getName() {
		if (list.size() > 0) {
			return "There are " + (list.size() + 1) + " places.";
		} else {
			return name;
		}

	}

	public void addList(MyOverlayItem item) {
		list.add(item);
	}

	public ArrayList<MyOverlayItem> getList() {
		return list;
	}

}
