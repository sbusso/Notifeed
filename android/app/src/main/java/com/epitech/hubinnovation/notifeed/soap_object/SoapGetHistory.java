package com.epitech.hubinnovation.notifeed.soap_object;

import com.epitech.hubinnovation.notifeed.tool.Tool;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Roro on 30/03/2015.
 */
public class SoapGetHistory implements KvmSerializable
{
    private String in_acc_key                           = null;
    private String in_feed_id                           = null;
    private List<Map<String, String>> out_notif_list  = null;

    public void setOut_notif_list(List<Map<String, String>> list)
    {
        this.out_notif_list = list;
    }

    public List<Map<String, String>> getOut_notif_list()
    {
        return out_notif_list;
    }

    public String getIn_acc_key()
    {
        return in_acc_key;
    }

    public void setIn_acc_key(String in_acc_key)
    {
        this.in_acc_key = in_acc_key;
    }

    public String getIn_feed_id()
    {
        return in_feed_id;
    }

    public void setIn_feed_id(String in_feed_id)
    {
        this.in_feed_id = in_feed_id;
    }

    public SoapGetHistory()
    {
        //out_list_feed = new ArrayList<>();
    }

    public SoapGetHistory(String acc_key, String feed_id)
    {
        in_acc_key  = acc_key;
        in_feed_id  = feed_id;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return in_acc_key;
            case 1:
                return in_feed_id;
            case 2:
                return out_notif_list;
        }
        return null;
    }

    public int getPropertyCount() {
        return 3;
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
                info.name = "feed_id";
                break;
            case 2:
                info.type = PropertyInfo.VECTOR_CLASS;
                info.name = "notif_list";
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
                in_feed_id = value.toString();
                break;
            case 2:
                out_notif_list = Tool.stringToMapList(value.toString());
                break;
            default:
                break;
        }
    }
}
