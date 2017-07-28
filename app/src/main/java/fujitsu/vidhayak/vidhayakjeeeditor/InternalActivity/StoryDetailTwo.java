package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class StoryDetailTwo extends AppCompatActivity implements View.OnClickListener {

    TextView mtitle, mdescription;
    ImageView mnewsimmage;
    String title, description,image,id;
    URL url;
    ImageView mbackomage;
    Button minprogressbt, mcompletedbtn, mrejectedbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail_two);

        mtitle = (TextView) findViewById(R.id.title);
        mdescription = (TextView) findViewById(R.id.description);
        //   mcontent = (TextView) findViewById(R.id.newscontent);
//        mcaption = (TextView) findViewById(R.id.newsimgcaption);
        mnewsimmage = (ImageView) findViewById(R.id.newsimage);

        mbackomage = (ImageView) findViewById(R.id.back_image);
        mbackomage.setOnClickListener(this);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        image = intent.getStringExtra("image");
        id = intent.getStringExtra("id");

        //  Log.d("con00","conte "+content);

        mtitle.setText("Request Title:" + title);
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
    public void onClick(View v) {
        StoryDetailTwo.this.finish();
    }
}
