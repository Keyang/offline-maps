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

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;

import org.apache.cordova.api.LOG;

import java.io.File;


public class May_Gurney extends Activity {

    GoogleMap map;

    final static int INSTALL_GOOGLE_PLAY_SERVICES = 0;


    protected void initialise(File mapFile) {
        MapsForgeTileProvider provider = new MapsForgeTileProvider();
        provider.setMapFile(mapFile);

        // add custom tile provider.
        map.addTileOverlay(new TileOverlayOptions().tileProvider(provider));

        // listen for a long click to add markers.
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                MarkerDialog dialog = new MarkerDialog();

                dialog.setMarkerDialogListener(new MarkerDialog.MarkerDialogListener() {
                    @Override
                    public void onDialogPositiveClick(DialogFragment dialog, String value) {
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(value)
                                .draggable(true));
                    }

                    @Override
                    public void onDialogNegativeClick(DialogFragment dialog) {

                    }
                });
                dialog.show(getFragmentManager(), "marker");
            }
        });

    }

    protected void mapSetup() {
        // see fragment in mail.xml + android Maps API docs
    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        int servicesAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if(servicesAvailable == ConnectionResult.SUCCESS) {

            Log.d("things", Environment.getExternalStorageDirectory().getAbsolutePath());

            // disable google maps tiles.
            map.setMapType(GoogleMap.MAP_TYPE_NONE);
            map.setMyLocationEnabled(true);

            final File mapFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_DOWNLOADS, "ireland.map");

            if(mapFile.exists()) {
                initialise(mapFile);
            }
            else {
                DownloadManager.Request r = new DownloadManager.Request(Uri.parse("http://download.mapsforge.org/maps/europe/ireland.map"));

                // This put the download in the same Download dir the browser uses
                r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ireland.map");

                // Notify user when download is completed
                // (Seems to be available since Honeycomb only)
                r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                final BroadcastReceiver onComplete = new BroadcastReceiver() {
                    
                	// TODO: this will fire for all downlaods - may need to distingush between downloads
                	public void onReceive(Context ctx, Intent intent) {
                        initialise(mapFile);
                    }
                };

                registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

                // Start download
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(r);
            }
        }
        else {
        	// You don't have GPS installed so this will prompt you to do that.
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(servicesAvailable, this, INSTALL_GOOGLE_PLAY_SERVICES);
            errorDialog.setCancelable(false);
            errorDialog.show();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mapSetup();
    }
    
    // Called after you install google play services 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == INSTALL_GOOGLE_PLAY_SERVICES) {
            mapSetup();
        }
    }
}
