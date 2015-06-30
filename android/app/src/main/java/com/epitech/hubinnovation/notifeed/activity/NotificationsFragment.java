package com.epitech.hubinnovation.notifeed.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.Constants;
import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.adapter.NotificationArrayAdapterItem;
import com.epitech.hubinnovation.notifeed.item.Feed;
import com.epitech.hubinnovation.notifeed.item.Notification;
import com.epitech.hubinnovation.notifeed.item.User;
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.soap_request.RequestCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class NotificationsFragment extends Fragment
{
    /** Layout */
    RelativeLayout empty_notif                              = null;
    TextView empty_title                                    = null;
    TextView empty_text                                     = null;
    ListView listview_notifs	                            = null;

    /** Local */
    String feed_id                                          = null;
    String feed_name                                        = null;
    String notif_id                                         = null;
    private MainActivity activity                           = null;
    ArrayList<Notification> notifsList                      = null;
    NotificationArrayAdapterItem adapter                    = null;
    Dialog dialog                                           = null;
    RequestCallback.FragmentCallback callback_comment       = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activity = (MainActivity)getActivity();

        callback_comment = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                comment_callback(result);
            }
        };

        /* Get Fragment title */
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            String title = bundle.getString(Constants.BUNDLE_DATA_FEED_NAME);
            activity.getActionbarTitle().setText(title);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        /** Init fragment view */
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_notifications, null);
        setHasOptionsMenu(true);

        /** Init display */
        initInterface(root);

        /** Get Notif list */
        RequestCallback.FragmentCallback callback = new RequestCallback.FragmentCallback()
        {
            @Override
            public void onTaskDone(Object result)
            {
                list_notif_callback(result);
            }
        };
        activity.showLoadingBar();
        Request.getInstance().launch_request("get_history", callback, User.getInstance().getAccKey(), feed_id);

        /** Init alert dialog */
        openNotification();

        return root;
    }

    private void list_notif_callback(Object result)
    {
        List<Map<String, String>> list_notif    = (List<Map<String, String>>)result;
        notifsList                              = new ArrayList<>();

        if (list_notif != null)
        {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map<String, String> tmp : list_notif)
            {
                Calendar date = Calendar.getInstance();
                try
                {
                    date.setTime(formatter.parse(tmp.get(Constants.MAP_KEY_NOTIF_DATE)));
                }
                catch (ParseException e)
                {
                    e.printStackTrace();
                }
                notifsList.add(new Notification(true, date, tmp.get(Constants.MAP_KEY_NOTIF_CONTENT), tmp.get(Constants.MAP_KEY_NOTIF_ID)));
            }
        }
        else
        {
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_timeout), Toast.LENGTH_LONG).show();
        }

        /** Compute infos */
        computeInterface();

        activity.hideLoadingBar();
    }

    private void initInterface(ViewGroup root)
    {
        listview_notifs = (ListView) root.findViewById(R.id.list_notifs);
        empty_notif     = (RelativeLayout) root.findViewById(R.id.empty_notif);
        empty_title     = (TextView) root.findViewById(R.id.empty_notif_title);
        empty_text      = (TextView) root.findViewById(R.id.empty_notif_text);
        feed_name       = getArguments().getString(Constants.BUNDLE_DATA_FEED_NAME);
        feed_id         = getArguments().getString(Constants.BUNDLE_DATA_FEED_ID);
    }

    private void openNotification()
    {
        listview_notifs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final Notification tmp_notification = notifsList.get(position);

                /** Custom dialog */
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_alertdialog);
                dialog.setTitle(tmp_notification.getDatetimeAsString());

                /** Set notif_id */
                notif_id = notifsList.get(position).getId();

                /** Set the custom dialog components */
                TextView text = (TextView) dialog.findViewById(R.id.dialog_content);
                text.setText(tmp_notification.getContent());

                Button dialog_button_ok = (Button) dialog.findViewById(R.id.dialog_btn_ok);
                dialog_button_ok.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Request.getInstance().launch_request("comment", null, User.getInstance().getAccKey(), notif_id, null);
                        dialog.dismiss();
                    }
                });
                Button dialog_button_answer = (Button) dialog.findViewById(R.id.dialog_btn_answer);
                dialog_button_answer.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        openAnswerDialogBox(tmp_notification.getDatetimeAsString());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void computeInterface()
    {
        if (notifsList != null && notifsList.size() > 0)
        {
            empty_notif.setVisibility(View.INVISIBLE);
            listview_notifs.setVisibility(View.VISIBLE);

            adapter = new NotificationArrayAdapterItem(getActivity(), notifsList, this, listview_notifs);
            listview_notifs.setAdapter(adapter);
        }
        else
        {
            empty_title.setText(getResources().getString(R.string.empty_notif_title));
            empty_text.setText(getResources().getString(R.string.empty_notif_text));

            empty_notif.setVisibility(View.VISIBLE);
            listview_notifs.setVisibility(View.INVISIBLE);
        }
    }

    private void openAnswerDialogBox(String title)
    {
        /** Custom dialog */
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_answerdialog);
        dialog.setTitle(title);

        /** Set the custom dialog components */
        final EditText feedback     = (EditText) dialog.findViewById(R.id.dialog_text_answer);
        final TextView char_counter = (TextView) dialog.findViewById(R.id.dialog_nb_char);
        Button dialog_button_ok     = (Button) dialog.findViewById(R.id.dialog_btn_ok);
        Button dialog_button_cancel = (Button) dialog.findViewById(R.id.dialog_btn_cancel);

        char_counter.setText(String.valueOf(Constants.TWEET_LENGTH));
        feedback.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                int length_remaining = Constants.TWEET_LENGTH - feedback.getText().toString().length();

                char_counter.setText(String.valueOf(length_remaining));
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        dialog_button_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                activity.showLoadingBar();
                Request.getInstance().launch_request("comment", callback_comment, User.getInstance().getAccKey(), notif_id, feedback.getText().toString());
            }
        });
        dialog_button_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Request.getInstance().launch_request("comment", null, User.getInstance().getAccKey(), notif_id, null);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void comment_callback(Object result)
    {
        boolean success = (boolean)result;
        String msg;

        if (success)
        {
            msg = activity.getString(R.string.success_feedback);
        }
        else
        {
            msg = activity.getString(R.string.error_feedback);
        }
        Toast.makeText(activity.getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        dialog.dismiss();
        activity.hideLoadingBar();
    }
}