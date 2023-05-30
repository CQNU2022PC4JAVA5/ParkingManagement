package com.team5.websocket;

import java.io.*;

public class FileReader {
    public FileReader() {
    }

    public static String readFile(String fileName) {
        String result="";
        File file = new File(fileName);
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;
            while ((tempchar = reader.read()) != -1) {
                if (((char) tempchar) != '\r') {
                    result+=(char) tempchar;
                    //System.out.print((char) tempchar);
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }





}
