package fr.lab.bbox.bboxapirunner.Application;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxStartApplication;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */
public class ApplicationStartAppFragment extends Fragment implements View.OnClickListener {

    private Button mButton;
    private Context ctxt;
    private Handler handler;
    private EditText packageNameEdit;
    private String packageName = " ";
    private EditText deeplinkEdit;
    private String deeplink = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_app_start, container, false);

        mButton = (Button) view.findViewById(R.id.try_startapp);
        mButton.setOnClickListener(this);
        packageNameEdit = (EditText) view.findViewById(R.id.app_start_packagename);
        deeplinkEdit = (EditText) view.findViewById(R.id.app_start_deeplink);

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        return view;
    }


    @Override
    public void onClick(final View v) {

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
        String ip = sharedPref.getString("bboxip", "");
        packageName = packageNameEdit.getText().toString();
        deeplink = deeplinkEdit.getText().toString();

        if(deeplink.isEmpty())
        {
            Bbox.getInstance().startApp(ip,
                    getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                    getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                    packageName,
                    new IBboxStartApplication() {

                        @Override
                        public void onResponse() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ctxt, "Start app success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, int errorCode) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ctxt, "Start app failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }
        else{
            Bbox.getInstance().startApp(ip,
                    getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                    getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                    packageName,
                    deeplink,
                    new IBboxStartApplication() {

                        @Override
                        public void onResponse() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ctxt, "Start app success", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Request request, int errorCode) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ctxt, "Start app failed", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
        }

    }

}