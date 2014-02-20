package com.feedhenry.maygurney;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.ArrayItemizedOverlay;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.core.GeoPoint;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class MarkerItemOverlay extends ArrayItemizedOverlay {
	
	Context ctx;
	public MarkerItemOverlay(Context ctx) {
		super(ctx.getResources().getDrawable(R.drawable.map_pin));
		this.ctx=ctx;
	}
	public void addMarker(GeoPoint geo, String text){
//		OverlayItem itemOverlay=new OverlayItem(geo,text,text);
		TextMarker itemOverlay=new TextMarker(geo, text, ctx);
		this.addItem(itemOverlay);
		
	}
	public OverlayItem getItemByIndex(int index){
		return this.createItem(index);
	}
	@Override
	protected boolean onTap(int index) {
		TextMarker item=(TextMarker)this.getItemByIndex(index);
		Log.d("tap", ""+index);
		item.toggleText();
		this.populate();
		
		return super.onTap(index);
	}
	
	
//	public static Drawable boundCenterBottom(Drawable balloon) {
//		balloon.setBounds(balloon.getIntrinsicWidth() / -2, -balloon.getIntrinsicHeight()-45,
//				balloon.getIntrinsicWidth() / 2, -45);
//		return balloon;
//	}
//	
}
