package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fujitsu.vidhayak.vidhayakjeeeditor.DashBoard;
import fujitsu.vidhayak.vidhayakjeeeditor.R;
import fujitsu.vidhayak.vidhayakjeeeditor.SaveUserId;
import fujitsu.vidhayak.vidhayakjeeeditor.Verifyotp;

public class NewsDetail extends AppCompatActivity implements View.OnClickListener {

    TextView mtitle, mdescription;
    ImageView mnewsimmage;
    String title, description,image,id;
    URL url;
    ImageView mbackomage;
    Button minprogressbt, mcompletedbtn, mrejectedbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        mtitle = (TextView) findViewById(R.id.title);
        mdescription = (TextView) findViewById(R.id.description);
     //   mcontent = (TextView) findViewById(R.id.newscontent);
//        mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);

        minprogressbt = (Button) findViewById(R.id.inprogress_btn);
        mcompletedbtn = (Button) findViewById(R.id.completed_btn);
        mrejectedbtn = (Button) findViewById(R.id.disapproved_btn);

        minprogressbt.setOnClickListener(this);
        mcompletedbtn.setOnClickListener(this);
        mrejectedbtn.setOnClickListener(this);

        mbackomage = (ImageView) findViewById(R.id.back_image);
        mbackomage.setOnClickListener(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

        Log.d("con00","conte "+title  + id);

        mtitle.setText("Request title:" + title);
        mdescription.setText("Request Description:" + description);
     //   mcontent.setText("News Content:" + content);
        //   mcaption.setText(caption);

        try {
            url = new URL(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.with(getApplicationContext()).load(String.valueOf(url)).resize(1024, 1024).into(mnewsimmage);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.back_image:

                NewsDetail.this.finish();
                break;

            case R.id.inprogress_btn:


                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmation Dialogue")
                        .setMessage("Are you sure you want to Put Request In Inprogress")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Inprogress();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();




                break;

            case R.id.completed_btn:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Confirmation Dialogue")
                        .setMessage("Are you sure you want to Put Request In Completed?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                compllete();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

                break;

            case R.id.disapproved_btn:

                Intent newsdetailintnt = new Intent(getApplicationContext(),RejectRequest.class);
                newsdetailintnt.putExtra("id",id);
                startActivity(newsdetailintnt);

                break;

        }
    }


    private void Inprogress(){

        final String KEY_id = "id";
        final String KEY_status = "status";
        final String VERSTATUS = "1";


        String REGISTER_URL = "http://minews.in/lumen1/public/post/action/";
        String newurl = REGISTER_URL+id;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, newurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                           Log.d("jaba", String.valueOf(VERSTATUS));

                        JSONObject jsonresponse = null;
                        try {
                            jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {

                                Intent registerintent = new Intent(NewsDetail.this, DashBoard.class);
                                startActivity(registerintent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(NewsDetail.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("jabadi", usernsme);
                        Toast.makeText(NewsDetail.this, error.toString(), Toast.LENGTH_LONG).show();

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(KEY_status, VERSTATUS);
                return params;

            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(NewsDetail.this);
        requestQueue.add(stringRequest);
    }



   public void compllete(){

       final String KEY_status = "status";
       final String REJSTATUS = "3";

       String REGISTER_URL = "http://minews.in/lumen1/public/post/action/";
       String newurl = REGISTER_URL+id;

       StringRequest stringRequest = new StringRequest(Request.Method.POST, newurl,
               new Response.Listener<String>() {
                   @Override
                   public void onResponse(String response) {
                       Log.d("jaba000", String.valueOf(REJSTATUS));

                       JSONObject jsonresponse = null;
                       try {
                           jsonresponse = new JSONObject(response);
                           boolean success = jsonresponse.getBoolean("success");

                           if (success) {

                               Intent registerintent = new Intent(NewsDetail.this, DashBoard.class);
                               startActivity(registerintent);
                               finish();
                           }

                       } catch (JSONException e) {
                           e.printStackTrace();
                       }

                       Toast.makeText(NewsDetail.this, response.toString(), Toast.LENGTH_LONG).show();
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {
                       // Log.d("jabadi", usernsme);
                       Toast.makeText(NewsDetail.this, error.toString(), Toast.LENGTH_LONG).show();

                   }
               }) {


           @Override
           protected Map<String, String> getParams() throws AuthFailureError {
               Map<String, String> params = new HashMap<>();
               //Adding parameters to request

               params.put(KEY_status, REJSTATUS);

               return params;

           }

       };
       RequestQueue requestQueue = Volley.newRequestQueue(NewsDetail.this);
       requestQueue.add(stringRequest);
   }


}


