package com.epitech.hubinnovation.notifeed.soap_request;

import android.os.StrictMode;
import android.widget.Toast;

import com.epitech.hubinnovation.notifeed.Constants;
import com.epitech.hubinnovation.notifeed.soap_object.SoapAccount;
import com.epitech.hubinnovation.notifeed.soap_object.SoapComment;
import com.epitech.hubinnovation.notifeed.soap_object.SoapFollowFeed;
import com.epitech.hubinnovation.notifeed.soap_object.SoapGetFeed;
import com.epitech.hubinnovation.notifeed.soap_object.SoapGetHistory;
import com.epitech.hubinnovation.notifeed.soap_object.SoapIsFollowingFeed;
import com.epitech.hubinnovation.notifeed.soap_object.SoapListFeed;
import com.epitech.hubinnovation.notifeed.soap_object.SoapRegisterDevice;
import com.epitech.hubinnovation.notifeed.soap_object.SoapUnfollowFeed;
import com.epitech.hubinnovation.notifeed.tool.Tool;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by Roro on 31/03/2015.
 */
public class Request
{
    /** Singleton Management */
    private static Request instance = null;

    public static void initInstance()
    {
        if (instance == null)
            instance = new Request();
    }

    public static Request getInstance()
    {
        if (instance == null)
            initInstance();
        return instance;
    }

    private Request() {};

    /** Requests */
    public void launch_request(final String request_name, final RequestCallback.FragmentCallback callback, final Object... request_params)
    {
        RequestInterface request = new RequestInterface(request_name, callback);

        request.execute(request_params);
    }

    public Object register_account(String public_id, String private_id)
    {
        String METHOD_NAME = "register_account";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapAccount acc = new SoapAccount();
        acc.setIn_public_key(public_id);
        acc.setIn_private_key(private_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi = new PropertyInfo();
        pi.setName("public_id");
        pi.setValue(acc.getIn_public_key());
        pi.setType(acc.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("private_id");
        pi2.setValue(acc.getIn_private_key());
        pi2.setType(acc.getClass());
        Request.addProperty(pi);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Account", new SoapAccount().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            acc.setOut_account_key(response.getPropertyAsString(0));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (acc.getOut_account_key());
    }

    public Object register_device(String device_id, String device_type, String app_key, String acc_key)
    {
        String METHOD_NAME = "register_device";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapRegisterDevice acc = new SoapRegisterDevice(device_id, device_type, app_key, acc_key);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi = new PropertyInfo();
        pi.setName("device_id");
        pi.setValue(acc.getIn_device_id());
        pi.setType(acc.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("device_type");
        pi2.setValue(acc.getIn_device_type());
        pi2.setType(acc.getClass());
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("app_key");
        pi3.setValue(acc.getIn_app_key());
        pi3.setType(acc.getClass());
        PropertyInfo pi4 = new PropertyInfo();
        pi4.setName("acc_key");
        pi4.setValue(acc.getIn_acc_key());
        pi4.setType(acc.getClass());
        Request.addProperty(pi);
        Request.addProperty(pi2);
        Request.addProperty(pi3);
        Request.addProperty(pi4);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "RegisterDevice", new SoapRegisterDevice().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            acc.setOut_device_success(Boolean.parseBoolean(tmp));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (acc.getOut_device_success());
    }

    public Object list_feed(String acc_key)
    {
        String METHOD_NAME = "list_feed";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapListFeed user = new SoapListFeed();
        user.setIn_acc_key(acc_key);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi = new PropertyInfo();
        pi.setName("acc_key");
        pi.setValue(user.getIn_acc_key());
        pi.setType(user.getClass());
        Request.addProperty(pi);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "User", new SoapListFeed().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            String[] feed_list = Tool.stringToStringArray(tmp);
            user.setOut_list_feed(feed_list);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (user.getOut_list_feed());
    }

    public Object get_feed(String acc_key, String feed_id)
    {
        String METHOD_NAME = "get_feed";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapGetFeed feed = new SoapGetFeed();
        feed.setIn_acc_key(acc_key);
        feed.setIn_feed_id(feed_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(feed.getIn_acc_key());
        pi1.setType(feed.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("feed_id");
        pi2.setValue(feed.getIn_feed_id());
        pi2.setType(feed.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Feed", new SoapGetFeed().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /** Construct result */
        String[] result = new String[2];
        result[0] = null;
        result[1] = null;

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            feed.setOut_feed(tmp);
            result[0] = feed_id;
            result[1] = feed.getOut_feed();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (result);
    }

    public Object is_following_feed(String acc_key, String feed_id)
    {
        String METHOD_NAME = "is_following_feed";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapIsFollowingFeed feed = new SoapIsFollowingFeed();
        feed.setIn_acc_key(acc_key);
        feed.setIn_feed_id(feed_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(feed.getIn_acc_key());
        pi1.setType(feed.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("feed_id");
        pi2.setValue(feed.getIn_feed_id());
        pi2.setType(feed.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Feed", new SoapIsFollowingFeed().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /** Construct result */
        Object[] result = new Object[2];
        result[0] = null;
        result[1] = false;

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            feed.setOut_follow(Boolean.parseBoolean(tmp));
            result[0] = feed_id;
            result[1] = feed.getOut_follow();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (result);
    }

    public Object follow_feed(String acc_key, String feed_id)
    {
        String METHOD_NAME = "follow_feed";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapFollowFeed feed = new SoapFollowFeed();
        feed.setIn_acc_key(acc_key);
        feed.setIn_feed_id(feed_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(feed.getIn_acc_key());
        pi1.setType(feed.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("feed_id");
        pi2.setValue(feed.getIn_feed_id());
        pi2.setType(feed.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Feed", new SoapFollowFeed().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            feed.setOut_success(Boolean.parseBoolean(tmp));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (feed.getOut_success());
    }

    public Object unfollow_feed(String acc_key, String feed_id)
    {
        String METHOD_NAME = "unfollow_feed";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapUnfollowFeed feed = new SoapUnfollowFeed();
        feed.setIn_acc_key(acc_key);
        feed.setIn_feed_id(feed_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(feed.getIn_acc_key());
        pi1.setType(feed.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("feed_id");
        pi2.setValue(feed.getIn_feed_id());
        pi2.setType(feed.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Feed", new SoapUnfollowFeed().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            feed.setOut_success(Boolean.parseBoolean(tmp));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (feed.getOut_success());
    }

    public Object get_history(String acc_key, String feed_id)
    {
        String METHOD_NAME = "get_history";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapGetHistory history = new SoapGetHistory();
        history.setIn_acc_key(acc_key);
        history.setIn_feed_id(feed_id);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(history.getIn_acc_key());
        pi1.setType(history.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("feed_id");
        pi2.setValue(history.getIn_feed_id());
        pi2.setType(history.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "History", new SoapGetHistory().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /** Construct result */
        List<Map<String, String>> result = new ArrayList<>();

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            Vector<Map<String, String>> res = (Vector<Map<String, String>>)response.getProperty(0);
            for (int i = 0 ; i < res.size() ; ++i)
            {
                Map<String, String> map = new HashMap<>();
                SoapObject soap_object  = (SoapObject) res.get(i);
                for (int j = 0 ; j < soap_object.getPropertyCount() ; ++j)
                {
                    SoapObject item = (SoapObject) soap_object.getProperty(j);
                    map.put(item.getProperty(0).toString(), item.getProperty(1).toString());
                }
                result.add(map);
            }
            history.setOut_notif_list(result);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (history.getOut_notif_list());
    }

    public Object comment(String acc_key, String notif_id, String content)
    {
        String METHOD_NAME = "comment";

        SoapObject Request = new SoapObject(Constants.NAMESPACE, METHOD_NAME);

        /**
         * Create Account with Id to be passed as an argument
         *
         * */
        SoapComment feedback = new SoapComment(acc_key, notif_id, content);

        /**
         * Set the category to be the argument of the web service method
         *
         * */
        PropertyInfo pi1 = new PropertyInfo();
        pi1.setName("acc_key");
        pi1.setValue(feedback.getIn_acc_key());
        pi1.setType(feedback.getClass());
        PropertyInfo pi2 = new PropertyInfo();
        pi2.setName("notif_id");
        pi2.setValue(feedback.getIn_notif_id());
        pi2.setType(feedback.getClass());
        PropertyInfo pi3 = new PropertyInfo();
        pi3.setName("content");
        pi3.setValue(feedback.getIn_content());
        pi3.setType(feedback.getClass());
        Request.addProperty(pi1);
        Request.addProperty(pi2);
        Request.addProperty(pi3);

        /**
         * Set the web service envelope
         *
         * */
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(Request);

        envelope.addMapping(Constants.NAMESPACE, "Comment", new SoapComment().getClass());
        HttpTransportSE androidHttpTransport = new HttpTransportSE(Constants.URL, Constants.REQUEST_TIMEOUT_MILLISECONDS);

        /**
         * Call the web service and retrieve result ... how luvly <3
         *
         * */
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            androidHttpTransport.call(Constants.SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            String tmp = response.getPropertyAsString(0);
            feedback.setOut_success(Boolean.parseBoolean(tmp));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (feedback.getOut_success());
    }
}
