package com.epitech.hubinnovation.notifeed.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.soap_request.RequestCallback;

import java.util.ArrayList;

public class FeedsFragment extends Fragment
{
    /** Layout */
    RelativeLayout empty_feed       = null;
    TextView empty_title            = null;
    TextView empty_text             = null;
    ListView listview_feeds	        = null;
    FeedArrayAdapterItem adapter    = null;

    /** Local */
    ArrayList<Feed> feedsList       = null;
    private MainActivity activity   = null;

    /** Temporary objects */
    ArrayList<TmpFeedName> feedsName        = null;
    ArrayList<TmpFeedFollow> feedsFollow    = null;
    int feedsNameI                          = 0;
    int feedsFollowI                        = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity    = (MainActivity)getActivity();
        feedsName   = new ArrayList<>();
        feedsFollow = new ArrayList<>();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (activity != null)
            activity.getActionbarTitle().setText(getResources().getString(R.string.title_feeds));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /** Init fragment view */
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_feeds, null);
        setHasOptionsMenu(true);

        /** Init Feed interface */
        initInterface(root);

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

    private void list_feed_callback(Object result)
    {
        String[] list_feed  = (String[])result;
        feedsList           = new ArrayList<>();

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
        computeInterface();
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
            listview_feeds.setVisibility(View.VISIBLE);
            activity.hideLoadingBar();
        }
    }

    private void initInterface(ViewGroup root)
    {
        empty_feed      = (RelativeLayout) root.findViewById(R.id.empty_feed);
        empty_text      = (TextView) root.findViewById(R.id.empty_feed_text);
        empty_title     = (TextView) root.findViewById(R.id.empty_notif_title);
        listview_feeds  = (ListView) root.findViewById(R.id.list_feeds);
    }

    private void computeInterface()
    {
        if (feedsList != null && feedsList.size() > 0)
        {
            empty_feed.setVisibility(View.INVISIBLE);
            listview_feeds.setVisibility(View.INVISIBLE);

            adapter = new FeedArrayAdapterItem(activity, feedsList, this, listview_feeds);
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