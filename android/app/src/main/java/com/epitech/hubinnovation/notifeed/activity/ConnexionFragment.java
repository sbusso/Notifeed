package com.epitech.hubinnovation.notifeed.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.item.User;
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.soap_request.RequestCallback;

import java.util.ArrayList;

public class ConnexionFragment extends Fragment
{
    /** Interface */
    private Button connect          = null;
    private TextView public_id      = null;
    private TextView private_id     = null;

    /** Local */
    private MainActivity activity   = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_connexion, null);
        setHasOptionsMenu(true);

        /** Items initialisation */
        initInterface(root);

        /** Items behaviour */
        computeInterface();



        RequestCallback.FragmentCallback callback = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                register_account_callback(result);
            }
        };
        activity.showLoadingBar();
        Request.getInstance().launch_request("register_account", callback, "assere_a", "cT,Jz6gs");

        return root;
    }

    void initInterface(View view)
    {
        connect     = (Button)view.findViewById(R.id.connect_ok);
        public_id   = (TextView)view.findViewById(R.id.connect_login);
        private_id  = (TextView)view.findViewById(R.id.connect_password);
    }

    void computeInterface()
    {
        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RequestCallback.FragmentCallback callback = new RequestCallback.FragmentCallback()
                {
                    @Override
                    public void onTaskDone(Object result)
                    {
                        register_account_callback(result);
                    }
                };
                activity.showLoadingBar();
                Request.getInstance().launch_request("register_account", callback, public_id.getText().toString(), private_id.getText().toString());

            }
        });
    }

    private void register_account_callback(Object result)
    {
        String acc_key = null;

        if (result != null)
            acc_key = result.toString();
        if (acc_key != null && !acc_key.equals(""))
        {
            /** Refresh User singleton */
            User.getInstance().setAccKey(acc_key);

            /** Launch Feed fragment */
            activity.emptyBackStack();
            FeedsFragment tmp_0 = new FeedsFragment();
            FragmentTransaction tx_0 = activity.getSupportFragmentManager().beginTransaction();
            tx_0.replace(R.id.main, tmp_0);
            tx_0.commit();
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
        }
        activity.hideLoadingBar();
    }
}