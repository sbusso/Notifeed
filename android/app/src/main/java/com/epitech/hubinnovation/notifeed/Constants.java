package com.epitech.hubinnovation.notifeed;


/**
 * 
 * @author rodolphe.assere
 *
 */
public class Constants
{
    /** Global */
    public static final int TWEET_LENGTH                    = 140;
    public static final int REQUEST_TIMEOUT_MILLISECONDS    = 5000;

    /** Server connection informations */
    public static final String NAMESPACE    = "http://notifeed.exanis.ovh";
    public static final String SOAP_ACTION  = "http://notifeed.exanis.ovh/#register_account";
    public static final String URL          = "http://notifeed.exanis.ovh/";

	/**
	 * Preference data stored in the phone/tablet
	 */
	public static final String PREFERENCES 					    = "PREFERENCES";
	public static final String PREFERENCES_USER_LOGIN 	        = "PREFERENCES_USER_LOGIN";
    public static final String PREFERENCES_USER_PASSWORD 	    = "PREFERENCES_USER_PASSWORD";
    public static final String PREFERENCES_FEEDS_LIST 	        = "PREFERENCES_FEEDS_LIST";
    public static final String PREFERENCES_PROPERTY_REG_ID      = "PREFERENCES_REGISTRATION_ID";
    public static final String PREFERENCES_PROPERTY_APP_VERSION = "PREFERENCES_APPVERSION";

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
    public static final String MONTH_JULY               = "Juillet";
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
    public static final String NOTIFICATION_SENDER_ID                       = "124659942760";
    public static final int NOTIFICATION_PLAY_SERVICES_RESOLUTION_REQUEST  = 9000;
}
