package com.epitech.hubinnovation.notifeed.soap_object;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Roro on 30/03/2015.
 */
public class SoapRegisterDevice implements KvmSerializable
{
    private String in_device_id     = null;
    private String in_device_type   = null;
    private String in_app_key       = null;
    private String in_acc_key       = null;
    private boolean out_success     = false;

    public boolean getOut_device_success() {
        return out_success;
    }

    public void setOut_device_success(boolean out_success) {
        this.out_success = out_success;
    }

    public String getIn_device_id() {
        return in_device_id;
    }

    public void setIn_device_id(String in_device_id) {
        this.in_device_id = in_device_id;
    }

    public String getIn_device_type() {
        return in_device_type;
    }

    public void setIn_device_type(String in_device_type) {
        this.in_device_type = in_device_type;
    }

    public String getIn_app_key() {
        return in_app_key;
    }

    public void setIn_app_key(String in_app_key) {
        this.in_app_key = in_app_key;
    }

    public String getIn_acc_key() {
        return in_acc_key;
    }

    public void setIn_acc_key(String in_acc_key) {
        this.in_acc_key = in_acc_key;
    }

    public SoapRegisterDevice() {
    }

    public SoapRegisterDevice(String device_id, String device_type, String app_key, String acc_key)
    {
        in_device_id    = device_id;
        in_device_type  = device_type;
        in_app_key      = app_key;
        in_acc_key      = acc_key;
        out_success     = false;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return in_device_id;
            case 1:
                return in_device_type;
            case 2:
                return in_app_key;
            case 3:
                return in_acc_key;
            case 4:
                return out_success;
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
                info.name = "device_id";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "device_type";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "app_key";
                break;
            case 3:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "acc_key";
                break;
            case 4:
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
                in_device_id = value.toString();
                break;
            case 1:
                in_device_type = value.toString();
                break;
            case 2:
                in_app_key = value.toString();
                break;
            case 3:
                in_acc_key = value.toString();
                break;
            case 4:
                out_success =  Boolean.parseBoolean(value.toString());
                break;
            default:
                break;
        }
    }
}
