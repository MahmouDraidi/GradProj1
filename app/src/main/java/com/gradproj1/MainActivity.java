package com.gradproj1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {


    private GoogleMap mMap;
    private boolean mPermissionDenied = false;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    Marker marker;
    user User;
    private FirebaseFirestore db;
    SharedPreferences SP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        final SharedPreferences.Editor SPE = SP.edit();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {


               /* mMap.addMarker(new MarkerOptions().position(new LatLng(35.123456,32.412544)).title("Marker in Sydney")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));*/


               /* List<String> d, dd, d1, dd1, dd2, d2;
                String[] acDrivers = {"Faleh1", "Asmar2", "Sweed7", "Zabadi6"};
                String[] nonAcDrivers = {"Malik3", "Firas", "Adly5"};
                d = Arrays.asList(acDrivers);
                dd = Arrays.asList(nonAcDrivers);

                String[] acDrivers1 = {"Hashem1", "Ahmed2", "Foud5"};
                String[] nonAcDrivers1 = {"Moayyad6", "Issa3", "AbHisham4"};
                d1 = Arrays.asList(acDrivers1);
                dd1 = Arrays.asList(nonAcDrivers1);

                String[] acDrivers2 = {"Driver1", "Driver4", "Driver5"};
                String[] nonAcDrivers2 = {"Driver2", "Driver3", "Driver6", "Driver7"};
                d2 = Arrays.asList(acDrivers2);
                dd2 = Arrays.asList(nonAcDrivers2);

                line L1=new line(new GeoPoint(32.263394,35.253545),new GeoPoint(32.264537,35.130564),"Nablus - BaytLid",d,dd);
                line L2=new line(new GeoPoint(32.221553,35.129784),new GeoPoint(32.312233,35.027102),"Tulkarm - BaytLid",d1,dd1);
                line L3=new line(new GeoPoint(32.221553,35.129784),new GeoPoint(32.403815,35.207732),"Nablus - Arraba",d2,dd2);
                */

               /*  GeoPoint GP1 = new GeoPoint(32.260257, 35.129973);
                GeoPoint GP2 = new GeoPoint(32.407120, 35.203915);
                GeoPoint GP3 = new GeoPoint(32.313411, 35.033419);
               user u1 = new user("+972595403748", "Mahmoud Draidi", "1234", GP1, "Nablus - Baytlid");
                user u2 = new user("+972595435114", "Mohammed Obaid", "1234", GP2, "Nablus - Arraba");
                user u3 = new user("+972597606430", "Kotada Draidi" , "1234", GP3, "Tulkarm - Baytlid");



                driver d1=new driver("Driver1",new GeoPoint(32.399210, 35.195875),"Driver1 d","147741","1234" , "Nablus - Arraba",true);
                driver d2=new driver("Driver2",new GeoPoint(32.417210, 35.195875),"Driver2 dd","987789","1234", "Nablus - Arraba",false);
                driver d3=new driver("Driver3",new GeoPoint(32.439210, 35.195575),"Driver3 ddm","147741","1234" , "Nablus - Arraba",false);
                driver d4=new driver("Driver4",new GeoPoint(32.409255, 35.195875),"Driver4 req","147741","1234" , "Nablus - Arraba",true);
                driver d5=new driver("Driver5",new GeoPoint(32.409610, 35.192275),"Driver5 qwerty","987789","1234", "Nablus - Arraba",true);
                driver d6=new driver("Driver6",new GeoPoint(32.405210, 35.191175),"Driver6 erwer","147741","1234" , "Nablus - Arraba",false);
                driver d7=new driver("Driver7",new GeoPoint(32.407210, 35.193875),"Driver7 rwer","147741","1234" , "Nablus - Arraba",false);

                db.collection("drivers").document(d1.getMobileNum()).set(d1);
                db.collection("drivers").document(d2.getMobileNum()).set(d2);
                db.collection("drivers").document(d3.getMobileNum()).set(d3);
                db.collection("drivers").document(d4.getMobileNum()).set(d4);
                db.collection("drivers").document(d5.getMobileNum()).set(d5);
                db.collection("drivers").document(d6.getMobileNum()).set(d6);
                db.collection("drivers").document(d7.getMobileNum()).set(d7);*/


                /*db.collection("lines").whereArrayContains("activeDrivers", "Faleh1").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                String sss = "";


                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    line L = documentSnapshot.toObject(line.class);
                                    sss += documentSnapshot.get("name") + "\n";
                                }
                                toastMessage(sss);



                                Snackbar.make(view, sss, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });*/


                // showDrivers();

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Intialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                //get the location name from latitude and longitude
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    List<Address> addresses =
                            geocoder.getFromLocation(latitude, longitude, 1);
                    String result = addresses.get(0).getLocality()+":";
                    result += addresses.get(0).getCountryName();
                    LatLng latLng = new LatLng(latitude, longitude);
                    if (marker != null){
                        marker.remove();
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
                    }
                    else{
                        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(result));
                        mMap.setMaxZoomPreference(20);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 21.0f));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
                // locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };*/
    }

    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();

    }


    @SuppressWarnings("MissingPermission")
    private Location getLastKnownLocation() {

        LocationManager mLocationManager;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_sign_out) {

            SharedPreferences SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
            SharedPreferences.Editor SPE = SP.edit();
            SPE.clear();
            SPE.apply();

            startActivity(new Intent(this, login.class));
            finish();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location ML = getLastKnownLocation();
        LatLng myLocation = (new LatLng(ML.getLatitude(), ML.getLongitude()));

        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(myLocation).title("Marker in Sydney")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));


        mMap.setMyLocationEnabled(true);


        Log.d("Your location is: ", ML.toString());

    }

    public void showDrivers() {


        DocumentReference m = db.collection("drivers").document("Asmar2");
        m.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String line = documentSnapshot.getString("line");

                            toastMessage(SP.getString("number", "") + " , Your line is: " + line);


                        }
                    }
                });
        /*DocumentReference mm=db.collection("lines").document(line);

        mm.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        L.add(documentSnapshot.get("drivers").toString());
                    }
                });*/
    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @SuppressWarnings("MissingPermission")
    public void location() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("YourLocation -------> :", location.toString());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
                // locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER,0,0,locationListener);

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
    }
}
