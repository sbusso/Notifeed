package com.epitech.hubinnovation.notifeed.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.epitech.hubinnovation.notifeed.R;

public class ServerFragment extends Fragment {

    public static Fragment newInstance(Context context) {
        ServerFragment f = new ServerFragment();

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_server, null);
        setHasOptionsMenu(true);
        return root;
    }

}