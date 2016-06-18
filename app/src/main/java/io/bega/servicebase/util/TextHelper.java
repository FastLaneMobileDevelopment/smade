package io.bega.servicebase.util;

/**
 * Created by user on 6/2/16.
 */

public class TextHelper {
    public static boolean isEmptyString(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }
}
