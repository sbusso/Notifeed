package com.epitech.hubinnovation.notifeed.item;

/**
 * Created by Roro on 03/03/2015.
 */
public class Feed
{
    /** Definition */
    private boolean subscribed;
    private String name;
    private String id;
    private int nb_new_notification;

    /** Constructors */
    private Feed() {}

    public Feed(boolean subscribed, String name, int nb_new_notification, String id)
    {
        this.id                     = id;
        this.name                   = name;
        this.subscribed             = subscribed;
        this.nb_new_notification    = nb_new_notification;
    }

    /** Getters / Setters */
    public boolean isSubscribed()
    {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed)
    {
        this.subscribed = subscribed;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public int getNb_new_notification()
    {
        return nb_new_notification;
    }

    public void setNb_new_notification(int nb_new_notification)
    {
        this.nb_new_notification = nb_new_notification;
    }
}
