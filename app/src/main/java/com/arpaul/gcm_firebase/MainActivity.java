package com.arpaul.gcm_firebase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.arpaul.gcm_firebase.common.AppPreference;
import com.arpaul.gcm_firebase.gcmService.GCMFirebaseInstanceIDService;
import com.arpaul.utilitieslib.UnCaughtException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private TextView tvText;
    private Toolbar toolbar;
    private FloatingActionButton fab;


    //https://firebase.google.com/docs/?authuser=2
    //https://console.firebase.google.com/project/gcm-firebase-35787/notification?authuser=2

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Thread.setDefaultUncaughtExceptionHandler(new UnCaughtException(MainActivity.this,"aritra1704@gmail.com",getString(R.string.app_name)));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        initializeControls();

        bindControls();

        checkPlaystore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlaystore();
    }

    private void bindControls(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String sentToken = new AppPreference(MainActivity.this).getStringFromPreference(AppPreference.GCM_TOKEN, "");
                if (TextUtils.isEmpty(sentToken)) {
                    Toast.makeText(MainActivity.this, "token empty..", Toast.LENGTH_SHORT).show();
                } else {
                    tvText.setText(sentToken);
                }
            }
        }, 5 * 1000);
    }

    private void checkPlaystore(){
        if (checkPlayServices()) {
            // Because this is the initial creation of the app, we'll want to be certain we have
            // a token. If we do not, then we will start the IntentService that will register this
            // application with GCM.
            String sentToken = new AppPreference(this).getStringFromPreference(AppPreference.GCM_TOKEN, "");
            if (TextUtils.isEmpty(sentToken)) {
                Intent intent = new Intent(this, GCMFirebaseInstanceIDService.class);
                startService(intent);
            }
        }
    }

    private boolean checkPlayServices(){
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS) {
            if(apiAvailability.isUserResolvableError(resultCode)) {
//                apiAvailability.getErrorDialog(this, resultCode, GoogleApiAvailability.GOOGLE_PLAY_SERVICES_VERSION_CODE).show();
                apiAvailability.makeGooglePlayServicesAvailable(this);
            } else {
                Log.i(TAG, "This device is not supported");
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
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

    private void initializeControls(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        tvText = (TextView) findViewById(R.id.tvText);
    }
}
