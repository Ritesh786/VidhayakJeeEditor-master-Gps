package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import fujitsu.vidhayak.vidhayakjeeeditor.CameraPackage.CameraActivity;
import fujitsu.vidhayak.vidhayakjeeeditor.CameraPackage.CropImage;
import fujitsu.vidhayak.vidhayakjeeeditor.CameraPackage.GlobalVariables;
import fujitsu.vidhayak.vidhayakjeeeditor.CameraPackage.UtilityClass;
import fujitsu.vidhayak.vidhayakjeeeditor.DashBoard;
import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class UploadWithImage extends AppCompatActivity implements View.OnClickListener {


    EditText mtitle,mdescription;
    String title,description,id,image;

    Button mchooseimagebtn,muploadnewsbtn;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_CROPIMAGE = 4;
    private Bitmap bitmap;
    String strtim;
    AlertDialog dialog;
    private int CAMERA_REQUEST = 2;

    public static final String KEY_ID= "id";
    public static final String KEY_title = "title";
    public static final String KEY_CONTENT = "content";
    // public static final String KEY_TYPE = "category";
     public static final String KEY_IMAGE = "image";
    // public static final String KEY_CAPTION = "caption";
ImageView mnewsimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_with_image);

        mtitle = (EditText) findViewById(R.id.titletxt);
        mdescription = (EditText) findViewById(R.id.descriptiontxt);

        mnewsimage = (ImageView) findViewById(R.id.news_Image);

        mchooseimagebtn = (Button) findViewById(R.id.chooseimage_btn);
        muploadnewsbtn = (Button) findViewById(R.id.uploadnews_btn);

        mchooseimagebtn.setOnClickListener(this);
        muploadnewsbtn.setOnClickListener(this);

        Intent intent = getIntent();
        description = intent.getStringExtra("description");
        title = intent.getStringExtra("title");
        id = intent.getStringExtra("id");
        image = intent.getStringExtra("image");
        //    image = intent.getStringExtra("image");

        mtitle.setText(title);
        mdescription.setText(description);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.chooseimage_btn:

                AlertDialog.Builder mbuilder = new AlertDialog.Builder(UploadWithImage.this);
                View mview =getLayoutInflater().inflate(R.layout.chooseimage, null);
                Button mtakephoto = (Button) mview.findViewById(R.id.imagebycamera);
                mtakephoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent cameraIntent = new Intent(UploadWithImage.this,CameraActivity.class);
                        cameraIntent.putExtra(GlobalVariables.FILENAME,GlobalVariables.profilepic_name);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);


                    }
                });

                Button mtakegallery = (Button) mview.findViewById(R.id.imagebygallery);
                mtakegallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        showfilechooser();

                    }
                });
                mbuilder.setView(mview);
                dialog = mbuilder.create();
                dialog.show();

                break;

            case R.id.uploadnews_btn:

                uploadImage();

                break;


        }
    }

    public void showfilechooser(){

        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, ""), PICK_IMAGE_REQUEST);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public void uploadImage() {


        final String title = mtitle.getText().toString().trim();
        final String description = mdescription.getText().toString().trim();
        final String image = getStringImage(bitmap);


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
        final ProgressDialog loading = ProgressDialog.show(UploadWithImage.this, "Uploading...", "Please wait...", false, false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("jabaed", id);
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            boolean success = jsonresponse.getBoolean("success");

                            if (success) {

                                Intent successin = new Intent(UploadWithImage.this,DashBoard.class);
                                successin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                successin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(successin);
                                UploadWithImage.this.finish();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(UploadWithImage.this);
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
                        Toast.makeText(UploadWithImage.this, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("bada123ed", id);

                        loading.dismiss();
                        Toast.makeText(UploadWithImage.this, error.toString(), Toast.LENGTH_LONG).show();
                        Log.d("error1234", error.toString());

                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request
                params.put(KEY_title, title);
                params.put(KEY_CONTENT, description);
                params.put(KEY_IMAGE, image);
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


        RequestQueue requestQueue = Volley.newRequestQueue(UploadWithImage.this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        //    Log.d("try4",str);
            super.onActivityResult(requestCode, resultCode, data);}catch (Exception e) {
            Log.d("try8", e.toString());
            Toast.makeText(getApplicationContext(), "On super " + e.toString(), Toast.LENGTH_LONG).show();

        }


//
//        if (requestCode >= PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//
//
//            Uri filePath = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
//                mnewsimage.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//        }

        if(requestCode==PICK_IMAGE_REQUEST) {

            if(data==null){

                Toast.makeText(getApplicationContext()," Please Select Image For Uploading.... ",Toast.LENGTH_LONG).show();

            }else {
                Uri filePath = data.getData();
                Intent intentcrop = new Intent(getApplicationContext(), CropImage.class);
                intentcrop.putExtra("ramji", filePath.toString());
                startActivityForResult(intentcrop, 6);
            }
        }


//        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            bitmap = (Bitmap) data.getExtras().get("data");
//            mnewsimage.setImageBitmap(bitmap);
//        }

        if(requestCode ==CAMERA_REQUEST ){
//            if(data.getExtras()==null){
//
//                Toast.makeText(getContext()," Please Take Image For Uploading.... ",Toast.LENGTH_LONG).show();
//
//            }else {
//                Bitmap bitmapcamear = (Bitmap) data.getExtras().get("data");
//                String bitstring = getStringImage(bitmapcamear);
//                boolean checktr = true;
//                Intent intentcrop = new Intent(getContext(), CropImage.class);
//                intentcrop.putExtra("cameraji", bitstring);
//                intentcrop.putExtra("camerajiboolean", checktr);
//                startActivityForResult(intentcrop, PICK_CROPIMAGE);
//            }
            bitmap =  UtilityClass.getImage(GlobalVariables.profilepic_name);
            mnewsimage.setImageBitmap(bitmap);
            dialog.dismiss();


        }

        if(requestCode==6)
        {
            strtim = data.getStringExtra("cropimage");
            Log.d("imageindash","imageindd "+strtim);
            bitmap = StringToBitMap(strtim);
            Log.d("imageinbitmap","imageinbit "+bitmap);
            mnewsimage.setImageBitmap(bitmap);
            dialog.dismiss();

        }
    }


    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }



}
