package com.epitech.hubinnovation.notifeed.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.epitech.hubinnovation.notifeed.Constants;
import com.epitech.hubinnovation.notifeed.R;
import com.epitech.hubinnovation.notifeed.item.User;
import com.epitech.hubinnovation.notifeed.soap_request.Request;
import com.epitech.hubinnovation.notifeed.tool.Tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends ActionBarActivity
{
    public static final int MENU_FEEDS      = 0;
    public static final int MENU_DISCONNECT = 1;

    ImageButton actionbarBtn                = null;
    TextView actionbarTitle                 = null;
    ProgressBar loading_bar                 = null;
    static String[] menu_data               = null;

    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        menu_data                       = getResources().getStringArray(R.array.menu_title);
		ArrayAdapter<String> adapter    = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu_data);
		final DrawerLayout drawer       = (DrawerLayout)findViewById(R.id.drawer_layout);
		final ListView navList          = (ListView) findViewById(R.id.drawer);

        navList.setAdapter(adapter);
		navList.setOnItemClickListener(new OnItemClickListener(){
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, final int pos, long id)
            {
		        drawer.setDrawerListener( new DrawerLayout.SimpleDrawerListener()
                {
		            @Override
		            public void onDrawerClosed(View drawerView)
                    {
		                super.onDrawerClosed(drawerView);
                        switch (pos)
                        {
                            case MENU_FEEDS:
                                if (User.getInstance().getAccKey() == null)
                                {
                                    diplayConnexionView();
                                    break;
                                }
                                else
                                {
                                    emptyBackStack();
                                    FeedsFragment tmp_0 = new FeedsFragment();
                                    FragmentTransaction tx_0 = getSupportFragmentManager().beginTransaction();
                                    tx_0.replace(R.id.main, tmp_0);
                                    tx_0.commit();
                                    actionbarTitle.setText(getResources().getString(R.string.title_feeds));
                                }
                                break;

                           /* case MENU_FEEDBACK:
                                if (User.getInstance().getAccKey() == null)
                                    break;
                                openFeedbackBox();
                                break; */

                            case MENU_DISCONNECT:
                                if (User.getInstance().getAccKey() == null)
                                    break;
                                launchAlertDialog();
                                break;
                        }
		            }
		        });
		        drawer.closeDrawer(navList);
	        }
		 });

        /** Hiding default app icon */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);

        /** Displaying custom ActionBar */
        View mActionBarView = getLayoutInflater().inflate(R.layout.custom_actionbar, null);
        actionBar.setCustomView(mActionBarView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        loading_bar = (ProgressBar)mActionBarView.findViewById(R.id.loading_bar);
        hideLoadingBar();
        actionbarTitle = (TextView)mActionBarView.findViewById(R.id.actionbar_title);
        actionbarTitle.setText(getResources().getString(R.string.title_connexion));
        actionbarBtn = (ImageButton)mActionBarView.findViewById(R.id.actionbar_btn);
        actionbarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (drawer.isDrawerOpen(navList))
                    drawer.closeDrawer(navList);
                else
                    drawer.openDrawer(navList);
            }
        });

        /** Launching default fragment */
        diplayConnexionView();
	}

    public void showLoadingBar()
    {
        if (loading_bar != null)
            loading_bar.setVisibility(View.VISIBLE);
    }

    public void hideLoadingBar()
    {
        if (loading_bar != null)
            loading_bar.setVisibility(View.INVISIBLE);
    }

    void launchAlertDialog()
    {
        /** Custom dialog */
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_alertdialog);
        dialog.setTitle(getResources().getString(R.string.disconnect_title));

        /** Set the custom dialog components */
        TextView text = (TextView) dialog.findViewById(R.id.dialog_content);
        text.setText(getResources().getString(R.string.disconnect_text));

        Button dialog_button_ok = (Button) dialog.findViewById(R.id.dialog_btn_ok);
        dialog_button_ok.setText(getResources().getString(R.string.btn_yes));
        dialog_button_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                /** Clean user account preferences */
                removeSharedPreferences();

                /** Launching default fragment */
                diplayConnexionView();

                dialog.dismiss();
            }
        });
        Button dialog_button_no = (Button) dialog.findViewById(R.id.dialog_btn_answer);
        dialog_button_no.setText(getResources().getString(R.string.btn_no));
        dialog_button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void removeSharedPreferences()
    {
        SharedPreferences.Editor editor =  getSharedPreferences(Constants.PREFERENCES, Context.MODE_PRIVATE).edit();

        editor.putString(Constants.PREFERENCES_USER_LOGIN, null);
        editor.putString(Constants.PREFERENCES_USER_PASSWORD, null);
        editor.commit();
    }

    void openFeedbackBox()
    {
        /** Custom dialog */
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_answerdialog);
        dialog.setTitle(getResources().getString(R.string.title_feedback));

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
                // TODO: Send feedback
                dialog.dismiss();
            }
        });
        dialog_button_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void emptyBackStack()
    {
        FragmentManager fm = this.getSupportFragmentManager();
        for (int i = 0 ; i < fm.getBackStackEntryCount() ; ++i)
        {
            fm.popBackStack();
        }
    }

    public TextView getActionbarTitle()
    {
        return actionbarTitle;
    }

    private void diplayConnexionView()
    {
        emptyBackStack();
        User.getInstance().resetInstance();
        ConnexionFragment tmp = new ConnexionFragment();
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, tmp);
        tx.commit();
        actionbarTitle.setText(getResources().getString(R.string.title_connexion));
    }
}
