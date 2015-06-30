package com.epitech.hubinnovation.notifeed.item;

/**
 * Created by Roro on 03/03/2015.
 */
public class User
{
    /** Definition */
    private String acc_key;

    /** Singleton Management */
    private static User instance = null;

    public static void initInstance()
    {
        if (instance == null)
            instance = new User();
    }

    public static User getInstance()
    {
        if (instance == null)
            initInstance();
        return instance;
    }

    public void resetInstance()
    {
        if (instance != null)
        {
            instance.acc_key = null;
        }
        else
        {
            initInstance();
        }
    }

    private User() {};

    /** Getters / Setters */
    public String getAccKey()
    {
        return acc_key;
    }

    public void setAccKey(String acc_key)
    {
        this.acc_key = acc_key;
    }
}
