package com.hippo.flash.meetutu;

import android.app.ActionBar;
import com.hippo.flash.meetutu.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

    import android.support.v4.app.FragmentActivity;
    import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Config;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
    import com.google.android.gms.maps.GoogleMap;
    import com.google.android.gms.maps.OnMapReadyCallback;
    import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MapShowActivity extends AppCompatActivity implements OnMapReadyCallback {
private double lat,lon;
    private String name,email,mobile,tut,cat,adr;
    private String compT="T";
    private String compL="L";
    public GpsLocation gps;
    double latitude;
   double longitude;
    ConnectivityManager connectivity;
        private GoogleMap mMap;

    private String jsonResult;
    private String url = "http://casualwebsite.6te.net/meetutu/retrieve.php";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (isOnline()) {
                setContentView(R.layout.activity_maps);


                accessWebService();

                android.support.v7.app.ActionBar actionBar = getSupportActionBar();

                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#E91E63")));
                //getSupportActionBar().setIcon(R.drawable.logo);

                int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

                    int requestCode = 10;
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                    dialog.show();

                } else {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);


                    // Getting GoogleMap object from the fragment
                    mMap = mapFragment.getMap();

                    // Enabling MyLocation Layer of Google Map
                    mMap.setMyLocationEnabled(true);
                }
            }

            else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapShowActivity.this);

                // Setting Dialog Title
                alertDialog.setTitle("Mobile network");

                // Setting Dialog Message
                alertDialog.setMessage("GPS OR Network is not enabled.Give complete access to get accuracy Do you want to go to settings menu?");

                // On pressing Settings button, to display settings pages
                alertDialog.setPositiveButton("Settings",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
                                startActivity(intent);

                            }
                        });

                // Showing Alert Message
                alertDialog.show();
            }

        }




    public boolean isOnline() {
        connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null)
            return false;

        final NetworkInfo activeNetwork = connectivity.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED)
            return true;

        return false;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
         case R.id.action_signup:
             startActivity(new Intent("com.hippo.flash.meetutu.MainActivity"));

                break;

            default:
                break;
        }

        return true;
    }



        @Override
        public void onMapReady(GoogleMap googleMap) {
           // mMap = googleMap;
          // LatLng sydney = new LatLng(lat,lon);
          //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
         //   mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }


    // Async Task to access the web
    private class JsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                Toast.makeText(getApplicationContext(),
                        "Error..." + e.toString(), Toast.LENGTH_LONG).show();
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            if(isOnline())
            ListDrwaer();
        }
    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
// passes values for the urls string array
        task.execute(new String[]{url});
    }

    // build hash set for list view
    public void ListDrwaer() {
        try {

                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("result");

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    lat = Double.parseDouble(jsonChildNode.optString("lat"));
                    lon = Double.parseDouble(jsonChildNode.optString("lon"));
                    name = jsonChildNode.optString("name");
                    email = jsonChildNode.optString("email");
                    mobile = jsonChildNode.optString("mobile");
                    tut = jsonChildNode.optString("tut");
                    cat = jsonChildNode.optString("cat");
                    adr = jsonChildNode.optString("adr");


                    // Add a marker in Sydney and move the camera
                    LatLng loc = new LatLng(lat, lon);
                    if (mMap != null) {


                        BitmapDescriptor tutoricon = BitmapDescriptorFactory.fromResource(R.drawable.tutor);
                        BitmapDescriptor learnericon = BitmapDescriptorFactory.fromResource(R.drawable.learner);


                        if (cat.equals(compT))
                            mMap.addMarker(new MarkerOptions().position(loc).
                                    title("Tutor name:" + name)
                                    .snippet(",\nTutorial:" + tut + "\nAddress:" + adr + ",\nMobile:" + mobile + ",\nEmail:" + email + "Click to see details")
                                    .icon(tutoricon));

                        if (cat.equals(compL))
                            mMap.addMarker(new MarkerOptions().position(loc).
                                    title("Learner name:" + name)
                                    .snippet(",\nTutorial:" + tut + "\nAddress:" + adr + ",\nMobile:" + mobile + ",\nEmail:" + email + "\nClick to see details")
                                    .icon(learnericon));


                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {

                                LinearLayout info = new LinearLayout(MapShowActivity.this);
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(MapShowActivity.this);
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(MapShowActivity.this);
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });


                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                Intent intent = new Intent("com.hippo.flash.meetutu.Info_Activity");
                                Bundle extras = new Bundle();
                                extras.putString("name", name);
                                extras.putString("email", email);
                                extras.putString("mobile", mobile);
                                extras.putString("tut", tut);
                                extras.putString("adr", adr);
                                extras.putString("cat", cat);


                                intent.putExtras(extras);
                                startActivity(intent);

                            }
                        });


                        gps = new GpsLocation(MapShowActivity.this);

                        if (gps.canGetLocation()) {

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
                            if (latitude == 0.0 || longitude == 0.0) {
                                Toast.makeText(getApplicationContext(), "Enable Gps and Google Locationservice to get accuracy", Toast.LENGTH_LONG).show();
                            }
                            // String mylat = String.valueOf(latitude);
                            //  String mylon = String.valueOf(longitude);
                            //latitude = Double.parseDouble(mylat);
                            //longitude=Double.parseDouble(mylon);

                            //String adr=null;
                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }

                        LatLng myloc = new LatLng(latitude, longitude);


                        float zoomLevel = 10.0f; //This goes up to 21
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, zoomLevel));


                    }

                }
            }catch(JSONException e){
                Toast.makeText(getApplicationContext(), "Error" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        catch(NullPointerException n){
            Toast.makeText(getApplicationContext(), "Set your internet connection first",
                    Toast.LENGTH_SHORT).show();



            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MapShowActivity.this);

            // Setting Dialog Title
            alertDialog.setTitle("Mobile network");

            // Setting Dialog Message
            alertDialog.setMessage("GPS OR Network is not enabled.Give complete access to get accuracy Do you want to go to settings menu?");

            // On pressing Settings button, to display settings pages
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                           startActivity(new Intent(Settings.ACTION_SETTINGS));

                        }
                    });

            // Showing Alert Message
            alertDialog.show();



        }


        }



    private String getAddress(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\t");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("loction address", "Canont get Address!");
        }
        return strAdd;
    }










}







