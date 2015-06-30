package com.epitech.hubinnovation.notifeed.item;

/**
 * Created by Roro on 30/06/2015.
 */
public class TmpFeedFollow
{
    private int     index;
    private Boolean subscribed;
    private String  id;

    public TmpFeedFollow(int _index, Boolean _subscribed, String _id)
    {
        this.index      = _index;
        this.subscribed = _subscribed;
        this.id         = _id;
    }

    public int getIndex()
    {
        return index;
    }

    public String getId()
    {
        return id;
    }

    public Boolean getSubscribed()
    {
        return subscribed;
    }
}
