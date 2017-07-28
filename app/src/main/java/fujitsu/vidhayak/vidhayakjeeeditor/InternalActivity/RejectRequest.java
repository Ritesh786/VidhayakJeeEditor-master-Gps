package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import fujitsu.vidhayak.vidhayakjeeeditor.DashBoard;
import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class RejectRequest extends AppCompatActivity implements View.OnClickListener {

    EditText mreasontxt;
    Button msubmitbtn;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reject_request);

        mreasontxt = (EditText) findViewById(R.id.resontxt);
        msubmitbtn = (Button) findViewById(R.id.submit_btn);
        msubmitbtn.setOnClickListener(this);

        id = getIntent().getStringExtra("id");

    }

    @Override
    public void onClick(View v) {

        Inprogress();

    }

    private void Inprogress(){

        final String KEY_id = "id";
        final String KEY_status = "status";
        final String VERSTATUS = "2";


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

                                Intent registerintent = new Intent(RejectRequest.this, DashBoard.class);
                                startActivity(registerintent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(RejectRequest.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("jabadi", usernsme);
                        Toast.makeText(RejectRequest.this, error.toString(), Toast.LENGTH_LONG).show();

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
        RequestQueue requestQueue = Volley.newRequestQueue(RejectRequest.this);
        requestQueue.add(stringRequest);
    }

}
