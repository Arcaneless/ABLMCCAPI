package com.arcaneless.ablmccapi;

import java.util.List;

public class Utils {

    public static boolean endOf(List list, Object obj) {
        return list.indexOf(obj) + 1 == list.size();
    }
}
