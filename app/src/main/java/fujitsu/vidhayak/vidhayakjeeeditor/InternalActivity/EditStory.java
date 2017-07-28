package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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

import fujitsu.vidhayak.vidhayakjeeeditor.DashBoard;
import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class EditStory extends AppCompatActivity implements View.OnClickListener {

    EditText mtitle,mdescription;
    String title,description,id,image;

    Button muolpadeditbtn,meditwithimgbtn;

    public static final String KEY_ID= "id";
    public static final String KEY_title = "title";
    public static final String KEY_CONTENT = "content";
   // public static final String KEY_TYPE = "category";
    // public static final String KEY_IMAGE = "image";
   // public static final String KEY_CAPTION = "caption";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_story);


        mtitle = (EditText) findViewById(R.id.requesttitletxt);
        mdescription = (EditText) findViewById(R.id.requestdescriptiontxt);

        muolpadeditbtn = (Button) findViewById(R.id.uploadedt_btn);
        meditwithimgbtn = (Button) findViewById(R.id.editwithimg_btn);
        muolpadeditbtn.setOnClickListener(this);
        meditwithimgbtn.setOnClickListener(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

        mtitle.setText(title);
        mdescription.setText(description);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.uploadedt_btn:

                Editedupload();

                break;

            case R.id.editwithimg_btn:

                Intent newsestinnt = new Intent(EditStory.this,SaveImage.class);
                newsestinnt.putExtra("id",id);
                newsestinnt.putExtra("title",title);
                newsestinnt.putExtra("description",description);
                newsestinnt.putExtra("image",image);
                startActivity(newsestinnt);
                break;

        }

    }

    public void Editedupload(){


        final String title = mtitle.getText().toString();
        final String description = mdescription.getText().toString();

        String url = null;
        String REGISTER_URL = "http://minews.in/lumen1/public/story/edit/";
        String newurl = REGISTER_URL+id;

        newurl = newurl.replaceAll(" ", "%20");
        try {
            URL sourceUrl = new URL(newurl);
            url = sourceUrl.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        final ProgressDialog loading = ProgressDialog.show(EditStory.this, "Uploading.....", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("jabaedit", id);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {

                                Intent successin = new Intent(EditStory.this,DashBoard.class);
                                startActivity(successin);

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(EditStory.this);
                                builder.setMessage("Upoading Failed")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("jabadi", id);
                        loading.dismiss();
                        Toast.makeText(EditStory.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("bada123", id);

                        loading.dismiss();
                        Toast.makeText(EditStory.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("error1234", error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(KEY_title, title);
                params.put(KEY_CONTENT, description);
                return params;

            }

        };
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        500000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );


        RequestQueue requestQueue = Volley.newRequestQueue(EditStory.this);
        requestQueue.add(stringRequest);



    }

}
