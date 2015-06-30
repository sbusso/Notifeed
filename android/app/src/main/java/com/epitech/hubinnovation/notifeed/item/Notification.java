package com.epitech.hubinnovation.notifeed.item;

import com.epitech.hubinnovation.notifeed.Constants;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;

/**
 * Created by Roro on 03/03/2015.
 */
public class Notification
{
    /** Definition */
    private boolean new_notif;
    private Calendar datetime;
    private String content;
    private String id;

    /** Constructors */
    public Notification() {}

    public Notification(boolean new_notif, Calendar datetime, String content, String id)
    {
        this.new_notif  = new_notif;
        this.datetime   = datetime;
        this.content    = content;
        this.id         = id;
    }

    /** Getters / Setters */
    public boolean isNew()
    {
        return new_notif;
    }

    public void setNew(boolean new_notif)
    {
        this.new_notif = new_notif;
    }

    public Calendar getDatetime()
    {
        return datetime;
    }

    public String getDatetimeAsString()
    {
        String date = "";

        if (datetime == null)
            return (date);
        switch (datetime.get(Calendar.DAY_OF_WEEK))
        {
            case Calendar.MONDAY:
                date += Constants.WEEK_MONDAY + " ";
                break;
            case Calendar.TUESDAY:
                date += Constants.WEEK_TUESDAY + " ";
                break;
            case Calendar.WEDNESDAY:
                date += Constants.WEEK_WEDNESDAY + " ";
                break;
            case Calendar.THURSDAY:
                date += Constants.WEEK_THURSDAY + " ";
                break;
            case Calendar.FRIDAY:
                date += Constants.WEEK_FRIDAY + " ";
                break;
            case Calendar.SATURDAY:
                date += Constants.WEEK_SATURDAY + " ";
                break;
            case Calendar.SUNDAY:
                date += Constants.WEEK_SUNDAY + " ";
                break;
        }
        date += datetime.get(Calendar.DAY_OF_MONTH) + " ";
        switch (datetime.get(Calendar.MONTH))
        {
            case Calendar.JANUARY:
                date += Constants.MONTH_JANUARY + " ";
                break;
            case Calendar.FEBRUARY:
                date += Constants.MONTH_FEBRUARY + " ";
                break;
            case Calendar.MARCH:
                date += Constants.MONTH_MARCH + " ";
                break;
            case Calendar.APRIL:
                date += Constants.MONTH_APRIL + " ";
                break;
            case Calendar.MAY:
                date += Constants.MONTH_MAY + " ";
                break;
            case Calendar.JUNE:
                date += Constants.MONTH_JUNE + " ";
                break;
            case Calendar.JULY:
                date += Constants.MONTH_JULY + " ";
                break;
            case Calendar.AUGUST:
                date += Constants.MONTH_AUGUST + " ";
                break;
            case Calendar.SEPTEMBER:
                date += Constants.MONTH_SEPTEMBER + " ";
                break;
            case Calendar.OCTOBER:
                date += Constants.MONTH_OCTOBER + " ";
                break;
            case Calendar.NOVEMBER:
                date += Constants.MONTH_NOVEMBER + " ";
                break;
            case Calendar.DECEMBER:
                date += Constants.MONTH_DECEMBER + " - ";
                break;
        }
        date += datetime.get(Calendar.HOUR_OF_DAY) + ":";
        date += datetime.get(Calendar.MINUTE);
        return date;
    }

    public void setName(Calendar datetime)
    {
        this.datetime = datetime;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
