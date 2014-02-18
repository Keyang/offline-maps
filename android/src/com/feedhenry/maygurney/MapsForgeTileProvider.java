package com.feedhenry.maygurney;

import java.io.File;
import java.io.InputStream;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.map.layer.renderer.DatabaseRenderer;
import org.mapsforge.map.layer.renderer.RendererJob;
import org.mapsforge.map.reader.MapDatabase;
import org.mapsforge.map.rendertheme.XmlRenderTheme;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;


public class MapsForgeTileProvider implements TileProvider {

    static private class RenderTheme implements XmlRenderTheme
    {	
    	
    	// overirding project mapsforge-map sub jar of mapsforge-map-android
    	
    	// Gets the pngs specified in os..xml from within the mapsforge....jar 
        @Override
        public String getRelativePathPrefix() {
            return "/osmarender/";
        }

        // get xml not from within jar but from assets directory
        // whole theme folder was copied into assets
        @Override
        public InputStream getRenderThemeAsStream()
        {
            return getClass().getResourceAsStream("/assets/theme/osmarender.xml");
        }
    }
    private final DatabaseRenderer mapGenerator;
    private final MapDatabase mapDatabase;
    private File mapFile;
    private BoundingBox bounds;
    private XmlRenderTheme theme;

    public MapsForgeTileProvider() {
        mapDatabase = new MapDatabase();
        mapGenerator = new DatabaseRenderer(mapDatabase,  GMapGraphicFactory.INSTANCE);
        theme = new RenderTheme();
    }


    public void setMapFile(File mapFile) {
        this.mapFile = mapFile;
        this.mapDatabase.openFile(mapFile);
        this.bounds = mapDatabase.getMapFileInfo().boundingBox;
        System.out.print(this.bounds.toString());
    }

    @Override
    public synchronized Tile getTile(int x, int y, int zoom) {
        if(tileOutOfBounds(x, y, zoom))
            return NO_TILE;

        final org.mapsforge.core.model.Tile tile = new org.mapsforge.core.model.Tile((long)x, (long)y, (byte)zoom);
        RendererJob rendererJob = new RendererJob(tile,
                mapFile,
                theme,
                1.5f);

        final GMapBitmap tileBitmap = (GMapBitmap)mapGenerator.executeJob(rendererJob);
        return new Tile(tileBitmap.getWidth(), tileBitmap.getHeight(), tileBitmap.toByteArray());
    }


    private boolean tileOutOfBounds(int tileX, int tileY, int zoom)
    {
        System.out.print("tileX: " + tileX + " tileY: " + tileY + " zoom: " + zoom);
        return false;
    }
}
