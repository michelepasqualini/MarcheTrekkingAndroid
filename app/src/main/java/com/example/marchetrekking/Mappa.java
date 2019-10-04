package com.example.marchetrekking;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.GraphHopperRoadManager;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.bonuspack.routing.RoadNode;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class Mappa extends AppCompatActivity {

    RoadManager roadManager;
    private final int PERMISSION = 1;
    private MapView mMapView;
    private IMapController mMapController;
    private String mappa;
    private List<Double> lat = new ArrayList<>();
    private List<Double> lon= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mappa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarMap) ;
        toolbar.setTitle("Mappa");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
        }

        mappa = getIntent().getExtras().getString("mappa");
        String[] splitter = mappa.split(",");
        for(int i = 0; i  < splitter.length; i++){
            if(i % 2 == 0){
                Double d=Double.parseDouble(splitter[i]);
                lat.add(d) ;
            }
            else{
                lon.add(Double.parseDouble(splitter[i])) ;
            }
        }

        setup_map(lat.get(0), lon.get(0));

        List<GeoPoint> geoPoints = new ArrayList<>();
        for(int i = 0; i  < lat.size(); i++){
            geoPoints.add(new GeoPoint(lat.get(i), lon.get(i)));
        }
        Polyline polyline = new Polyline();
        polyline.setPoints(geoPoints);
        mMapView.getOverlays().add(polyline);

        //start
        Marker startMarker = new Marker(mMapView);
        startMarker.setPosition(geoPoints.get(0));
        mMapView.getOverlays().add(startMarker);
        startMarker.setTitle("Start");
        startMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        Drawable icon = getResources().getDrawable(R.drawable.pedone);
        startMarker.setImage(icon);

        //end
        Marker endMarker = new Marker(mMapView);
        endMarker.setPosition(geoPoints.get(geoPoints.size()-1));
        mMapView.getOverlays().add(endMarker);
        endMarker.setTitle("Fine");
        endMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
        Drawable icon2 = getResources().getDrawable(R.drawable.pedone);
        endMarker.setImage(icon2);

        insert_scale();

    }

    private void setup_map(double lat, double lon){
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);
        mMapView.setBuiltInZoomControls(false);
        mMapController = mMapView.getController();
        mMapController.setZoom(16);

        //dove la mappa viene centrata
        GeoPoint gPt = new GeoPoint(lat,lon);
        mMapController.setCenter(gPt);

    }

    //private void setup_path(double startLat, double startLon, double endLat, double endLon){
        //OSRMRoadManager(this);
        //roadManager = new GraphHopperRoadManager("8b26f56b-56a0-4ffd-93f0-744fb4b2f75a",true);
        //ArrayList<GeoPoint> waypoints = new ArrayList<>();
        //GeoPoint startPoint = new GeoPoint(startLat, startLon);
        //waypoints.add(startPoint);
        //GeoPoint endPoint = new GeoPoint(endLat,endLon);
        //waypoints.add(endPoint);

        //Marker startMarker = new Marker(mMapView);
        //startMarker.setPosition(startPoint);
        //startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //mMapView.getOverlays().add(startMarker);

        //Marker endMarker = new Marker(mMapView);
        //endMarker.setPosition(endPoint);
        //endMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        //mMapView.getOverlays().add(endMarker);


        //roadManager.addRequestOption("vehicle=foot");
        //Road road = roadManager.getRoad(waypoints);
        //roadManager.addRequestOption("optimize=true");
        //Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        //mMapView.getOverlays().add(roadOverlay);
        //mMapView.invalidate();
        //mMapView.setMaxZoomLevel(1000.0);


        /*for(int i = 0; i < road.mNodes.size(); i++){
            RoadNode node = road.mNodes.get(i);
            Marker prova = new Marker(mMapView);
            prova.setTitle("Step" + i);
            prova.setIcon(getResources().getDrawable(R.drawable.marker_default));
            prova.setPosition(node.mLocation);
            mMapView.getOverlays().add(prova);
            prova.setSnippet(node.mInstructions);
            prova.setSubDescription(Road.getLengthDurationText(this, node.mLength, node.mDuration));
            Drawable icon = getResources().getDrawable(R.drawable.pedone);
            prova.setImage(icon);
        }*/





    private void insert_scale(){
        ScaleBarOverlay myScaleBarOverlay = new ScaleBarOverlay(mMapView);
        mMapView.getOverlays().add(myScaleBarOverlay);
    }


}


