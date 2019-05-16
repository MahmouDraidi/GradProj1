package com.gradproj1.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.gradproj1.Profile;
import com.gradproj1.R;
import com.gradproj1.Reservation;
import com.gradproj1.driver.DriversListActivity;
import com.gradproj1.driver.driver;
import com.gradproj1.line.line;
import com.gradproj1.line.linesList;
import com.gradproj1.login;
import com.gradproj1.reservationPopup;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class userMap extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, RoutingListener {


    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private user User;
    private FirebaseFirestore db;
    private SharedPreferences SP;
    private line myLine;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private final List<String> driversNames = new ArrayList<String>();
    private Button reqButton;
    Reservation res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        reqButton = findViewById(R.id.requestButton);
        db = FirebaseFirestore.getInstance();
        SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
        final SharedPreferences.Editor SPE = SP.edit();
        polylines = new ArrayList<>();
        initUser();
        fragmentManager = getSupportFragmentManager();


        reqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean userHasReservation = false;

                db.collection("reservations").document(User.getMobileNumber()).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    res = documentSnapshot.toObject(Reservation.class);
                                    if (res.getReservationDriver().equals("none")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(userMap.this);
                                        builder.setMessage("لقد قمت بالطلب مسبقا,هل تود حذف الطلب؟");
                                        builder.setPositiveButton("حذف الطلب", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                db.collection("reservations").document(User.getMobileNumber()).delete();
                                                db.collection("drivers").document(res.getReservationDriver()).update("myPassengers", FieldValue.arrayRemove(User.getMobileNumber()));
                                                updatePassengersNum(res.getReservationDriver(), res.getReservationSize());

                                            }
                                        })
                                                .setNegativeButton("إلغاء", null);
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(userMap.this);
                                        builder.setMessage("أنت لدى حجز مع " + res.getDriverName() + " هل تريد الإلغاء؟");
                                        builder.setPositiveButton("الغاء الحجز", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                db.collection("reservations").document(User.getMobileNumber()).update("cancelRes", true);

                                            }
                                        })
                                                .setNegativeButton("تراجع", null);
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }


                                } else showPopup();
                            }
                        });
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View menuHeaderView = navigationView.getHeaderView(0);
        ((TextView) (menuHeaderView.findViewById(R.id.myNameView))).setText(User.getName());
        ((TextView) (menuHeaderView.findViewById(R.id.myLineView))).setText(User.getLine());


        //Intialize map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        location();


    }

    public void updatePassengersNum(String resDriver, final int cancelledResSize) {
        db.collection("drivers").document(resDriver).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            int passengersNum = documentSnapshot.toObject(driver.class).getCurrentPassengersNum();
                            db.collection("drivers").document(res.getReservationDriver()).update("currentPassengersNum", passengersNum - cancelledResSize);

                        }
                    }
                });
    }

    private void initUser() {
        User = new user();
        User.setMobileNumber(SP.getString("number", ""));
        User.setName(SP.getString("name", ""));
        User.setLine(SP.getString("line", ""));
        User.setPIN(SP.getString("PIN", ""));

    }

    public void showPopup() {

        Intent i = new Intent(this, reservationPopup.class);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();

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
                                            mMap.clear();
                                            showDrivers();
                                            drawMyLoacation(false);
                                        }
                                    }
                                });
                            }
                        } else toastMessage("Failed to find drivers");
                    }
                });
        //getDriversNames();


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

    public List<String> getDriversNames() {

        db.collection("lines").document(SP.getString("line", "")).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            line Line = documentSnapshot.toObject(line.class);
                            List<String> dID = Line.getActiveDrivers();
                            dID.addAll(Line.getNonActiveDrivers());
                            for (String S : dID) {
                                db.collection("drivers").document(S).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                driversNames.add(documentSnapshot.toObject(driver.class).getName());
                                                //toastMessage(documentSnapshot.toObject(driver.class).getName());
                                            }
                                        });
                            }
                        }
                    }
                });
        toastMessage("Retreived");
        return driversNames;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (fragment != null) {
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragment = null;
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

        if (id == R.id.nav_drivers) {
            // Handle the camera action
            startActivity(new Intent(this, DriversListActivity.class));

        } else if (id == R.id.nav_prof) {
            Intent intent = new Intent(this, Profile.class);

            intent.putExtra("path", SP.getString("number", ""));
            intent.putExtra("type", "users");
            intent.putExtra("isMe", true);
            startActivity(intent);

        } else if (id == R.id.nav_lines) {
            Intent linesIntent = new Intent(this, linesList.class);

            startActivity(linesIntent);



        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_sign_out) {

            SharedPreferences SP = getSharedPreferences("mobile_number", MODE_PRIVATE);
            SP.edit().clear().apply();
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
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_user_marker_icon)));
        if (moveCam)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12f));


    }


    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);

        drawMyLoacation(true);
        //buildLineRoute();

        final LatLngBounds PALESTINE = new LatLngBounds(
                new LatLng(31.022610, 34.231112), new LatLng(33.245395, 35.681387));
        mMap.setLatLngBoundsForCameraTarget(PALESTINE);


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

                                                    setDriverMarker(loc, dr.getName(), "عدد الركاب: " + String.valueOf(dr.getCurrentPassengersNum()));

                                                }
                                            }
                                        });
                            }
                        } else toastMessage("Failed to find drivers");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(myLine.getGarage1().getLatitude(), myLine.getGarage1().getLongitude()))
                                .title(myLine.getGarage1Discription()).zIndex(10)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(myLine.getGarage2().getLatitude(), myLine.getGarage2().getLongitude()))
                                .title(myLine.getGarage2Discription()).zIndex(10)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))

                        );
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

    private void setDriverMarker(LatLng loc, String title, String snipp) {
        mMap.addMarker(new MarkerOptions()
                .position(loc)
                .title(title)
                .snippet(snipp)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.taxi)));
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
                //TODO update location for user

                toastMessage(location.toString());
            }

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
                .key("AIzaSyAbms9wIG8L3EtGwC2q87jXbxiuELYRENs")
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
