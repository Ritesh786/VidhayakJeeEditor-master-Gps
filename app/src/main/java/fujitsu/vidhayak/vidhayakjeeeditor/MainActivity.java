package fujitsu.vidhayak.vidhayakjeeeditor;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AbsRuntimePermission implements View.OnClickListener {

    Button mloginbtn;
    EditText mnumbertxt;
    String sendotptxt;
    android.app.AlertDialog alertDialog;

    private static final int REQUEST_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mnumbertxt = (EditText) findViewById(R.id.number_txt);
        mloginbtn = (Button) findViewById(R.id.login_btn);

        mloginbtn.setOnClickListener(this);


        requestAppPermissions(new String[]{
                        Manifest.permission.READ_SMS,
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                R.string.msg, REQUEST_PERMISSION);


        if(!isGpsEnable(MainActivity.this)){
            call();
        }


    }

    @Override
    public void onPermissionsGranted(int requestCode) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login_btn:

                SendNumber();

                break;

        }
    }

    public void SendNumber(){

        final String KEY_mobile = "mobile";
        final String KEY_token = "token";

        sendotptxt = mnumbertxt.getText().toString().trim();
        //final String token = SaveUserId.getInstance(this).getDeviceToken();

        if (TextUtils.isEmpty(sendotptxt)) {
            mnumbertxt.requestFocus();
            mnumbertxt.setError("This Field Is Mandatory");
        }
        else{

            String url = null;

            String REGISTER_URL = "http://minews.in/core/editor_login_request.php";

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
                            //   Log.d("jaba", usernsme);
                            try {
                                JSONObject jsonresponse = new JSONObject(response);
                                boolean success = jsonresponse.getBoolean("success");

                                if (success) {


                                    Intent registerintent = new Intent(MainActivity.this, Verifyotp.class);
                                    startActivity(registerintent);
                                    finish();


                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Login Failed....")
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            Toast.makeText(MainActivity.this, response.toString(), Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                             Log.d("jabadi", sendotptxt);
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_LONG).show();

                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    params.put(KEY_mobile, sendotptxt);
                    return params;

                }

            };
            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void call(){

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setMessage("No GPS Enabled ! Enable your Connection First !!! ");

        alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });
        alertDialogBuilder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                alertDialog.dismiss();
            }
        });
        alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                                           @Override
                                           public void onShow(DialogInterface arg0) {
                                               alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                               alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor( getResources().getColor( R.color.colorPrimary ));
                                           }
                                       }
        );
        alertDialog.show();

    }

    public static boolean isGpsEnable(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (statusOfGPS==true){
            return true;
        }else{
            return false;
        }
    }

}
