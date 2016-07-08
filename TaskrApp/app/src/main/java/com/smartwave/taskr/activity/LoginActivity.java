package com.smartwave.taskr.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.model.people.Person;
import com.smartwave.taskr.core.AppController;
import com.smartwave.taskr.core.BaseActivity;
import com.smartwave.taskr.core.DBHandler;
import com.smartwave.taskr.core.Engine;
import com.smartwave.taskr.core.SharedPreferencesCore;
import com.smartwave.taskr.core.TSingleton;
import com.smartwave.taskr.fragment.LoginFragment;
import com.smartwave.taskr.R;
import com.google.android.gms.plus.Plus;
import com.smartwave.taskr.object.TaskObject;

import java.io.InputStream;
import java.util.HashMap;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import jp.wasabeef.blurry.Blurry;

public class LoginActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks {

    public static LoginActivity INSTANCE = null;

    public GoogleApiClient google_api_client;
    GoogleApiAvailability google_api_availability;

    SignInButton signIn_btn;
    private static final int SIGN_IN_CODE = 0;
    private static final int PROFILE_PIC_SIZE = 120;
    private ConnectionResult connection_result;

    private boolean is_intent_inprogress;
    public boolean is_signInBtn_clicked;
    private int request_code;
    ProgressDialog progress_dialog;
    private ImageView mImageBg;
    private Bitmap bitmap;



    private SliderLayout mDemoSlider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        INSTANCE = this;

        /* Initialize Frame Layout */
//        setFrameLayout(R.id.framelayout);
//
//        Engine.switchFragment(INSTANCE, new LoginFragment(), getFrameLayout());

//        buidNewGoogleApiClient();
//        //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
//        custimizeSignBtn();
//        setBtnClickListeners();
//        progress_dialog = new ProgressDialog(this);
//        progress_dialog.setMessage("Signing in....");
//

        if (TSingleton.getLogoutGmail()!= null){
            if (TSingleton.getLogoutGmail().equalsIgnoreCase("1")){
                buidNewGoogleApiClient();
                //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
                custimizeSignBtn();
                gPlusSignOut();
            } else {
                buidNewGoogleApiClient();
                //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
                custimizeSignBtn();
                setBtnClickListeners();
                progress_dialog = new ProgressDialog(this);
                progress_dialog.setMessage("Signing in....");

            }
        } else {
            buidNewGoogleApiClient();
            //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
            custimizeSignBtn();
            setBtnClickListeners();
            progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Signing in....");

        }

        mImageBg = (ImageView) findViewById(R.id.imagebackground);


        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.taskbg);
        Bitmap blurred = blurRenderScript(this,bitmap, 25);
//        mImageBg.setImageBitmap(blurred);


        Drawable d = new BitmapDrawable(getResources(), blurred);

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Taskr", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Do it", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("Sample", "http://cdn3.nflximg.net/images/3093/2043093.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Taskr",R.drawable.doneimage);
        file_maps.put("Do it",R.drawable.tasktime);
        file_maps.put("Sample",R.drawable.taskbg);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
//            .image(file_maps.get(name))

            textSliderView
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(3000);


    }


        /*
   create and  initialize GoogleApiClient object to use Google Plus Api.
   While initializing the GoogleApiClient object, request the Plus.SCOPE_PLUS_LOGIN scope.
   */

    private void buidNewGoogleApiClient(){

        google_api_client =  new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API,Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

    }

    private void setBtnClickListeners(){
        // Button listeners
        signIn_btn.setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
    }

     /*
      Customize sign-in button. The sign-in button can be displayed in
      multiple sizes and color schemes. It can also be contextually
      rendered based on the requested scopes. For example. a red button may
      be displayed when Google+ scopes are requested, but a white button
      may be displayed when only basic profile is requested. Try adding the
      Plus.SCOPE_PLUS_LOGIN scope to see the  difference.
    */

    private void custimizeSignBtn(){

        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signIn_btn.setSize(SignInButton.SIZE_STANDARD);
        signIn_btn.setScopes(new Scope[]{Plus.SCOPE_PLUS_LOGIN});

    }

    protected void onStart() {
        super.onStart();
        google_api_client.connect();
    }

    protected void onStop() {
        super.onStop();
        if (google_api_client.isConnected()) {
            google_api_client.disconnect();
        }
        mDemoSlider.stopAutoCycle();
    }

    protected void onResume(){
        super.onResume();
        if (google_api_client.isConnected()) {
            google_api_client.connect();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        is_signInBtn_clicked = false;
        // Get user's information and set it into the layout
        getProfileInfo();
        // Update the UI after signin
//        changeUI(true);

        startActivity(new Intent(LoginActivity.this, InitialActivity.class));


    }

    @Override
    public void onConnectionSuspended(int i) {
        google_api_client.connect();
        changeUI(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                Toast.makeText(this, "start sign process", Toast.LENGTH_SHORT).show();
                gPlusSignIn();
                break;
            case R.id.sign_out_button:
                Toast.makeText(this, "Sign Out from G+", Toast.LENGTH_LONG).show();
                gPlusSignOut();

                break;
            case R.id.disconnect_button:
                Toast.makeText(this, "Revoke Access from G+", Toast.LENGTH_LONG).show();
                gPlusRevokeAccess();

                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {

        if (!result.hasResolution()) {
//            google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();


            if (google_api_availability != null){
                google_api_availability.getErrorDialog(this, result.getErrorCode(),request_code).show();
            }

            return;
        }

        if (!is_intent_inprogress) {

            connection_result = result;

            if (is_signInBtn_clicked) {

                resolveSignInError();
            }
        }

    }

     /*
      Sign-in into the Google + account
     */

    private void gPlusSignIn() {
        if (!google_api_client.isConnecting()) {
            Log.d("user connected","connected");
            is_signInBtn_clicked = true;
            progress_dialog.show();
            resolveSignInError();

        }


        //try
        final DBHandler db = new DBHandler(this);
        db.addTask(new TaskObject("Task 1", " Create database", "listed", "AV","12/12/2016","0"));
        db.addTask(new TaskObject("Task 2", "Login with Gmail", "listed", "AV", "10/10/2016","0"));
        db.addTask(new TaskObject("Task 3", "UI design", "listed", "AV", "09/12/2016","0"));



    }

        /*
      Method to resolve any signin errors
     */

    private void resolveSignInError() {
        if (connection_result.hasResolution()) {
            try {
                is_intent_inprogress = true;
                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
                Log.d("resolve error", "sign in error resolved");
            } catch (IntentSender.SendIntentException e) {
                is_intent_inprogress = false;
                google_api_client.connect();
            }
        }

//        if (connection_result == null){
//            Log.d("connection_result", "null");
//                is_intent_inprogress = false;
//                google_api_client.connect();
//        } else{
//            Log.d("connection_result", "not null");
//                is_intent_inprogress = true;
//            try {
//                connection_result.startResolutionForResult(this, SIGN_IN_CODE);
//            } catch (IntentSender.SendIntentException e) {
//                e.printStackTrace();
//            }
//        }

    }

    /*
      Sign-out from Google+ account
     */

    public void gPlusSignOut() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            google_api_client.disconnect();
            google_api_client.connect();
//            changeUI(false);
        }
    }

      /*
     Revoking access from Google+ account
     */

    private void gPlusRevokeAccess() {
        if (google_api_client.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(google_api_client);
            Plus.AccountApi.revokeAccessAndDisconnect(google_api_client)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.d("MainActivity", "User access revoked!");
                            buidNewGoogleApiClient();
                            google_api_client.connect();
                            changeUI(false);
                        }

                    });
        }
    }

    /*
     get user's information name, email, profile pic,Date of birth,tag line and about me
     */

    private void getProfileInfo() {

        try {

            if (Plus.PeopleApi.getCurrentPerson(google_api_client) != null) {
                Person currentPerson = Plus.PeopleApi.getCurrentPerson(google_api_client);
                setPersonalInfo(currentPerson);

            } else {
                Toast.makeText(getApplicationContext(),
                        "No Personal info mention", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

      /*
     set the User information into the views defined in the layout
     */

    private void setPersonalInfo(Person currentPerson){

        String personName = currentPerson.getDisplayName();
        String personPhotoUrl = currentPerson.getImage().getUrl();
        String email = Plus.AccountApi.getAccountName(google_api_client);
//        TextView   user_name = (TextView) findViewById(R.id.userName);
//        user_name.setText("Name: "+personName);
//        TextView gemail_id = (TextView)findViewById(R.id.emailId);
//        gemail_id.setText("Email Id: " +email);
//        TextView dob = (TextView)findViewById(R.id.dob);
//        dob.setText("DOB: "+currentPerson.getBirthday());
//        TextView tag_line = (TextView)findViewById(R.id.tag_line);
//        tag_line.setText("Tag Line: " +currentPerson.getTagline());
//        TextView about_me = (TextView)findViewById(R.id.about_me);
//        about_me.setText("About Me: "+currentPerson.getAboutMe());
//        setProfilePic(personPhotoUrl);
//        progress_dialog.dismiss();
//        Toast.makeText(this, "Person information is shown!", Toast.LENGTH_LONG).show();

        TSingleton.setUserName(personName);
        SharedPreferencesCore.setSomeStringValue(AppController.getInstance(),"username",personName);

    }

    private void setProfilePic(String profile_pic){
        profile_pic = profile_pic.substring(0,
                profile_pic.length() - 2)
                + PROFILE_PIC_SIZE;
        ImageView    user_picture = (ImageView)findViewById(R.id.profile_pic);
        new LoadProfilePic(user_picture).execute(profile_pic);
    }

    private void changeUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    /*
   Will receive the activity result and check which request we are responding to

  */
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        // Check which request we're responding to
        if (requestCode == SIGN_IN_CODE) {
            request_code = requestCode;
            if (responseCode != RESULT_OK) {
                is_signInBtn_clicked = false;
                progress_dialog.dismiss();

            }

            is_intent_inprogress = false;

            if (!google_api_client.isConnecting()) {
                google_api_client.connect();
            }
        }
    }


      /*
    Perform background operation asynchronously, to load user profile picture with new dimensions from the modified url
    */

    private class LoadProfilePic extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmap_img;

        public LoadProfilePic(ImageView bitmap_img) {
            this.bitmap_img = bitmap_img;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap new_icon = null;
            try {
                InputStream in_stream = new java.net.URL(url).openStream();
                new_icon = BitmapFactory.decodeStream(in_stream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return new_icon;
        }

        protected void onPostExecute(Bitmap result_img) {

            bitmap_img.setImageBitmap(result_img);
        }
    }


    @SuppressLint("NewApi")
    public static Bitmap blurRenderScript(Context context, Bitmap smallBitmap, int radius) {
        try {
            smallBitmap = RGB565toARGB888(smallBitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bitmap bitmap = Bitmap.createBitmap(
                smallBitmap.getWidth(), smallBitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        RenderScript renderScript = RenderScript.create(context);

        Allocation blurInput = Allocation.createFromBitmap(renderScript, smallBitmap);
        Allocation blurOutput = Allocation.createFromBitmap(renderScript, bitmap);

        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(renderScript,
                Element.U8_4(renderScript));
        blur.setInput(blurInput);
        blur.setRadius(radius); // radius must be 0 < r <= 25
        blur.forEach(blurOutput);

        blurOutput.copyTo(bitmap);
        renderScript.destroy();

        return bitmap;

    }

    private static Bitmap RGB565toARGB888(Bitmap img) throws Exception {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        //Get JPEG pixels.  Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        //Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);

        //Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }


}
