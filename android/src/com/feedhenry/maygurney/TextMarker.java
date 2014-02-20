package com.feedhenry.maygurney;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.overlay.ItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class TextMarker extends OverlayItem {
	
	private Context ctx;
	private PinDrawable marker;
	public TextMarker(GeoPoint geoPoint, String text, Context ctx) {
		super(geoPoint, text,text);
		this.ctx=ctx;
		
//		Drawable d=ctx.getResources().getDrawable(R.drawable.icon);
		marker=new PinDrawable(text, ctx);
//		d=MarkerItemOverlay.boundCenterBottom(d);
		this.setMarker(marker);
	}
	
	public void toggleText(){
		marker.toggleText();
		
	}
	
	

	
}
