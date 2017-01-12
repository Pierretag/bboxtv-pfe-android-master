package fr.lab.bbox.bboxapirunner.Notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import fr.bouyguestelecom.bboxapi.bboxapi.Bbox;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxGetOpenedChannels;
import fr.bouyguestelecom.bboxapi.bboxapi.callback.IBboxUnsubscribe;
import fr.lab.bbox.bboxapirunner.R;
import okhttp3.Request;

/**
 * Created by dinh on 01/07/16.
 */

// TODO : create a button to unsubscribe to all channels
public class NotificationUnsubscribeFragment extends Fragment {

    private Button mButton;
    private Button mButtonBis;
    private Context ctxt;
    private Handler handler;

    private EditText channelIdEdit;
    private String channelId = " ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification_unsubscribe, container, false);

        mButtonBis = (Button) view.findViewById(R.id.try_unsubscribeall);
        mButtonBis.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(final View v) {

               final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
               final String ip = sharedPref.getString("bboxip", "");

               Bbox.getInstance().getOpenedChannels(ip,
                       getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                       getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                       new IBboxGetOpenedChannels() {

                           @Override
                           public void onResponse(final List<String> channels) {
                               for(final String str:channels)
                               {
                                   Bbox.getInstance().unsubscribeNotification(ip,
                                           getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                                           getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                                           str,
                                           new IBboxUnsubscribe() {
                                               @Override
                                               public void onUnsubscribe() {
                                                   handler.post(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           Toast.makeText(ctxt, "Channels being removed", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });

                                                   String appId = str.substring(0, str.lastIndexOf("-"));
                                                   Log.d("notif", "appId : " + appId);

                                                   Bbox.getInstance().removeMediaListener(ip, appId, str);
                                                   Bbox.getInstance().removeAppListener(ip, appId, str);
                                                   Bbox.getInstance().removeMsgListener(ip, appId, str);
                                               }

                                               @Override
                                               public void onFailure(Request request, int errorCode) {
                                                   handler.post(new Runnable() {
                                                       @Override
                                                       public void run() {
                                                           Toast.makeText(ctxt, "unsubscribe failed", Toast.LENGTH_SHORT).show();
                                                       }
                                                   });
                                               }
                                           });
                               }

                           }

                           @Override
                           public void onFailure(Request request, int errorCode) {
                           }
                       });

           }
        });

        mButton = (Button) view.findViewById(R.id.try_unsubscribe);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                final String ip = sharedPref.getString("bboxip", "");
                channelId = channelIdEdit.getText().toString();

                Bbox.getInstance().unsubscribeNotification(ip,
                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_ID),
                        getResources().getString(fr.bouyguestelecom.bboxapi.R.string.APP_SECRET),
                        channelId,
                        new IBboxUnsubscribe() {
                            @Override
                            public void onUnsubscribe() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, channelId + " removed", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                String appId = channelId.substring(0, channelId.lastIndexOf("-"));
                                Log.d("notif", "appId : " + appId);

                                Bbox.getInstance().removeMediaListener(ip, appId, channelId);
                                Bbox.getInstance().removeAppListener(ip, appId, channelId);
                                Bbox.getInstance().removeMsgListener(ip, appId, channelId);
                            }

                            @Override
                            public void onFailure(Request request, int errorCode) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(ctxt, "unsubscribe failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
            }
        });

        ctxt = getActivity().getApplicationContext();
        handler = new Handler();

        channelIdEdit = (EditText) view.findViewById(R.id.notification_channelid);

        return view;
    }

}