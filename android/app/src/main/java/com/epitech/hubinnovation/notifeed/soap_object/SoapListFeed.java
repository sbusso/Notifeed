package com.epitech.hubinnovation.notifeed.soap_object;

import com.epitech.hubinnovation.notifeed.tool.Tool;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by Roro on 30/03/2015.
 */
public class SoapListFeed implements KvmSerializable
{
    private String in_acc_key       = null;
    private String[] out_list_feed  = null;

    public void setOut_list_feed(String[] out_list_feed)
    {
        this.out_list_feed = out_list_feed;
    }

    public String[] getOut_list_feed()
    {
        return out_list_feed;
    }

    public String getIn_acc_key()
    {
        return in_acc_key;
    }

    public void setIn_acc_key(String in_acc_key)
    {
        this.in_acc_key = in_acc_key;
    }

    public SoapListFeed()
    {
    }

    public SoapListFeed(String acc_key) {
        in_acc_key  = acc_key;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return in_acc_key;
            case 1:
                return out_list_feed;
        }
        return null;
    }

    public int getPropertyCount() {
        return 2;
    }

    public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info)
    {
        switch (index)
        {
            case 0:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "acc_key";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "feed_list";
                break;
            default:
                break;
        }
    }

    @Override
    public String getInnerText() {
        return null;
    }

    @Override
    public void setInnerText(String s) {

    }

    public void setProperty(int index, Object value)
    {
        switch (index)
        {
            case 0:
                in_acc_key = value.toString();
                break;
            case 1:
                out_list_feed = Tool.stringToStringArray(value.toString());
                break;
            default:
                break;
        }
    }
}
