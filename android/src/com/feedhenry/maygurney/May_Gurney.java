/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.feedhenry.maygurney;

import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;


import org.mapsforge.android.maps.*;
import org.mapsforge.android.maps.overlay.*;
import org.mapsforge.core.GeoPoint;

import java.io.File;
import java.util.List;

public class May_Gurney extends MapActivity {


	final static int INSTALL_GOOGLE_PLAY_SERVICES = 0;

	protected void initialise(File mapFile) {
		final AdvanceMapView mapView = new AdvanceMapView(this);
		mapView.setClickable(true);
		mapView.setBuiltInZoomControls(true);
		mapView.setMapFile(mapFile);
		setContentView(mapView);
		final MarkerItemOverlay pinOverlay=new MarkerItemOverlay(this);
		mapView.getOverlays().add(pinOverlay);
		mapView.setOnLongpressListener(new AdvanceMapView.OnLongpressListener() {
			
			@Override
			public void onLongpress(MapView view, final GeoPoint geoPoint) {
				Log.d("Map","long press:"+geoPoint.latitudeE6+", "+geoPoint.longitudeE6);
				MarkerDialog dialog = new MarkerDialog();

				dialog.setMarkerDialogListener(new MarkerDialog.MarkerDialogListener() {
					@Override
					public void onDialogPositiveClick(DialogFragment dialog,
							String value) {
						Log.d("Map",value);
						
						pinOverlay.addMarker(geoPoint, value);
//						mapView.redraw();
//						mapView.redrawTiles();
						
					}

					@Override
					public void onDialogNegativeClick(DialogFragment dialog) {

					}
				});
				dialog.show(getFragmentManager(), "marker");
			}
		});
//		List<Overlay> overlays = mapView.getOverlays();
//
//		Log.d("overlay", String.valueOf(overlays.size()));

	}


	protected void mapSetup() {
		final File mapFile = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS,
				"ireland.map");
		if (mapFile.exists()) {
			Log.d("Map", "Map exists, start initialise");
			initialise(mapFile);
		} else {
			DownloadManager.Request r = new DownloadManager.Request(
					Uri.parse("http://download.mapsforge.org/maps/europe/ireland.map"));

			// This put the download in the same Download dir the browser uses
			r.setDestinationInExternalPublicDir(
					Environment.DIRECTORY_DOWNLOADS, "ireland.map");

			// Notify user when download is completed
			// (Seems to be available since Honeycomb only)
			r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			final BroadcastReceiver onComplete = new BroadcastReceiver() {

				@Override
				public void onReceive(Context ctx, Intent intent) {
					initialise(mapFile);
				}

			};

			registerReceiver(onComplete, new IntentFilter(
					DownloadManager.ACTION_DOWNLOAD_COMPLETE));

			// Start download
			DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
			dm.enqueue(r);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.main);
		mapSetup();
	}

	// Called after you install google play services
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INSTALL_GOOGLE_PLAY_SERVICES) {
			mapSetup();
		}
	}
}
