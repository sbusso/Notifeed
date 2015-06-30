package com.epitech.hubinnovation.notifeed.adapter;

import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.activity.FeedsFragment;
import com.epitech.hubinnovation.notifeed.activity.NotificationsFragment;
import com.epitech.hubinnovation.notifeed.item.Feed;
import com.epitech.hubinnovation.notifeed.item.Notification;

import java.util.ArrayList;

/**
 * Created by Roro on 03/03/2015.
 */
public class NotificationArrayAdapterItem extends ArrayAdapter<Notification>
{
    /** Layout elements */
    TextView textview_new               = null;
    TextView textview_title 	        = null;
    TextView textview_content 		    = null;

    /** Global vars */
    ArrayList<Notification> _notifsList = null;
    LayoutInflater _inflater            = null;
    Activity _activity                  = null;
    protected ListView mListView        = null;

    /** Item */
    Notification item                   = null;

    public NotificationArrayAdapterItem(Activity activity, ArrayList<Notification> datas, NotificationsFragment fragment, ListView listView)
    {
        super(activity.getApplicationContext(), -1, datas);
        this.mListView      = listView;
        this._notifsList    = datas;
        this._activity      = activity;
        this._inflater      = LayoutInflater.from(activity);
    }

    static class ViewHolder
    {
        protected TextView title;
        protected TextView content;
        protected TextView is_new;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        View view = null;

        // Check is view already exist
        if (convertView == null) {
            view = _inflater.inflate(R.layout.adapter_notif, null);
            if (view == null)
                return (null);

            // Link objects from view
            initDisplay(view);

            // Get item to manage
            item = _notifsList.get(position);

            // Assign values
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.is_new   = (TextView) view.findViewById(R.id.text_new);
            viewHolder.title    = (TextView) view.findViewById(R.id.notif_title);
            viewHolder.content  = (TextView) view.findViewById(R.id.notif_content);

            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        // Set notif datetime
        holder.title.setText(_notifsList.get(position).getDatetimeAsString());

        // Set notif content
        holder.content.setText(_notifsList.get(position).getContent());

        // Set nb new feeds
        if (_notifsList.get(position).isNew() == false)
            holder.is_new.setVisibility(View.GONE);
        else
            holder.is_new.setVisibility(View.VISIBLE);

        return (view);
    }

    void initDisplay(View view)
    {
        // Assign elements from layout
        textview_new        = (TextView) view.findViewById(R.id.text_new);
        textview_title      = (TextView) view.findViewById(R.id.notif_title);
        textview_content    = (TextView) view.findViewById(R.id.notif_content);
    }
}
