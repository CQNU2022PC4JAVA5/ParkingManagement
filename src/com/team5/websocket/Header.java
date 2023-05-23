package com.team5.websocket;

import java.util.ArrayList;

public class Header {
    public static final String br ="\r\n";
    public ArrayList<String> headers=new ArrayList<String>();
    public Header() {
    }
    public void addHearder(String hearder){
        this.headers.add(hearder);
    }
    public void addHearder(String name,String value){
        this.headers.add(name+": "+value);
    }
    public void cleanHearders(){
        this.headers.clear();
    }
    private String _getName(String header){
        String result=header.substring(0,header.indexOf(':'));
        return result;
    }
    private String _getValue(String header){
        String result=header.substring(header.indexOf(':')+1);
        if(result.charAt(0)==' ')
            result=result.substring(1);
        return result;
    }
    public String getValue(String name){
        if(headers.size()==0)
            return null;
        for(String eachheader:headers){
            if(_getName(eachheader).equals(name)){
                return _getValue(eachheader);
            }
        }
        return null;
    }
    public boolean isInclude(String name){
        if(headers.size()==0)
            return false;
        return getValue(name) != null;
    }
}
