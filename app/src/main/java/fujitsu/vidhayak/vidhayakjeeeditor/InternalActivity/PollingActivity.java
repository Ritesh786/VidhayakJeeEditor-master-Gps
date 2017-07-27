package fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import fujitsu.vidhayak.vidhayakjeeeditor.R;

public class PollingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mbackimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polling);

        mbackimage = (ImageView) findViewById(R.id.back_imageverified);
        mbackimage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        PollingActivity.this.finish();
    }
}
