package com.team5.other;

public class text {
    public static String getRightString(String original, String specific) {
        if (original == null || specific == null) {
            return "";
        }
        int index = original.indexOf(specific);
        if (index != -1) {
            return original.substring(index + specific.length());
        }
        else {
            return "";
        }
    }
    public static String getSubString(String original, String specific1, String specific2) {
        if (original == null || specific1 == null || specific2 == null) {
            return "";
        }
        int index1 = original.indexOf(specific1);
        int index2 = original.indexOf(specific2);
        if (index1 != -1 && index2 != -1 && index1 < index2) {
            return original.substring(index1 + specific1.length(), index2);
        }
        else {
            return "";
        }
    }
    public static String getLeftString(String original, String specific) {
        if (original == null || specific == null) {
            return "";
        }
        int index = original.indexOf(specific);
        if (index != -1) {
            return original.substring(0, index);
        }
        else {
            return "";
        }
    }




}
