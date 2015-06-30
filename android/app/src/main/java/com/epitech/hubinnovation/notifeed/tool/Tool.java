package com.epitech.hubinnovation.notifeed.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Roro on 31/03/2015.
 */
public class Tool
{
    /**
     * Converting objects to byte arrays
     */
    static public byte[] object2Bytes( Object o ) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        return baos.toByteArray();
    }

    /**
     * Converting byte arrays to objects
     */
    static public Object bytes2Object( byte raw[] ) throws IOException, ClassNotFoundException
    {
        ByteArrayInputStream bais = new ByteArrayInputStream( raw );
        ObjectInputStream ois = new ObjectInputStream( bais );
        Object o = ois.readObject();
        return o;
    }

    /**
     * Converting string to int array
     */
    static public ArrayList<Integer> stringToIntArray(String arr) throws NumberFormatException
    {
        String[] items          = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").split(",");
        ArrayList<Integer> list = new ArrayList<>();

        for (String tmp : items)
        {
            list.add(Integer.parseInt(tmp));
        }
        return list;
    }

    /**
     * Converting string to string array
     */
    static public String[] stringToStringArray(String arr) throws NumberFormatException
    {
        String[] items          = arr.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" ", "").split(",");

        return items;
    }

    static public List<Map<String, String>> stringToMapList(String arr) throws NumberFormatException
    {
        return null;
    }
}
