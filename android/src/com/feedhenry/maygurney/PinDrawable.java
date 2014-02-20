package com.feedhenry.maygurney;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class PinDrawable extends BitmapDrawable {
	private int pinIcon;
	private Context ctx;
	private String pinText;
	private Boolean textOn=false;
	public PinDrawable(String pinText, Context t) {
		pinIcon=R.drawable.map_pin;
		ctx=t;
		this.pinText=pinText;
		Bitmap icon=this.getPinIcon();
		int width=icon.getWidth();
		int height=icon.getHeight();
		Log.d("img","w: "+width+" h:"+height);
//		MarkerItemOverlay.boundCenterBottom(this);
		this.setBounds(-width/2, -height/2, width/2, height/2);
//		this.setBounds(0, 0, width, height);
	}
	
	@Override
	public void draw(Canvas c) {
		Bitmap pinImg=this.getPinIcon();
		Rect rect=this.getBounds();
		Rect r1=new Rect(rect.left,rect.top,rect.right,rect.bottom);
//		r1.top-=45;
//		r1.bottom-=45;
        c.drawBitmap(pinImg, null,r1,null);
        Log.d("offset","left "+rect.left+" top"+rect.top);
		
		if (this.textOn){
			Paint paint=new Paint();
			paint.setColor(Color.BLACK);
	        paint.setTextSize(22f);
	        paint.setAntiAlias(true);
	        paint.setFakeBoldText(true);
	        paint.setStyle(Paint.Style.FILL);
	        paint.setTextAlign(Paint.Align.LEFT);
	        
	        c.drawText(pinText, r1.right+3, r1.top+25, paint);
		}

	}
	

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter arg0) {
		// TODO Auto-generated method stub

	}
	public void toggleText(){
		this.textOn=!this.textOn;
	}
	protected Bitmap getPinIcon(){
		return BitmapFactory.decodeResource(this.ctx.getResources(), pinIcon);
	}
	//Fix mapsforges bug where point project not count sb.
	protected int getStatusBarHeight() { 
	      int result = 0;
	      int resourceId = this.ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
	      if (resourceId > 0) {
	          result = this.ctx.getResources().getDimensionPixelSize(resourceId);
	      } 
	      return 0;
	} 
}
