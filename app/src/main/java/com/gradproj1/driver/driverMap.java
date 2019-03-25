package com.gradproj1.driver;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.gradproj1.MyPassengers_List;
import com.gradproj1.R;
import com.gradproj1.Reservation;
import com.gradproj1.line.line;
import com.gradproj1.line.linesList;
import com.gradproj1.login;
import com.gradproj1.ReservationsList;
import com.gradproj1.user.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class driverMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, RoutingListener {


    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    driver Driver;
    String driverMobileNumber;
    FirebaseFirestore db;
    SharedPreferences SP;
    line myLine;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private Switch activatingSwitch;
    boolean isActive = false;
    static boolean garageView = true;
    ImageView garage_switch;
    Button navigateMyPassengers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_driver);
        activatingSwitch = (Switch) findViewById(R.id.acivateDriverSwitch);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        driverMobileNumber = SP.getString("number", "");
        polylines = new ArrayList<>();
        Driver = new driver();
        initDriver();
        isActive = getIsDriverActive();
        garage_switch = (ImageView) findViewById(R.id.garage_switch);
        navigateMyPassengers = findViewById(R.id.myPassengersButton);



        activatingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (activatingSwitch.isChecked()) {
                    updateIsActive(true);
                    activatingSwitchStatus(true);
                    isActive = true;
                    setDriversListener();
                    showDrivers();
                    showPassengers();
                    location();

                } else {
                    isActive = false;
                    updateIsActive(false);
                    activatingSwitchStatus(false);
                    mMap.clear();

                }
            }
        });
        garage_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isActive) switchBetweenGarages();

            }
        });
        /*navigateMyPassengers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location loc1 = new Location("");
                loc1.setLatitude(32.255132);
                loc1.setLongitude(35.185162);

                Location loc2 = new Location("");
                loc2.setLatitude(32.264543);
                loc2.setLongitude(35.130513);

                float distanceInMeters = loc1.distanceTo(loc2);
                toastMessage(String.valueOf(distanceInMeters));
            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View menuHeaderView = navigationView.getHeaderView(0);
        ((TextView) (menuHeaderView.findViewById(R.id.myNameView))).setText(Driver.getName());
        ((TextView) (menuHeaderView.findViewById(R.id.myLineView))).setText(Driver.getLine());


        //Intialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);


    }

    public void switchBetweenGarages() {
        if (garageView) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLine.getGarage1().getLatitude(), myLine.getGarage1().getLongitude()), 15f));
            garageView = false;
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLine.getGarage2().getLatitude(), myLine.getGarage2().getLongitude()), 15f));
            garageView = true;
        }
    }

    private void updateIsActive(boolean newStatus) {
        db.collection("drivers").document(driverMobileNumber).update("active", newStatus);
        if (newStatus) {
            db.collection("lines").document(Driver.getLine()).update("activeDrivers", FieldValue.arrayUnion(Driver.getMobileNumber()));
            db.collection("lines").document(Driver.getLine()).update("nonActiveDrivers", FieldValue.arrayRemove(Driver.getMobileNumber()));
        } else {
            db.collection("lines").document(Driver.getLine()).update("activeDrivers", FieldValue.arrayRemove(Driver.getMobileNumber()));
            db.collection("lines").document(Driver.getLine()).update("nonActiveDrivers", FieldValue.arrayUnion(Driver.getMobileNumber()));

        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void setDriversListener() {
        db.collection("lines").document(SP.getString("line", "")).get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            myLine = documentSnapshot.toObject(line.class);
                            List<String> active_drivers = myLine.getActiveDrivers();

                            for (String driverID : active_drivers) {
                                db.collection("drivers").document(driverID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                        if (e != null) {
                                            toastMessage("Failed to load drivers");
                                            return;
                                        }
                                        if (documentSnapshot.exists()) {
                                            if (isActive) {
                                                mMap.clear();
                                                showDrivers();
                                                showPassengers();
                                                drawMyLoacation(false);
                                            }
                                        }
                                    }
                                });
                            }
                        } else toastMessage("Failed to find drivers");
                    }
                });


    }

    public void activatingSwitchStatus(boolean b) {
        if (b) {
            activatingSwitch.setChecked(true);
            activatingSwitch.setText("On");
            activatingSwitch.setTextColor(Color.parseColor("#FF9800"));
        } else {
            activatingSwitch.setChecked(false);
            activatingSwitch.setText("off");
            activatingSwitch.setTextColor(Color.GRAY);
        }
    }


    public void toastMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show();


    }

    public boolean getIsDriverActive() {

        db.collection("drivers").document(Driver.getMobileNumber()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            if (documentSnapshot.toObject(driver.class).isActive()) {
                                isActive = true;
                                drawMyLoacation(true);
                                setDriversListener();
                                showDrivers();
                                location();
                                activatingSwitchStatus(true);


                            } else isActive = false;
                        }
                    }
                });
        return isActive;
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

        if (id == R.id.driversList) {
            startActivity(new Intent(this, DriversListActivity.class));

        } else if (id == R.id.driver_linesList) {
            startActivity(new Intent(this, linesList.class));

        } else if (id == R.id.driver_reservationList) {
            startActivity(new Intent(this, ReservationsList.class));


        } else if (id == R.id.my_passengers) {
            startActivity(new Intent(this, MyPassengers_List.class));


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

    public void drawMyLoacation(Boolean moveCam) {
        Location ML = getLastKnownLocation();
        LatLng myLocation = (new LatLng(ML.getLatitude(), ML.getLongitude()));

        // Add a marker in Sydney and move the camera

        mMap.addMarker(new MarkerOptions().position(myLocation).title("Me")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
        if (moveCam)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f));
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        setDriversListener();

        final LatLngBounds PALESTINE = new LatLngBounds(
                new LatLng(31.022610, 34.231112), new LatLng(33.245395, 35.681387));

// Constrain the camera target to the Adelaide bounds.
        mMap.setLatLngBoundsForCameraTarget(PALESTINE);


        //buildLineRoute();
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.marker_window, null);

                TextView titleTV = (TextView) v.findViewById(R.id.markerTitle);
                TextView descTV = (TextView) v.findViewById(R.id.markerDesc);

                titleTV.setText(marker.getTitle());
                descTV.setText(marker.getSnippet());

                return v;
            }
        });

        db.collection("reservations").whereEqualTo("line", Driver.getLine()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mMap.clear();
                showDrivers();
                showPassengers();
            }
        });

    }


    private void initDriver() {

        Driver.setMobileNumber(SP.getString("number", ""));
        Driver.setName(SP.getString("name", ""));
        Driver.setPIN(SP.getString("PIN", ""));
        Driver.setLine(SP.getString("line", ""));

    }

    public void showPassengers() {

        db.collection("reservations").whereEqualTo("line", Driver.getLine()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (final Reservation q : queryDocumentSnapshots.toObjects(Reservation.class)) {

                            db.collection("users").document(q.getUserMobileNumber()).get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                user u = documentSnapshot.toObject(user.class);
                                                GeoPoint g = u.getCurrentLocation();

                                                LatLng L = new LatLng(g.getLatitude(), g.getLongitude());

                                                mMap.addMarker(new MarkerOptions()
                                                        .title(u.getName())
                                                        .snippet("السائق: " + q.getDriverName() + "\n" + q.getPlaceDetails())
                                                        .position(L)
                                                        .icon(q.isNeedDriver() ?
                                                                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                                                                : BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                                                );
                                                        /*.icon(q.isNeedDriver()?
                                                                BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker_icon_green)
                                                                :BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker_icon)));*/

                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void showDrivers() {


        db.collection("lines").document(SP.getString("line", "")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {
                            myLine = documentSnapshot.toObject(line.class);
                            List<String> active_drivers = myLine.getActiveDrivers();

                            for (String driverID : active_drivers) {
                                db.collection("drivers").document(driverID).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                if (documentSnapshot.exists()) {
                                                    driver dr = documentSnapshot.toObject(driver.class);
                                                    GeoPoint GP = dr.getCurrentLocation();
                                                    LatLng loc = new LatLng(GP.getLatitude(), GP.getLongitude());
                                                    mMap
                                                            .addMarker(new MarkerOptions().position(loc).title(dr.getName())
                                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
                                                }
                                            }
                                        });
                                mMap.addMarker(new MarkerOptions().position(new LatLng(myLine.getGarage1().getLatitude(), myLine.getGarage1().getLongitude()))
                                        .title(myLine.getGarage1Discription()).zIndex(10)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                mMap.addMarker(new MarkerOptions().position(new LatLng(myLine.getGarage2().getLatitude(), myLine.getGarage2().getLongitude()))
                                        .title(myLine.getGarage2Discription()).zIndex(10)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                                );
                            }
                        } else toastMessage("Failed to find drivers");
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
                toastMessage(location.toString());
            }

            //TODO upload driver location
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, locationListener);


    }

    public void buildLineRoute() {
        toastMessage("Route building started");
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.BIKING)
                .withListener(this)
                .waypoints(new LatLng(32.222954, 35.256682), new LatLng(32.254565, 35.18574))
                .key("AIzaSyAbms9dwIG8L3EtGwC2q87jXbxiuELYRENs")
                .build();
        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getApplicationContext(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }
}

