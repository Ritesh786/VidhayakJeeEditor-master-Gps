package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fujitsu.vidhayak.vidhayakjeeeditor.FetchingPackage.Movie;
import fujitsu.vidhayak.vidhayakjeeeditor.FetchingPackage.RecycleAdapter;
import fujitsu.vidhayak.vidhayakjeeeditor.FetchingPackage.RecyclerTouchListener;
import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class CompletedRequest extends AppCompatActivity implements View.OnClickListener {

        ImageView mbackimage;

        private ProgressDialog pDialog;
        private List<Movie> movieList = new ArrayList<Movie>();

        private RecyclerView recyclerView;

        private RecycleAdapter adapter;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_request);
        mbackimage = (ImageView) findViewById(R.id.back_imageverified);
        mbackimage.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        adapter = new RecycleAdapter(movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getApplicationContext(), new RecyclerTouchListener.OnItemClickListener() {
                        @Override public void onItemClick(View view, int position) {

                                Movie mo123 = movieList.get(position);
                                Intent newsdetailintnt = new Intent(getApplicationContext(),RequestDetailtwo.class);
                                //   newsdetailintnt.putExtra("type",mo123.getYear());
                                newsdetailintnt.putExtra("title",mo123.getTitle());
                                newsdetailintnt.putExtra("description",mo123.getRating());
                                newsdetailintnt.putExtra("image",mo123.getThumbnailUrl());
                                newsdetailintnt.putExtra("id",mo123.getId());
                                //    newsdetailintnt.putExtra("caption",mo123.getCaption());
                                startActivity(newsdetailintnt);

                                // TODO Handle item click
                        }
                })
        );

        pDialog = new ProgressDialog(CompletedRequest.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        populatedata();

}

@Override
public void onClick(View v) {

        CompletedRequest.this.finish();

}

        public void populatedata(){

                final String url = "http://minews.in/lumen/public/stories/completed";

                JsonArrayRequest movieReq = new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                        Log.d("rr000", response.toString());
                                        hidePDialog();

                                        // Parsing json
                                        for (int i = 0; i < response.length(); i++) {
                                                try {

                                                        JSONObject obj = response.getJSONObject(i);

                                                        Movie movie = new Movie();

                                                        String imagestr = obj.getString("image");
                                                        String imagrurl = "https://s3.ap-south-1.amazonaws.com/vidhayak-storage/images/stories/";
                                                        String imageurlfull = imagrurl+imagestr;


                                                        movie.setTitle(obj.getString("title"));
                                                        movie.setThumbnailUrl(imageurlfull);
                                                        movie.setRating(obj.getString("content"));

                                                        movie.setYear("Request");
                                                        movie.setGenre(obj.getString("updated_at"));
                                                        movie.setId(obj.getString("id"));

                                                        // Genre is json array  (Number) obj.get("type")
//                                JSONArray genreArry = obj.getJSONArray("created");
//                                ArrayList<String> genre = new ArrayList<String>();
//                                for (int j = 0; j < genreArry.length(); j++) {
//                                    genre.add((String) genreArry.get(j));
//                                }
//                                movie.setGenre(genre);

                                                        // adding movie to movies array
                                                        movieList.add(movie);
                                                        Log.d("valu123","caption "+obj.getString("caption"));

                                                } catch (JSONException e) {
                                                        e.printStackTrace();
                                                }

                                        }

                                        // notifying list adapter about data changes
                                        // so that it renders the list view with updated data
                                        adapter.notifyDataSetChanged();
                                }
                        }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                                //  Log.d(TAG, "Error: " + error.getMessage());
                                hidePDialog();

                        }
                });{

                        RequestQueue requestQueue = Volley.newRequestQueue(CompletedRequest.this);
                        requestQueue.add(movieReq);
                }

                // Adding request to request queue




        }

        @Override
        public void onDestroy() {
                super.onDestroy();
                hidePDialog();
        }

        private void hidePDialog() {
                if (pDialog != null) {
                        pDialog.dismiss();
                        pDialog = null;
                }
        }

}