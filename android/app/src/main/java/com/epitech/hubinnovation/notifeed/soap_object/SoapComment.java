package com.epitech.hubinnovation.notifeed.soap_object;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Roro on 30/03/2015.
 */
public class SoapComment implements KvmSerializable
{
    private String in_acc_key   = null;
    private String in_notif_id  = null;
    private String in_content   = null;
    private boolean out_success;

    public void setOut_success(boolean success)
    {
        this.out_success = success;
    }

    public boolean getOut_success()
    {
        return out_success;
    }

    public String getIn_acc_key()
    {
        return in_acc_key;
    }

    public void setIn_acc_key(String in_acc_key)
    {
        this.in_acc_key = in_acc_key;
    }

    public String getIn_notif_id()
    {
        return in_notif_id;
    }

    public void setIn_notif_id(String notif_id)
    {
        this.in_notif_id = notif_id;
    }

    public String getIn_content()
    {
        return in_content;
    }

    public void setIn_content(String content)
    {
        this.in_content = content;
    }

    public SoapComment()
    {
    }

    public SoapComment(String acc_key, String notif_id, String comment)
    {
        in_acc_key  = acc_key;
        in_notif_id = notif_id;
        in_content  = comment;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return in_acc_key;
            case 1:
                return in_notif_id;
            case 2:
                return in_content;
            case 3:
                return out_success;
        }
        return null;
    }

    public int getPropertyCount() {
        return 4;
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
                info.name = "notif_id";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "content";
                break;
            case 3:
                info.type = PropertyInfo.BOOLEAN_CLASS;
                info.name = "success";
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
                in_notif_id = value.toString();
                break;
            case 2:
                in_content = value.toString();
                break;
            case 3:
                out_success = Boolean.parseBoolean(value.toString());
                break;
            default:
                break;
        }
    }
}
