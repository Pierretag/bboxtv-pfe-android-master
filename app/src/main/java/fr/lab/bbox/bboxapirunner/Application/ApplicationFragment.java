package fr.lab.bbox.bboxapirunner.Application;

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
public class ApplicationFragment extends Fragment {

    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_application, container, false);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_app);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                switch (item.toString()) {
                    case "Get application info":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppInfoFragment()).commit();
                        break;
                    case "Get application list":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppListFragment()).commit();
                        break;
                    case "Get application icon":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationGetAppIconFragment()).commit();
                        break;
                    case "Start application":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationStartAppFragment()).commit();
                        break;
                    case "Stop application":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationStopAppFragment()).commit();
                        break;
                    case "Register (for notification)":
                        mFragmentManager = getFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.addToBackStack(null);
                        mFragmentTransaction.replace(R.id.container_app_parameter, new ApplicationRegisterFragment()).commit();
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