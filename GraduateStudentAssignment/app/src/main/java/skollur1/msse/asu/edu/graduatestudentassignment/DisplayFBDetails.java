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
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

/*
This class allows us to connect to Facebook using the Facebook graph API and pull user details.
Currently the profile picture, name, date of birth, email address, gender and location of the user are displayed.
Please note this information will only be visible if it is specified in the users facebook profile
and the visibility is set to public.
 */

public class DisplayFBDetails extends AppCompatActivity {

    private TextView name;
    private TextView dob;
    private TextView email;
    private TextView location;
    private TextView gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_fb_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = (TextView)findViewById(R.id.nameview);
        dob = (TextView)findViewById(R.id.dobview);
        email = (TextView)findViewById(R.id.emailview);
        location = (TextView)findViewById(R.id.locationview);
        gender = (TextView)findViewById(R.id.genderview);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");

        //Getting the users profile picture
        ProfilePictureView profilePictureView;

        profilePictureView = (ProfilePictureView) findViewById(R.id.image);

        profilePictureView.setProfileId(userid);

        //Submitting a new Graph request which will execute as an async task to pull users details.
        GraphRequest request = GraphRequest.newMeRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        try {
                            name.setText(object.getString("name"));
                            email.setText(object.getString("email"));

                            dob.setText(object.getString("birthday"));
                            gender.setText(object.getString("gender"));

                            if (object.getJSONObject("location") == null) {
                                location.setText("NA");
                            } else {
                                location.setText(object.getJSONObject("location").getString("name"));
                            }
                        } catch (Exception e) {
                            Log.e("Error ", e.getMessage());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        //Setting parameters to the request.
        parameters.putString("fields", "name,email,gender,birthday,location");
        request.setParameters(parameters);
        request.executeAsync();

    }

    //Adding back button to the page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.backMenu:
                this.finish();
            default:
                super.onOptionsItemSelected(menuItem);
        }
        return true;
    }


}
