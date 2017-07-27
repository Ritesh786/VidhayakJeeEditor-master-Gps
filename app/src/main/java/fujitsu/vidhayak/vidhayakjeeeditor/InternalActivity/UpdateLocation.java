package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fujitsu.vidhayak.vidhayakjeeeditor.DashBoard;
import fujitsu.vidhayak.vidhayakjeeeditor.R;
import fujitsu.vidhayak.vidhayakjeeeditor.SaveUserId;
import fujitsu.vidhayak.vidhayakjeeeditor.Verifyotp;

public class UpdateLocation extends AppCompatActivity implements View.OnClickListener {

    Button msendlocationbtn;
    String lat, log;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    ProgressDialog locationProgressDialog;
    Timer timer1;
    TimerTask timerTask;
    int count = 0;
    Location myLocation;
    ImageView mbackimage;
    String lat1 = null, log1= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_location);

        msendlocationbtn = (Button) findViewById(R.id.sendlocation_btn);
        msendlocationbtn.setOnClickListener(this);
        alert = new AlertDialog.Builder(this);
        locationProgressDialog = new ProgressDialog(this);

        mbackimage = (ImageView) findViewById(R.id.back_imageverified);
        mbackimage.setOnClickListener(this);

        locationProgressDialog.setMessage("Getting location\n Please wait");
        getGpsLocation();
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.sendlocation_btn:

                SendLocation();

                break;

            case R.id.back_imageverified:

              UpdateLocation.this.finish();

                break;


        }
    }


//    public void location(){
//
//        LocationManager locationManager =
//                (LocationManager) UpdateLocation.this.getSystemService(Context.LOCATION_SERVICE);
//
//        LocationListener locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//                lat = Double.toString(location.getLatitude());
//                log = Double.toString(location.getLongitude());
//                Log.d("khbbibkb", lat + "lolo"+log);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//
//    }


    private void getGpsLocation() {

        locationProgressDialog.show();
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           /* alert.setTitle("GPS");
            alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS...");*/
            alert.setNegativeButton("Cancel..", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            alert.setView(R.layout.gps_message);
            alert.setPositiveButton("Allow Gps..",
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                return;
                            }
                            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000,
                                    10, (android.location.LocationListener) listener);
                            setCriteria();

                            mlocManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 0, (float)
                                            0.01, (android.location.LocationListener) listener);

                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(I);

                        }
                    });
            alert.show();


        } else {

            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    (float) 0.01, (android.location.LocationListener) listener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    (float) 0.01, (android.location.LocationListener) listener);
        }
        count=0;
        startTimer();
    }

    private void startTimer(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>20)
                        {
                            if(locationProgressDialog.isShowing()) locationProgressDialog.dismiss();
                            if (myLocation==null){
                                //Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                                timer1.cancel();
                            }
                        }
                    }
                });
            }
        };

        timer1.schedule(timerTask, 1000, 1000);
    }



    private final android.location.LocationListener listener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            myLocation=location;


            if (location.getLatitude() > 0.0) {
                DecimalFormat df= new DecimalFormat("#.00000000");

                lat=String.valueOf(df.format(location.getLatitude()));
                log=String.valueOf(df.format(location.getLongitude()));
                Log.d("khbbibkb", lat + "lolo"+log);
                locationProgressDialog.setMessage("lat: "+lat+"\n"+"long:"+log+"\n"+"accuracy:"+location.getAccuracy());
                if (location.getAccuracy()>0 && location.getAccuracy()<1000) {
                    if(locationProgressDialog.isShowing()) locationProgressDialog.dismiss();
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lang, Toast.LENGTH_LONG).show();

                }
                else
                {


                }
            }
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

    public String setCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = mlocManager.getBestProvider(criteria, true);
        return provider;
    }

    private void SendLocation() {


        final String macid = "abc";
        Log.d("mc00", "macid11" + macid);
        final String KEY_lat = "lat";
        final String KEY_long = "long";



            String url = null;
            String REGISTER_URL = "http://minews.in/lumen/public/location/create";

            REGISTER_URL = REGISTER_URL.replaceAll(" ", "%20");
            try {
                URL sourceUrl = new URL(REGISTER_URL);
                url = sourceUrl.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("latbever", lat);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateLocation.this);
                                    builder.setMessage("Send Location Failed")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(UpdateLocation.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Log.d("jabadi", usernsme);
                            Toast.makeText(UpdateLocation.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(KEY_lat, lat);
                    params.put(KEY_long, log);
                    return params;

                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(UpdateLocation.this);
            requestQueue.add(stringRequest);
        }
    }



