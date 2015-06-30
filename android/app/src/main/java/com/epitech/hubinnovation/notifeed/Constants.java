package com.epitech.hubinnovation.notifeed;

import java.text.SimpleDateFormat;
import android.annotation.SuppressLint;

/**
 * 
 * @author rodolphe.assere
 *
 */
@SuppressLint("SimpleDateFormat")
public class Constants
{
    /** Global */
    public static final int TWEET_LENGTH                    = 140;
    public static final int REQUEST_TIMEOUT_MILLISECONDS    = 10000;

    /** Server connection informations */
    public static final String NAMESPACE = "http://notifeed.exanis.ovh";
    public static final String SOAP_ACTION = "http://notifeed.exanis.ovh/#register_account";
    public static final String URL = "http://notifeed.exanis.ovh/";

	/**
	 * Preference data stored in the phone/tablet
	 */
	public static final String PREFERENCES 											= "PREFERENCES";
	public static final String PREFERENCES_VISITOR_LOGIN 							= "PREFERENCES_VISITOR_LOGIN";
	public static final String PREFERENCES_VISITOR_SEX 								= "PREFERENCES_VISITOR_SEX";
	public static final String PREFERENCES_VISITOR_AGE 								= "PREFERENCES_VISITOR_AGE";
	public static final String PREFERENCES_SEARCH_CRITERIA_SEX 						= "PREFERENCES_SEARCH_CRITERIA_SEX";
	public static final String PREFERENCES_SEARCH_CRITERIA_MIN_AGE 					= "PREFERENCES_SEARCH_CRITERIA_MIN_AGE";
	public static final String PREFERENCES_SEARCH_CRITERIA_MAX_AGE 					= "PREFERENCES_SEARCH_CRITERIA_MAX_AGE";
	public static final String PREFERENCES_SEARCH_CRITERIA_COUNTRY 					= "PREFERENCES_SEARCH_CRITERIA_COUNTRY";
	public static final String PREFERENCES_SEARCH_CRITERIA_DPT 						= "PREFERENCES_SEARCH_CRITERIA_DPT";
	public static final String PREFERENCES_SEARCH_CRITERIA_COUNTRY_POSITION_ID 		= "PREFERENCES_SEARCH_CRITERIA_COUNTRY_POSITION_ID";
	public static final String PREFERENCES_SEARCH_CRITERIA_DPT_POSITION_ID			= "PREFERENCES_SEARCH_CRITERIA_DPT_POSITION_ID";
	public static final String PREFERENCES_SEARCH_CRITERIA_ONLY_USERS_PICTURE 		= "PREFERENCES_SEARCH_CRITERIA_ONLY_USERS_PICTURE";
	public static final String PREFERENCES_SEARCH_CRITERIA_ONLY_USERS_WITH_ALBUMS	= "PREFERENCES_SEARCH_CRITERIA_ONLY_USERS_WITH_ALBUMS";

	/** COUNTRY FORMAT ISO 3166-1 alpha-3  **/
	public static final String COUNTRY_FRANCE_ISO3      = "FRA";

    /**
     * Day of week
     */
    public static final String WEEK_MONDAY              = "Lundi";
    public static final String WEEK_TUESDAY             = "Mardi";
    public static final String WEEK_WEDNESDAY           = "Mercredi";
    public static final String WEEK_THURSDAY            = "Jeudi";
    public static final String WEEK_FRIDAY              = "Vendredi";
    public static final String WEEK_SATURDAY            = "Samedi";
    public static final String WEEK_SUNDAY              = "Dimanche";

    /**
     * Month of year
     */
    public static final String MONTH_JANUARY            = "Janvier";
    public static final String MONTH_FEBRUARY           = "Février";
    public static final String MONTH_MARCH              = "Mars";
    public static final String MONTH_APRIL              = "Avril";
    public static final String MONTH_MAY                = "Mai";
    public static final String MONTH_JUNE               = "Juin";
    public static final String MONTH_JULY               = "July";
    public static final String MONTH_AUGUST             = "Août";
    public static final String MONTH_SEPTEMBER          = "Septembre";
    public static final String MONTH_OCTOBER            = "Octobre";
    public static final String MONTH_NOVEMBER           = "Novembre";
    public static final String MONTH_DECEMBER           = "Décembre";

    /**
	 * Bundle data
	 */
    public static final String BUNDLE_DATA_FEED_NAME    = "feed_name";
    public static final String BUNDLE_DATA_FEED_ID      = "feed_id";
    public static final String BUNDLE_USER              = "user";

    /**
     * Map keys
     */
    public static final String MAP_KEY_NOTIF_ID         = "id";
    public static final String MAP_KEY_NOTIF_DATE       = "date";
    public static final String MAP_KEY_NOTIF_CONTENT    = "content";

	/**
	 * Notifications
	 */
	public static final int NOTIFICATION_HTTP_REQUEST_FAILURE               = 999000;
	public static final int NOTIFICATION_DIALOGS_LOADED                     = 999001;
	public static final int NOTIFICATION_NEW_MESSAGES_COUNTER_SUCCESSFULL   = 999002;
	public static final int NOTIFICATION_NEW_MESSAGES_LOADED                = 999003;
	public static final int NOTIFICATION_MESSAGE_SENT                       = 999008;
	public static final int NOTIFICATION_DIALOG_DELETED                     = 999009;
	public static final int NOTIFICATION_DIALOG_UPDATED                     = 999010;
}
