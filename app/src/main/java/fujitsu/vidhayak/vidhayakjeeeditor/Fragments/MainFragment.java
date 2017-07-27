package fujitsu.vidhayak.vidhayakjeeeditor.Fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.ApprovedStory;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.CompletedRequest;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.DisapprovedStory;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.InprogressRequest;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.PendingRequest;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.PendingStory;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.PollingActivity;
import fujitsu.vidhayak.vidhayakjeeeditor.InternalActivity.UpdateLocation;
import fujitsu.vidhayak.vidhayakjeeeditor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    Button mpendrequestbtn, minprogressrequestbtn, mcompleterequestbtn, mpendingstorybtn, mapprovedstorybtn,
            mdisapprovedstorybtn, mupdatelocation_btn, mcreatepollingbtn;
    int i = 0;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mpendrequestbtn = (Button) view.findViewById(R.id.pendrequest_btn);
        minprogressrequestbtn = (Button) view.findViewById(R.id.inprogressrequest_btn);
        mcompleterequestbtn = (Button) view.findViewById(R.id.completedrequest_btn);
        mpendingstorybtn = (Button) view.findViewById(R.id.pendstory_btn);
        mapprovedstorybtn = (Button) view.findViewById(R.id.approvedstory_btn);
        mdisapprovedstorybtn = (Button) view.findViewById(R.id.disapprovedstory_btn);
        mcreatepollingbtn = (Button) view.findViewById(R.id.polling_btn);
        mupdatelocation_btn = (Button) view.findViewById(R.id.updatelocation_btn);


        mpendrequestbtn.setOnClickListener(this);
        minprogressrequestbtn.setOnClickListener(this);
        mcompleterequestbtn.setOnClickListener(this);
        mpendingstorybtn.setOnClickListener(this);
        mapprovedstorybtn.setOnClickListener(this);
        mdisapprovedstorybtn.setOnClickListener(this);
        mcreatepollingbtn.setOnClickListener(this);
        mupdatelocation_btn.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.updatelocation_btn:

                startActivity(new Intent(getContext(), UpdateLocation.class));

                break;

            case R.id.pendrequest_btn:

                startActivity(new Intent(getContext(), PendingRequest.class));

                break;
            case R.id.inprogressrequest_btn:

                startActivity(new Intent(getContext(), InprogressRequest.class));

                break;

            case R.id.completedrequest_btn:

                startActivity(new Intent(getContext(), CompletedRequest.class));

                break;

            case R.id.pendstory_btn:

                startActivity(new Intent(getContext(), PendingStory.class));

                break;

            case R.id.approvedstory_btn:

                startActivity(new Intent(getContext(), ApprovedStory.class));

                break;

            case R.id.disapprovedstory_btn:

                startActivity(new Intent(getContext(), DisapprovedStory.class));

                break;

            case R.id.polling_btn:

                startActivity(new Intent(getContext(), PollingActivity.class));

                break;

        }
    }
}
