package com.epitech.hubinnovation.notifeed.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.Constants;
import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.adapter.FeedArrayAdapterItem;
import com.epitech.hubinnovation.notifeed.item.Feed;
import com.epitech.hubinnovation.notifeed.item.TmpFeedFollow;
import com.epitech.hubinnovation.notifeed.item.TmpFeedName;
import com.epitech.hubinnovation.notifeed.item.User;
import com.epitech.hubinnovation.notifeed.notification.RegistrationIntentService;
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.soap_request.RequestCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class FeedsFragment extends Fragment {
    /**
     * Layout
     */
    RelativeLayout empty_feed = null;
    TextView empty_title = null;
    TextView empty_text = null;
    ListView listview_feeds = null;
    FeedArrayAdapterItem adapter = null;

    /**
     * Local
     */
    ArrayList<Feed> feedsList = null;
    private MainActivity activity = null;

    /**
     * Temporary objects
     */
    ArrayList<TmpFeedName> feedsName = null;
    ArrayList<TmpFeedFollow> feedsFollow = null;
    int feedsNameI = 0;
    int feedsFollowI = 0;

    /**
     * User preferences
     */
    SharedPreferences mPrefs = null;
    ArrayList<Feed> tmpFeedList = null;

    /**
     * Notifications
     */
    private GoogleCloudMessaging gcm                            = null;
    private String device_token                                 = null;
    private String device_id                                    = null;
    private BroadcastReceiver mRegistrationBroadcastReceiver    = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity    = (MainActivity) getActivity();
        feedsName   = new ArrayList<>();
        feedsFollow = new ArrayList<>();
        feedsList   = new ArrayList<>();
        mPrefs      = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        device_id   = Settings.Secure.getString(activity.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(Constants.NOTIFICATION_SENT_TOKEN_TO_SERVER, false);
                if (sentToken)
                {
                    Toast.makeText(activity.getApplicationContext(), "Token OK", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(activity.getApplicationContext(), "Token KO", Toast.LENGTH_LONG).show();
                }
            }
        };



        /** Notif initialisation */
        initNotifRegistration();

        /** User Preferences management */
        retrieveFeedsInUserPrefs();
//        displayFeedsInUserPrefs();
    }

    void initNotifRegistration()
    {
        if (checkPlayServices())
        {
            Intent intent = new Intent(activity, RegistrationIntentService.class);
            activity.startService(intent);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(activity).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Constants.NOTIFICATION_REGISTRATION_COMPLETE));
        if (activity != null)
        {
            activity.getActionbarTitle().setText(getResources().getString(R.string.title_feeds));
        }
    }

    @Override
    public void onPause()
    {
        LocalBroadcastManager.getInstance(activity).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity, Constants.NOTIFICATION_PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Toast.makeText(activity.getApplicationContext(), "Application not supported", Toast.LENGTH_LONG).show();
                activity.finish();
            }
            return false;
        }
        return true;
    }

    private String getRegistrationId(Context context)
    {
        final SharedPreferences prefs = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(Constants.PREFERENCES_PROPERTY_DEVICE_TOKEN, null);
        if (registrationId != null && registrationId.isEmpty()) {
            Toast.makeText(activity.getApplicationContext(), "Registration ID not found.", Toast.LENGTH_LONG).show();
            return "";
        }
//        int registeredVersion = prefs.getInt(Constants.PREFERENCES_PROPERTY_APP_VERSION, Integer.MIN_VALUE);
//        int currentVersion = getAppVersion(context);
//        if (registeredVersion != currentVersion) {
//            Toast.makeText(activity.getApplicationContext(), "App version changed.", Toast.LENGTH_LONG).show();
//            return "";
//        }
        return registrationId;
    }

    private static int getAppVersion(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground()
    {     new AsyncTask() {
        @Override
        protected Object doInBackground(Object... params)
        {
            String msg = "";
            try
            {
                if (gcm == null)
                {
                    gcm = GoogleCloudMessaging.getInstance(activity.getApplicationContext());
                }
                InstanceID instanceID = InstanceID.getInstance(activity);
                device_token = instanceID.getToken(Constants.NOTIFICATION_SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);


                //    Toast.makeText(activity.getApplicationContext(), "Current Device's Registration ID is: " + msg, Toast.LENGTH_LONG).show();
            }
            catch (IOException ex)
            {
                msg = "Error :" + ex.getMessage();
            }
            return null;
        }

        protected void onPostExecute(Object result)
        {
            SharedPreferences.Editor editor = activity.getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).edit();
            editor.putString(Constants.PREFERENCES_PROPERTY_DEVICE_TOKEN, device_token);
            editor.commit();
            registerDeviceOnServer();
        };

    }.execute(null, null, null);
    }

    private void retrieveFeedsInUserPrefs()
    {
        Gson gson = new Gson();
        String tmpJsonList = mPrefs.getString(Constants.PREFERENCES_FEEDS_LIST, null);
        if (tmpJsonList == null)
            return;
        Type listType = new TypeToken<ArrayList<Feed>>() { }.getType();
        tmpFeedList = gson.fromJson(tmpJsonList, listType);
    }

    private void saveFeedsInUserPrefs()
    {
        SharedPreferences.Editor editor = mPrefs.edit();
        String feedsJSONString = new Gson().toJson(feedsList);
        editor.putString(Constants.PREFERENCES_FEEDS_LIST, feedsJSONString);
        editor.commit();
    }

    private void displayFeedsInUserPrefs()
    {
        computeInterface(tmpFeedList);
    }

    private void registerDeviceOnServer()
    {
        /** Callback initialisation */
        RequestCallback.FragmentCallback callback = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                register_device_callback(result);
            }
        };

        /** Register device for notifications */
        Request.getInstance().launch_request("register_device", callback, device_token, Constants.DEVICE_TYPE, Constants.NOTIFICATION_APP_KEY, User.getInstance().getAccKey());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /** Init fragment view */
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_feeds, null);
        setHasOptionsMenu(true);

        /** Init Feed interface */
        initInterface(root);

        /** Display saves feeds */
        displayFeedsInUserPrefs();
//        if (tmpFeedList != null && tmpFeedList.size() > 0)
//            listview_feeds.setVisibility(View.VISIBLE);

        /** Get Feed list */
        RequestCallback.FragmentCallback callback = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                list_feed_callback(result);
            }
        };
        activity.showLoadingBar();
        Request.getInstance().launch_request("list_feed", callback, User.getInstance().getAccKey());

        return root;
    }

    public void register_device_callback(Object result)
    {
        //Boolean answer  = (Boolean)result;
        String answer = result.toString();

        if (answer != null)// && answer.booleanValue() == false)
            Toast.makeText(activity.getApplicationContext(), "Failed to register_device", Toast.LENGTH_LONG).show();
    }

    private void list_feed_callback(Object result)
    {
        String[] list_feed  = (String[])result;

        /** Get Feed */
        RequestCallback.FragmentCallback callback_get_feed = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                get_feed_callback(result);
            }
        };

        /** Is following feed */
        RequestCallback.FragmentCallback callback_is_following_feed = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                is_following_feed_callback(result);
            }
        };

        if (list_feed != null)
        {
            for (String tmp : list_feed)
            {
                feedsList.add(new Feed(false, tmp, 0, tmp));
                Request.getInstance().launch_request("get_feed", callback_get_feed, User.getInstance().getAccKey(), tmp);
                Request.getInstance().launch_request("is_following_feed", callback_is_following_feed, User.getInstance().getAccKey(), tmp);
            }
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
            activity.hideLoadingBar();
        }

        /** Compute infos */
        computeInterface(feedsList);
    }

    private void get_feed_callback(Object result)
    {
        String[] feed   = (String[]) result;
        String id       = feed[0];
        String name     = feed[1];

        /** Request done */
        ++feedsNameI;

        /** Add name to list */
        if (feed != null && feed[0] != null && !feed[0].equals("") && feed[1] != null)
        {
            for (int i = 0 ; i < feedsList.size() ; ++i)
            {
                Feed tmp = feedsList.get(i);
                if (id != null && tmp != null && tmp.getName().equals(id))
                {
                    feedsName.add(new TmpFeedName(i, id, name));
                    break;
                }
            }
        }
        else
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();

        /** Check all feed refresh */
        if (feedsList.size() == feedsNameI)
            adapter.refreshFeedName(feedsName);
        checkIfAllRequestsDone();
    }

    private void is_following_feed_callback(Object result)
    {
        Object[] feed       = (Object[]) result;
        String id           = null;
        Boolean subscribed  = false;

        /** Request done */
        ++feedsFollowI;

        /** Parse request result */
        if (feed != null && feed[0] != null && !feed[0].equals(""))
            id = feed[0].toString();
        if (feed != null && feed[1] != null)
            subscribed = Boolean.parseBoolean(feed[1].toString());

        /** Add follow status to list */
        if (id != null && subscribed != null)
        {
            for (int i = 0; i < feedsList.size(); ++i)
            {
                Feed tmp = feedsList.get(i);
                if (id != null && tmp != null && tmp.getName().equals(id))
                {
                    feedsFollow.add(new TmpFeedFollow(i, subscribed, id));
                    break;
                }
            }
        }
        else
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();

        /** Check all feed refresh */
        if (feedsList.size() == feedsFollowI)
            adapter.refreshIsFollowingFeed(feedsFollow);
        checkIfAllRequestsDone();
    }

    private void checkIfAllRequestsDone()
    {
        if (feedsList.size() == feedsFollowI && feedsList.size() == feedsNameI)
        {
//            listview_feeds.setVisibility(View.VISIBLE);
            activity.hideLoadingBar();
            saveFeedsInUserPrefs();
        }
    }

    private void initInterface(ViewGroup root)
    {
        empty_feed      = (RelativeLayout) root.findViewById(R.id.empty_feed);
        empty_text      = (TextView) root.findViewById(R.id.empty_feed_text);
        empty_title     = (TextView) root.findViewById(R.id.empty_notif_title);
        listview_feeds  = (ListView) root.findViewById(R.id.list_feeds);
    }

    private void computeInterface(ArrayList<Feed> list)
    {
        if (list != null && list.size() > 0)
        {
            empty_feed.setVisibility(View.INVISIBLE);
            listview_feeds.setVisibility(View.VISIBLE);

            adapter = new FeedArrayAdapterItem(activity, list, this, listview_feeds);
            listview_feeds.setAdapter(adapter);
        }
        else
        {
            empty_title.setText(getResources().getString(R.string.empty_feedlist_title));
            empty_text.setText(getResources().getString(R.string.empty_feedlist_text));

            empty_feed.setVisibility(View.VISIBLE);
            listview_feeds.setVisibility(View.INVISIBLE);
        }
    }
}