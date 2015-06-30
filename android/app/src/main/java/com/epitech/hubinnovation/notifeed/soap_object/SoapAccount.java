package com.epitech.hubinnovation.notifeed.soap_object;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by Roro on 30/03/2015.
 */
public class SoapAccount implements KvmSerializable
{
    private String in_public_key    = null;
    private String in_private_key   = null;

    public String getOut_account_key() {
        return out_account_key;
    }

    public void setOut_account_key(String out_account_key) {
        this.out_account_key = out_account_key;
    }

    public String getIn_public_key() {
        return in_public_key;
    }

    public void setIn_public_key(String in_public_key) {
        this.in_public_key = in_public_key;
    }

    public String getIn_private_key() {
        return in_private_key;
    }

    public void setIn_private_key(String in_private_key) {
        this.in_private_key = in_private_key;
    }

    private String out_account_key  = null;

    public SoapAccount() {
    }

    public SoapAccount(String pub_key, String priv_key) {
        in_public_key  = pub_key;
        in_private_key = priv_key;
        out_account_key = null;
    }

    public Object getProperty(int arg0) {
        switch (arg0) {
            case 0:
                return in_public_key;
            case 1:
                return in_private_key;
            case 2:
                return out_account_key;
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
                info.name = "id_public";
                break;
            case 1:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "id_private";
                break;
            case 2:
                info.type = PropertyInfo.STRING_CLASS;
                info.name = "acc_key";
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
                in_public_key = value.toString();
                break;
            case 1:
                in_private_key = value.toString();
                break;
            case 2:
                out_account_key = value.toString();
                break;
            default:
                break;
        }
    }
}
