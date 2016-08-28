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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/*
The goal of this application is to display Users Facebook information and
perform various activities on Google maps that is embedded into the application.

To access the application one must login into the application using Facebook.
Once the user is Logged in, he/she will see 2 buttons, One to display facebook information
and the other to open google maps.

On hitting the 'Show FB DETAILS' button, the user will see a screen which displays the users Profile Picture, Name,
Date of birth, email address, Location and their Gender.
All this information is pulled from the Facebook's Graph API by making an async request to it.
Please note this information will only be visible if it is specified in the users facebook profile
and the visibility is set to public.

On clicking the second button 'Open Google Maps', the user will see google maps that is embedded into the application.
The screen will we consist a marker that is pointed to Tempe by default.

The user will be able to switch between various Maps types(Normal, Satellite, Terrain, Hybrid), by selecting
the grouped menu button on the top right section of the screen.

The user can also view the latitude, longitude coordinates of any location on the map by clicking on the location.

Additionally, the user will be able to add markers on the map in two ways.
First, the user can click on the 'ADD MARKER' and specify the latitude, longitude for the location.
Second, the user can add a marker by going to the location on the map and clicking on the Map for a long duration.
*/

public class MainActivity extends AppCompatActivity {
    private TextView info;

    private LoginButton loginButton;
    private Button showFBbutton;
    private Button openMapsbutton;

    private CallbackManager callbackManager;
    private ProfileTracker mProfileTracker;

    private String userid = "";
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();

        showFBbutton = (Button) findViewById(R.id.fbdetailsbutton);
        openMapsbutton = (Button) findViewById(R.id.openMapsButton);

        info = (TextView)findViewById(R.id.info);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                showContent();
                userid = loginResult.getAccessToken().getUserId();
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            Log.v("facebook - profile", profile2.getFirstName());
                            info.setText("Welcome " + profile2.getFirstName() + "!!!");
                            mProfileTracker.stopTracking();
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("facebook - profile", profile.getFirstName());
                    String name = profile.getFirstName();
                    info.setText("Welcome " + name + "!!!");
                }
                Toast.makeText(getApplicationContext(), "User logged in", Toast.LENGTH_LONG).show();

                /*info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );*/

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login attempt canceled.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_LONG).show();
            }
        });

        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    clearContent();
                }
            }
        };

        clearContent();
    }


    private void clearContent(){
        LoginManager.getInstance().logOut();
        if(info!=null) {
            info.setVisibility(View.GONE);
        }
        if(showFBbutton!=null) {
            showFBbutton.setVisibility(View.GONE);
        }
        if(openMapsbutton!=null) {
            openMapsbutton.setVisibility(View.GONE);
        }

    }

    private void showContent(){
        info.setVisibility(View.VISIBLE);
        showFBbutton.setVisibility(View.VISIBLE);
        openMapsbutton.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void showFBDetails(View view){
        Intent fbDetails = new Intent(mainActivity, DisplayFBDetails.class);
        fbDetails.putExtra("userid", userid);
        mainActivity.startActivityForResult(fbDetails,1);
    }

    public void openGoogleMaps(View view){
        Intent maps = new Intent(mainActivity, MapsActivity.class);
        mainActivity.startActivityForResult(maps,1);
    }
}
