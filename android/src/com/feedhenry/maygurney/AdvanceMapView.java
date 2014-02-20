package com.feedhenry.maygurney;

import java.util.Timer;
import java.util.TimerTask;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;


import android.content.Context;
import android.view.MotionEvent;

public class AdvanceMapView extends MapView {
	public interface OnLongpressListener{
		public void onLongpress(MapView view, GeoPoint geoPoint);
	}
	private OnLongpressListener longpressListener;
	private Timer longpressTimer = new Timer();
	private GeoPoint lastMapCenter;
	public AdvanceMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean onTouchEvent(MotionEvent event){
		handleLongPress(event);
		return super.onTouchEvent(event);
	}
	private void handleLongPress(final MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN){
			longpressTimer=new Timer();
			longpressTimer.schedule(new TimerTask(){

				@Override
				public void run() {
					
					GeoPoint geoPoint=getProjection().fromPixels((int)event.getX(), (int)event.getY());
					longpressListener.onLongpress(AdvanceMapView.this, geoPoint);
				}
				
			}, 500);
			lastMapCenter = this.getMapPosition().getMapCenter();
		}
		if (event.getAction()==MotionEvent.ACTION_UP){
			longpressTimer.cancel();
		}
		if (event.getAction()==MotionEvent.ACTION_MOVE){
			if (!getMapPosition().getMapCenter().equals(lastMapCenter)){
				longpressTimer.cancel();
			}
			lastMapCenter = this.getMapPosition().getMapCenter();
		}
		if (event.getPointerCount() > 1) {
		    longpressTimer.cancel();
		}
		
	}
	public void setOnLongpressListener(AdvanceMapView.OnLongpressListener listener){
		longpressListener=listener;
	}

}
