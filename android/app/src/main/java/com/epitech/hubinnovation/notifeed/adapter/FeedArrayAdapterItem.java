package com.epitech.hubinnovation.notifeed.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.Constants;
import com.epitech.hubinnovation.notifeed.activity.FeedsFragment;
import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.activity.MainActivity;
import com.epitech.hubinnovation.notifeed.activity.NotificationsFragment;
import com.epitech.hubinnovation.notifeed.item.Feed;
import com.epitech.hubinnovation.notifeed.item.User;
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.soap_request.RequestCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roro on 03/03/2015.
 */
public class FeedArrayAdapterItem extends ArrayAdapter<Feed>
{
    /** Layout elements */
    TextView textview_name 	    	    = null;
    TextView textview_nb_news 		    = null;
    Button button_subscribe             = null;
    Button button_feed_listener         = null;

    /** Global vars */
    MainActivity _activity              = null;
    Fragment _fragment                  = null;
    LayoutInflater _inflater            = null;
    ArrayList<Feed> _feedsList 		    = null;
    protected ListView mListView        = null;

    /** Callbacks */
    RequestCallback.FragmentCallback callback_follow    = null;
    RequestCallback.FragmentCallback callback_unfollow  = null;

    /** Item */
    Feed item                           = null;

    public FeedArrayAdapterItem(Activity activity, ArrayList<Feed> datas, FeedsFragment fragment, ListView listView)
    {
        super(activity.getApplicationContext(), -1, datas);
        this.mListView      = listView;
        this._feedsList     = datas;
        this._fragment      = fragment;
        this._activity      = (MainActivity)activity;
        this._inflater      = LayoutInflater.from(activity);
    }

    static class ViewHolder
    {
        protected TextView name;
        protected TextView number;
        protected Button subscribe;
        protected Button feed_listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = null;

        /** Check is view already exist */
        if (convertView == null) {
            view = _inflater.inflate(R.layout.adapter_feed, null);
            if (view == null)
                return (null);

            /** Link objects from view */
            initDisplay(view);

            /** Get item to manage */
            item = _feedsList.get(position);

            /** Assign values */
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.feed_listener    = (Button) view.findViewById(R.id.feed_listener);
            viewHolder.subscribe        = (Button) view.findViewById(R.id.feed_subscribe);
            viewHolder.number           = (TextView) view.findViewById(R.id.feed_number);
            viewHolder.name             = (TextView) view.findViewById(R.id.feed_name);

            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        /** Set callbacks */
        callback_follow = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                follow_feed_callback(result);
            }
        };
        callback_unfollow = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                unfollow_feed_callback(result);
            }
        };

        /** Set feed name */
        holder.name.setText(_feedsList.get(position).getName());

        /** Set clickListener */
        holder.subscribe.setOnClickListener(mOnCheckboxClickListener);
        holder.feed_listener.setOnClickListener(mOnFeedClickListener);

        /** Set Checkbox background */
        if (_feedsList.get(position).isSubscribed() == false)
        {
            holder.subscribe.setBackgroundResource(R.drawable.checkbox_off);
            button_subscribe.setBackgroundResource(R.drawable.checkbox_off);
        }
        else
        {
            holder.subscribe.setBackgroundResource(R.drawable.checkbox_on);
            button_subscribe.setBackgroundResource(R.drawable.checkbox_on);
        }

        /** Set nb new feeds */
        String nb_new_notif;
        if (_feedsList.get(position).getNb_new_notification() > 9)
            nb_new_notif = "9+";
        else
            nb_new_notif = String.valueOf(_feedsList.get(position).getNb_new_notification());
        holder.number.setText(nb_new_notif);
        if (_feedsList.get(position).getNb_new_notification() == 0)
            textview_nb_news.setVisibility(View.INVISIBLE);
        else
            textview_nb_news.setText(nb_new_notif);

        return (view);
    }

    public void refreshFeedName(String id, String name)
    {
        for (int i = 0 ; i < _feedsList.size() ; ++i)
        {
            Feed tmp = _feedsList.get(i);
            if (id != null && tmp != null && tmp.getName().equals(id))
            {
                tmp.setName(name);
                this.notifyDataSetChanged();
                break;
            }
        }
    }

    public void refreshIsFollowingFeed(String id, boolean follow)
    {
        for (int i = 0 ; i < _feedsList.size() ; ++i)
        {
            Feed tmp = _feedsList.get(i);
            if (id != null && tmp != null && tmp.getId().equals(id))
            {
                tmp.setSubscribed(follow);
                if (button_subscribe != null && tmp.isSubscribed() == false)
                    button_subscribe.setBackgroundResource(R.drawable.checkbox_off);
                else if (button_subscribe != null)
                    button_subscribe.setBackgroundResource(R.drawable.checkbox_on);
                this.notifyDataSetChanged();
                break;
            }
        }
    }

    private View.OnClickListener mOnCheckboxClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            final int position = mListView.getPositionForView((View) v.getParent());
            boolean new_value = !_feedsList.get(position).isSubscribed();
            _feedsList.get(position).setSubscribed(new_value);

            if (v == null)
                return;
            Button subscribe_btn = (Button) v.findViewById(R.id.feed_subscribe);
            if (new_value == false)
            {
                _activity.showLoadingBar();
                Request.getInstance().launch_request("unfollow_feed", callback_unfollow, User.getInstance().getAccKey(), _feedsList.get(position).getId());
                subscribe_btn.setBackgroundResource(R.drawable.checkbox_off);
            }
            else
            {
                _activity.showLoadingBar();
                Request.getInstance().launch_request("follow_feed", callback_follow, User.getInstance().getAccKey(), _feedsList.get(position).getId());
                subscribe_btn.setBackgroundResource(R.drawable.checkbox_on);
            }
        }
    };

    private View.OnClickListener mOnFeedClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            final int position = mListView.getPositionForView((View) v.getParent());
            NotificationsFragment tmp = new NotificationsFragment();
            FragmentTransaction tx = _fragment.getFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putString(Constants.BUNDLE_DATA_FEED_NAME, _feedsList.get(position).getName());
            bundle.putString(Constants.BUNDLE_DATA_FEED_ID, _feedsList.get(position).getId());
            tmp.setArguments(bundle);

            tx.replace(R.id.main, tmp);
            tx.addToBackStack(null);
            tx.commit();
        }
    };

    private void follow_feed_callback(Object result)
    {
        boolean success = (boolean)result;

        if (!success)
            Toast.makeText(_activity.getApplicationContext(), _activity.getString(R.string.error_follow), Toast.LENGTH_LONG).show();
        _activity.hideLoadingBar();
    }

    private void unfollow_feed_callback(Object result)
    {
        boolean success = (boolean)result;

        if (!success)
            Toast.makeText(_activity.getApplicationContext(), _activity.getString(R.string.error_unfollow), Toast.LENGTH_LONG).show();
        _activity.hideLoadingBar();
    }

    void initDisplay(View view)
    {
        /** Assign elements from layout */
        button_subscribe        = (Button) view.findViewById(R.id.feed_subscribe);
        button_feed_listener    = (Button) view.findViewById(R.id.feed_listener);
        textview_name		    = (TextView) view.findViewById(R.id.feed_name);
        textview_nb_news	    = (TextView) view.findViewById(R.id.feed_number);
    }
}
