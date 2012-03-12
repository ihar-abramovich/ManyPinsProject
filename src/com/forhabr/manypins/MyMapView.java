package com.forhabr.manypins;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.maps.MapView;

public class MyMapView extends MapView {
	int oldZoomLevel = -1;
	IOnZoomListener onZoomListener;

	public MyMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyMapView(Context context, String apiKey) {
		super(context, apiKey);
	}

	public MyMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnZoomListener(IOnZoomListener onZoomListener) {

		this.onZoomListener = onZoomListener;

	}

	@Override
	public void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int newZoom = this.getZoomLevel();

		if (newZoom != oldZoomLevel) {
			if (oldZoomLevel != -1 && onZoomListener != null) {
				onZoomListener.onZoomChanged();
			}

			oldZoomLevel = getZoomLevel();
		}
	}
}
