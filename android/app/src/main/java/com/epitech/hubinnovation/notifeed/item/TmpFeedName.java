package com.epitech.hubinnovation.notifeed.item;

/**
 * Created by Roro on 30/06/2015.
 */
public class TmpFeedName
{
    private int      index;
    private String   id;
    private String   name;

    public TmpFeedName(int _index, String _id, String _name)
    {
        this.index  = _index;
        this.id     = _id;
        this.name   = _name;
    }

    public int getIndex()
    {
        return index;
    }

    public String getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
}
