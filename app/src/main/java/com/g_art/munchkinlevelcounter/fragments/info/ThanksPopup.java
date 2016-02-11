package com.g_art.munchkinlevelcounter.fragments.info;

import android.app.Activity;
import android.app.Fragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.g_art.munchkinlevelcounter.R;
import com.g_art.munchkinlevelcounter.activity.About;
import com.g_art.munchkinlevelcounter.activity.InfoActivity;
import com.g_art.munchkinlevelcounter.util.SettingsHandler;

/**
 * LevelCounter
 * Created by G_Art on 1/12/2015.
 */
public class ThanksPopup extends Fragment implements View.OnClickListener {

    private InfoActivity mCallback;
    private ImageButton imgBtnRatePopup;
    private ImageButton imgBtnSendScreen;
    private Button btnMore;
    private Button btnLater;
    private Button btnNeverShow;
    /**
     * The system calls this to get the DialogFragment's layout, regardless
     * of whether it's being displayed as a dialog or an embedded fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_tanks_popup, container, false);
        imgBtnRatePopup = (ImageButton) view.findViewById(R.id.imgBtnRatePopup);
        imgBtnRatePopup.setOnClickListener(this);
        imgBtnSendScreen = (ImageButton) view.findViewById(R.id.imgBtnSendScreen);
        imgBtnSendScreen.setOnClickListener(this);
        btnMore = (Button) view.findViewById(R.id.btnMore);
        btnMore.setOnClickListener(this);
        btnLater = (Button) view.findViewById(R.id.btnLater);
        btnLater.setOnClickListener(this);
        btnNeverShow = (Button) view.findViewById(R.id.btnNeverShow);
        btnNeverShow.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (InfoActivity) activity;
            mCallback.updateStatus(SettingsHandler.ASK_LATER);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtnRatePopup:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                //Try Google Play
                intent.setData(Uri.parse("market://details?id=com.g_art.munchkinlevelcounter"));
                if (!mayStartActivity(intent)) {
                    //Market (Google play) app seems not installed, let's try to open a webbrowser
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.g_art.munchkinlevelcounter"));
                    if (!mayStartActivity(intent)) {
                        //Well if this also fails, we have run out of options, inform the user.
                        Toast.makeText(mCallback, "Could not open Android market, please install the market app.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.imgBtnSendScreen:
                Intent Email = new Intent(Intent.ACTION_SEND);
                Email.setType("message/rfc822");
                Email.putExtra(Intent.EXTRA_EMAIL, new String[]{"android.dev.g.art@gmail.com"});
                Email.putExtra(Intent.EXTRA_SUBJECT, "Rate Screenshot");
                Email.putExtra(Intent.EXTRA_TEXT, "Screenshot:");
                startActivity(Intent.createChooser(Email, "Choose your Email App:"));
                break;
            case R.id.btnMore:
                Intent aboutIntent = new Intent(getActivity(), About.class);
                startActivity(aboutIntent);
                break;
            case R.id.btnLater:
                mCallback.updateStatus(SettingsHandler.ASK_LATER);
                getActivity().finish();
                break;
            case R.id.btnNeverShow:
                mCallback.updateStatus(SettingsHandler.NEVER_ASK);
                getActivity().finish();
                break;
        }
    }

    private boolean mayStartActivity(Intent intent) {
        try {
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    // interface for communication between fragment and activity
    public interface PopupStatusUpdate {
        boolean updateStatus(int updateStatus);
    }
}
