package fr.lab.bbox.bboxapirunner.Notification;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import fr.lab.bbox.bboxapirunner.R;

/**
 * Created by dinh on 01/07/16.
 */
public class NotificationFragment extends Fragment {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_notification, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_notif);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                switch (item.toString()) {
                    case "Subscribe to resources":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSubscribeFragment()).commit();
                        break;
                    case "Unsubscribe":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_notif_parameter, new NotificationUnsubscribeFragment()).commit();
                        break;
                    case "Get opened channels":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_notif_parameter, new NotificationGetOpenedChannelsFragment()).commit();
                        break;
                    case "Send message to room":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSendMsgToRoomFragment()).commit();
                        break;
                    case "Send message to a specific channelId":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_notif_parameter, new NotificationSendMsgToChannelIdFragment()).commit();
                        break;
                    default:
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }


}