package skollur1.msse.asu.edu.graduatestudentassignment;
/*
 * Copyright 2016 Supraj Kolluri,
 *
 *
 * The contents of the file can only be used for the purpose of grading and reviewing.
 * The instructor and the University have the right to build and evaluate the
 * software package for the purpose of determining the grade and program assessment.
 *
 *
 * @author Supraj Kolluri mailto:supraj.kolluri@asu.edu
 *         Software Engineering, CIDSE, IAFSE, ASU Poly
 * @version April 28, 2016
 */
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*
In this activity the user will be able to perform various actions in the
google map that is embedded into the application.

The screen will we consist a marker that is pointed to Tempe by default.

The user will be able to switch between various Maps types(Normal, Satellite, Terrain, Hybrid), by selecting
the grouped menu button on the top right section of the screen.

The user can also view the latitude, longitude coordinates of any location on the map by clicking on the location.

Additionally, the user will be able to add markers on the map in two ways.
First, the user can click on the 'ADD MARKER' and specify the latitude, longitude for the location.
Second, the user can add a marker by going to the location on the map and clicking on the Map for a long duration.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener  {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Tempe and move the camera
        LatLng tempe = new LatLng(33, -111);
        mMap.addMarker(new MarkerOptions().position(tempe).title("Marker in Tempe"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(tempe));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(5.0f));
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Switch between various menu items and select the map type.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normalMap:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.satelliteMap:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                break;
            case R.id.terrainMap:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                break;
            case R.id.hybridMap:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                break;
            case R.id.noneMap:
                if(mMap != null){
                    mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                }
                break;
            case R.id.menu_legalnotices:
                String LicenseInfo = GoogleApiAvailability
                        .getInstance()
                        .getOpenSourceSoftwareLicenseInfo(MapsActivity.this);
                AlertDialog.Builder LicenseDialog =
                        new AlertDialog.Builder(MapsActivity.this);
                LicenseDialog.setTitle("Legal Notices");
                LicenseDialog.setMessage(LicenseInfo);
                LicenseDialog.show();
                break;
            case R.id.menu_addmarkers:
                addMarkers();
                break;
            default:
                super.onOptionsItemSelected(item);
                break;
        }
        return true;
    }

    private void addMarkers(){
        if(mMap != null){

            //create custom LinearLayout programmatically
            LinearLayout layout = new LinearLayout(MapsActivity.this);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            layout.setOrientation(LinearLayout.VERTICAL);

            final EditText titleET = new EditText(MapsActivity.this);
            titleET.setHint("Location Name");

            final EditText latitudeET = new EditText(MapsActivity.this);
            latitudeET.setHint("Latitude");
            latitudeET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

            final EditText longitudeET = new EditText(MapsActivity.this);
            longitudeET.setHint("Longitude");
            longitudeET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

            layout.addView(titleET);
            layout.addView(latitudeET);
            layout.addView(longitudeET);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Marker");
            builder.setView(layout);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    boolean parsable = true;
                    Double lat = null, lon = null;

                    String latitude = latitudeET.getText().toString();
                    String longitude = longitudeET.getText().toString();
                    String title = titleET.getText().toString();

                    try{
                        lat = Double.parseDouble(latitude);
                    }catch (NumberFormatException ex){
                        parsable = false;
                        Toast.makeText(MapsActivity.this, "Incorrect Value Entered", Toast.LENGTH_LONG).show();
                    }

                    try{
                        lon = Double.parseDouble(longitude);
                    }catch (NumberFormatException ex){
                        parsable = false;
                        Toast.makeText(MapsActivity.this, "Incorrect Value Entered", Toast.LENGTH_LONG).show();
                    }

                    if(parsable){
                        LatLng targetLatLng = new LatLng(lat, lon);
                        MarkerOptions markerOptions = new MarkerOptions().position(targetLatLng).title(title);
                        mMap.addMarker(markerOptions);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(targetLatLng));
                    }
                }
            });
            builder.setNegativeButton("Cancel", null);

            builder.show();
        }else{
            Toast.makeText(MapsActivity.this, "Map not ready", Toast.LENGTH_LONG).show();
        }
    }

    //Display latitude, longitude information when the user clicks on the map.
    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this, "Latitude:Longitude:\n" + latLng.latitude + " : " + latLng.longitude,
                Toast.LENGTH_LONG).show();
    }

    //Add marker on the location where the user performed a long click.
    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(MapsActivity.this, "Latitude:Longitude\n" + latLng.latitude + " : " + latLng.longitude, Toast.LENGTH_LONG).show();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(latLng.toString());
        mMap.addMarker(markerOptions);
    }
}
