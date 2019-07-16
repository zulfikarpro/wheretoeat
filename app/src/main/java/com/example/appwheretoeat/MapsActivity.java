package com.example.appwheretoeat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    int jd=0;
    String myLati="-6.179745";
    String myLongi="106.841933";
    String myPosisi="Kampus Nusa Mandiri Jakarta";

    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();
    JSONArray myJSON = null;
    String ip="";
    ArrayList<HashMap<String, String>> arrayList;
    private static final String TAG_SUKSES = "sukses";
    private static final String TAG_record = "record";

    String[]arid_restoran;
    String[]arnama_restoran;
    String[]arlokasi;
    String[]arlatitude;
    String[]arlongitude;
    String[]arbudget;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Intent get= this.getIntent();
        myLati=get.getStringExtra("myLati");
        myLongi=get.getStringExtra("myLongi");
        myPosisi=get.getStringExtra("myPosisi");


        arrayList = new ArrayList<HashMap<String, String>>();
        ip=jParser.getIP();

        new load().execute();




    }

    class load extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MapsActivity.this);
            pDialog.setMessage("Load data. Silahkan Tunggu...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(ip+"restoran/restoran_show.php", "GET", params);
            Log.d("show: ", json.toString());
            try {
                int sukses = json.getInt(TAG_SUKSES);
                if (sukses == 1) {
                    myJSON = json.getJSONArray(TAG_record);
                    jd=myJSON.length();

                    arid_restoran=new String[jd];
                    arnama_restoran=new String[jd];
                    arlokasi=new String[jd];
                    arlatitude=new String[jd];
                    arlongitude=new String[jd];
                    arbudget=new String[jd];


                    for (int i = 0; i < jd; i++) {
                        JSONObject c = myJSON.getJSONObject(i);

                        arid_restoran[i]= c.getString("id_restoran");
                        arnama_restoran[i]= c.getString("nama_restoran");
                        arlokasi[i]= c.getString("lokasi");
                        arlatitude[i]= c.getString("latitude");
                        arlongitude[i]= c.getString("longitude");
                        arbudget[i]= c.getString("budget");


                    }
                }
            }
            catch (JSONException e) {e.printStackTrace();}
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {

                    mapFragment.getMapAsync(MapsActivity.this);
                }
            });}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat=-6.179745;
        double lon=106.841933;
        try{
            lat=Double.parseDouble(myLati);
            lon=Double.parseDouble(myLongi);
        }
        catch(Exception e){  lat=-6.179745; lon=106.841933;}
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title("Posisi Anda").snippet(myPosisi));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lon),15));

        for(int i=0;i<jd;i++){
            lat=-6.179745;
            lon=106.841933;
            try{
                lat=Double.parseDouble(arlatitude[i]);
                lon=Double.parseDouble(arlongitude[i]);
            }
            catch(Exception e){  lat=-6.179745; lon=106.841933;}
            mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(arnama_restoran[i]).snippet(arlokasi[i]));

        }
    }

}
