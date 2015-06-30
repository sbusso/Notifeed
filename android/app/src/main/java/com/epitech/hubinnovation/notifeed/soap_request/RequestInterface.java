package com.epitech.hubinnovation.notifeed.soap_request;

import android.os.AsyncTask;

import com.epitech.hubinnovation.notifeed.activity.ConnexionFragment;
import com.epitech.hubinnovation.notifeed.activity.MainActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Roro on 01/04/2015.
 */
public class RequestInterface extends AsyncTask<Object, Integer, Object>
{
    private String request_name                         = null;
    private RequestCallback.FragmentCallback callback   = null;

    RequestInterface(String name, RequestCallback.FragmentCallback cback)
    {
        request_name    = name;
        callback        = cback;
    }

    private RequestInterface() {}

    protected Object doInBackground(Object... params)
    {
        Method[] methods    = Request.class.getMethods();
        Object res          = null;

        for (Method method : methods)
        {
            if (request_name != null && request_name.equals(method.getName()))
            {
                try
                {
                   res = method.invoke(Request.getInstance(), params);

                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                    break;
            }
        }
        return res;
    }

    protected void onProgressUpdate(Integer... progress)
    {
    }

    protected void onPostExecute(Object result)
    {
        if (callback != null)
            callback.onTaskDone(result);
    }
}
