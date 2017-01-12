package fr.lab.bbox.bboxapirunner;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by dinh on 01/07/16.
 */
public class BboxFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_bbox, container, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");

        TextView currentip = (TextView) view.findViewById(R.id.currentip);
        currentip.setText(ip);

        return view;
    }

}